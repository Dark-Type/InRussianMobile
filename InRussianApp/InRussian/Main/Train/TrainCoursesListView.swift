//
//  TrainCoursesListView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import Shared
import SwiftUI

struct TrainCoursesListView: View {
    let component: TrainCoursesListComponent

    @StateValue private var state: TrainCoursesState
    @State private var expandedCourses: Set<String> = []
    @State private var expandedThemeKeys: Set<String> = []

    init(component: TrainCoursesListComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        NavigationStack {
            ZStack {
                AppColors.Palette.baseBackground.color
                    .ignoresSafeArea()

                content
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .principal) {
                    VStack(spacing: 2) {
                        Text("Тренажер")
                            .font(.headline.weight(.semibold))
                            .foregroundStyle(AppColors.Palette.fontCaptive.color)
                        Text("\(state.courses.count) \(courseWord(state.courses.count))")
                            .font(.caption2)
                            .foregroundStyle(AppColors.Palette.fontInactive.color)
                    }
                }

                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        component.onRefresh()
                        UIImpactFeedbackGenerator(style: .light).impactOccurred()
                    } label: {
                        Image(systemName: "arrow.clockwise")
                    }
                    .tint(AppColors.Palette.fontCaptive.color)
                    .accessibilityLabel("Обновить")
                }
            }
        }
    }

    private func courseWord(_ count: Int) -> String {
        let mod10 = count % 10
        let mod100 = count % 100
        if mod10 == 1 && mod100 != 11 { return "курс" }
        if (2...4).contains(mod10) && !(12...14).contains(mod100) { return "курса" }
        return "курсов"
    }
}

extension TrainCoursesListView {
    @ViewBuilder
    var content: some View {
        CourseListContent(
            state: state,
            expandedCourses: $expandedCourses,
            expandedThemeKeys: $expandedThemeKeys,
            onRefresh: { component.onRefresh() },
            onResumeCourse: { courseId in component.onResumeCourse(courseId: courseId) },
            onOpenTheme: { courseId, path in component.onThemeClick(courseId: courseId, themePath: path) }
        )
    }
}

private struct CourseListContent: View {
    let state: TrainCoursesState
    @Binding var expandedCourses: Set<String>
    @Binding var expandedThemeKeys: Set<String>

    let onRefresh: () -> Void
    let onResumeCourse: (_ courseId: String) -> Void
    let onOpenTheme: (_ courseId: String, _ path: [String]) -> Void

    var body: some View {
        switch (state.isLoading, state.courses.isEmpty, state.error) {
        case (true, true, _):
            LoadingStateView()
                .transition(.opacity)

        case (_, true, let error?) where !error.isEmpty:
            ErrorStateView(error: error, onRetry: onRefresh)
                .transition(.opacity)

        default:
            CourseListScroll(
                courses: state.courses,
                expandedCourses: $expandedCourses,
                expandedThemeKeys: $expandedThemeKeys,
                onRefresh: onRefresh,
                onResumeCourse: onResumeCourse,
                onOpenTheme: onOpenTheme
            )
        }
    }
}

private struct CourseListScroll: View {
    let courses: [CourseUiModel]
    @Binding var expandedCourses: Set<String>
    @Binding var expandedThemeKeys: Set<String>

    let onRefresh: () -> Void
    let onResumeCourse: (_ courseId: String) -> Void
    let onOpenTheme: (_ courseId: String, _ path: [String]) -> Void

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 14) {
                ForEach(courses, id: \.id) { course in
                    CourseCardNodeView(
                        course: course,
                        isExpanded: expandedCourses.contains(course.id),
                        onExpandToggle: { toggleCourse(course.id) },
                        onResume: { onResumeCourse(course.id) },
                        themeContent: {
                            CourseThemesContent(
                                course: course,
                                expandedThemeKeys: $expandedThemeKeys,
                                onOpenTheme: onOpenTheme
                            )
                        }
                    )
                    .padding(.horizontal, 16)
                    .transition(.opacity.combined(with: .scale(scale: 0.98)))
                }

                Spacer(minLength: 16)
            }
            .padding(.top, 8)
        }
        .refreshable { onRefresh() }
        .scrollIndicators(.hidden)
        .background(AppColors.Palette.baseBackground.color)
        .animation(.spring(response: 0.28, dampingFraction: 0.95, blendDuration: 0.15), value: courses.map(\.id))
        .animation(.spring(response: 0.28, dampingFraction: 0.95, blendDuration: 0.15), value: expandedCourses)
        .animation(.spring(response: 0.28, dampingFraction: 0.95, blendDuration: 0.15), value: expandedThemeKeys)
    }

    private func toggleCourse(_ id: String) {
        if expandedCourses.contains(id) {
            expandedCourses.remove(id)
        } else {
            expandedCourses.insert(id)
        }
        UIImpactFeedbackGenerator(style: .light).impactOccurred()
    }
}

private struct CourseThemesContent: View {
    let course: CourseUiModel
    @Binding var expandedThemeKeys: Set<String>

    let onOpenTheme: (_ courseId: String, _ path: [String]) -> Void

    var body: some View {
        if course.themes.isEmpty {
            EmptyThemesLabel()
        } else {
            VStack(spacing: 12) {
                ForEach(course.themes, id: \.id) { theme in
                    ThemeNodeView(
                        courseId: course.id,
                        theme: theme,
                        parentPath: [],
                        expandedKeys: $expandedThemeKeys,
                        onRowTap: { tappedTheme, path in
                            if tappedTheme.childThemes.isEmpty {
                                onOpenTheme(course.id, path)
                            } else {
                                toggleTheme(path: path)
                            }
                        },
                        onDeepNavigate: { _, path in
                            onOpenTheme(course.id, path)
                        }
                    )
                }
            }
            .padding(.top, 4)
        }
    }

    private func toggleTheme(path: [String]) {
        let key = path.joined(separator: ">")
        if expandedThemeKeys.contains(key) {
            expandedThemeKeys.remove(key)
        } else {
            expandedThemeKeys.insert(key)
        }
        UIImpactFeedbackGenerator(style: .light).impactOccurred()
    }
}

private struct EmptyThemesLabel: View {
    var body: some View {
        Text("Пока нет тем")
            .font(.footnote)
            .foregroundStyle(AppColors.Palette.fontInactive.color)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.top, 8)
    }
}

// MARK: - Course Card

private struct CourseCardNodeView<Content: View>: View {
    let course: CourseUiModel
    let isExpanded: Bool
    let onExpandToggle: () -> Void
    let onResume: () -> Void
    @ViewBuilder let themeContent: () -> Content

    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        let accent = accentColor(for: Int(course.percent))
        let strokeColor = AppColors.Palette.stroke.color
        let background = AppColors.Palette.componentBackground.color

        VStack(spacing: 12) {
            Button(action: onExpandToggle) {
                HStack(spacing: 14) {
                    RingProgress(fraction: CGFloat(course.percent) / 100.0, color: accent)
                        .frame(width: 46, height: 46)
                        .accessibilityHidden(true)

                    VStack(alignment: .leading, spacing: 2) {
                        Text(course.title)
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundStyle(AppColors.Palette.fontCaptive.color)
                            .lineLimit(1)
                            .truncationMode(.tail)

                        Text("\(course.solvedTasks)/\(course.totalTasks) задач • \(course.percent)%")
                            .font(.caption2)
                            .foregroundStyle(AppColors.Palette.fontInactive.color)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    Image(systemName: "chevron.right")
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundStyle(AppColors.Palette.fontInactive.color)
                        .rotationEffect(.degrees(isExpanded ? 90 : 0))
                        .animation(.spring(response: 0.22, dampingFraction: 0.95), value: isExpanded)
                        .accessibilityHidden(true)
                }
                .contentShape(Rectangle())
            }
            .buttonStyle(.plain)
            .accessibilityElement(children: .combine)
            .accessibilityLabel("\(course.title), прогресс \(course.percent) процентов")
            .accessibilityAddTraits(.isButton)

            HStack {
                Spacer()
                Button {
                    onResume()
                    UIImpactFeedbackGenerator(style: .light).impactOccurred()
                } label: {
                    Label("Продолжить", systemImage: "play.fill")
                        .labelStyle(.titleAndIcon)
                        .font(.subheadline.weight(.semibold))
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                }
                .buttonStyle(.bordered)
                .tint(accent.opacity(0.9))
                .foregroundStyle(accent)
                .background(
                    Capsule()
                        .fill(accent.opacity(0.12))
                )
                .clipShape(Capsule())
            }

            VStack(spacing: 16) {
                AnimatedLinearProgress(
                    fraction: CGFloat(course.percent) / 100.0,
                    accent: accent
                )
                .frame(height: 10)
                .clipShape(Capsule())
                .accessibilityHidden(true)

                if isExpanded {
                    themeContent()
                        .transition(.asymmetric(insertion: .opacity.combined(with: .move(edge: .top)),
                                                removal: .opacity.combined(with: .move(edge: .top))))
                }
            }
            .opacity(isExpanded ? 1 : 0)
            .frame(maxHeight: isExpanded ? .infinity : 0, alignment: .top)
            .clipped()
        }
        .padding(16)
        .background(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .fill(background)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .stroke(strokeColor, lineWidth: 1)
        )
        .shadow(color: Color.black.opacity(colorScheme == .dark ? 0.2 : 0.06), radius: 8, x: 0, y: 4)
        .accessibilityElement(children: .contain)
    }

    private func accentColor(for percent: Int) -> Color {
        if percent < 30 { return .orange }
        if percent < 85 { return .yellow }
        return .green
    }
}

// MARK: - Theme Node

private enum GuidelineMetrics {
    static let baseInset: CGFloat = 8

    static let step: CGFloat = 18

    static let gapToCard: CGFloat = 6
}

struct ThemeNodeView: View {
    let courseId: String
    let theme: ThemeModel
    let parentPath: [String]
    @Binding var expandedKeys: Set<String>
    let onRowTap: (_ theme: ThemeModel, _ path: [String]) -> Void
    let onDeepNavigate: (_ theme: ThemeModel, _ path: [String]) -> Void
    var depth: Int = 0

    private var path: [String] { parentPath + [theme.id] }
    private var pathKey: String { path.joined(separator: ">") }
    private var isExpanded: Bool { expandedKeys.contains(pathKey) }
    private var hasChildren: Bool { !theme.childThemes.isEmpty }

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Button {
                onRowTap(theme, path)
            } label: {
                HStack(spacing: 10) {
                    if hasChildren {
                        Image(systemName: "chevron.right")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundStyle(accentColor)
                            .rotationEffect(.degrees(isExpanded ? 90 : 0))
                            .animation(.spring(response: 0.22, dampingFraction: 0.95), value: isExpanded)
                            .frame(width: 20, height: 20)
                    } else {
                        Circle()
                            .fill(theme.completionFraction >= 1.0 ? accentColor : accentColor.opacity(0.35))
                            .frame(width: 12, height: 12)
                            .frame(width: 20, height: 20)
                    }

                    VStack(alignment: .leading, spacing: 2) {
                        Text(theme.title)
                            .font(.system(size: 15, weight: .medium))
                            .foregroundStyle(AppColors.Palette.fontCaptive.color)
                            .lineLimit(1)
                            .truncationMode(.tail)

                        HStack(spacing: 4) {
                            Text("\(theme.solvedTasks)/\(theme.totalTasks)")
                                .font(.system(size: 11))
                                .foregroundStyle(AppColors.Palette.fontInactive.color)
                            Text("• \(Int((theme.completionFraction) * 100))%")
                                .font(.system(size: 11).weight(.medium))
                                .foregroundStyle(accentColor)
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    AnimatedLinearProgress(
                        fraction: CGFloat(theme.completionFraction),
                        accent: accentColor
                    )
                    .frame(width: 70, height: 6)
                    .clipShape(Capsule())
                }
                .contentShape(Rectangle())
                .padding(.horizontal, 12)
                .padding(.vertical, 10)
                .background(
                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                        .fill(AppColors.Palette.componentBackground.color)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 12, style: .continuous)
                        .stroke(theme.completionFraction >= 1.0 ? accentColor.opacity(0.6) : AppColors.Palette.stroke.color, lineWidth: 1)
                )
            }
            .buttonStyle(.plain)
            .accessibilityElement(children: .combine)
            .accessibilityLabel("\(theme.title), прогресс \(Int(theme.completionFraction * 100)) процентов")
            .accessibilityHint(hasChildren ? (isExpanded ? "Свернуть тему" : "Развернуть тему") : "Открыть тему")

            if isExpanded && hasChildren {
                VStack(alignment: .leading, spacing: 10) {
                    if let description = theme.description_, !description.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                        Text(description)
                            .font(.system(size: 12))
                            .foregroundStyle(Color(red: 0.36, green: 0.38, blue: 0.40)) // ~0x5C6167
                            .lineSpacing(2)
                            .padding(.bottom, 4)
                    }

                    ForEach(theme.childThemes, id: \.id) { child in
                        ThemeNodeView(
                            courseId: courseId,
                            theme: child,
                            parentPath: path,
                            expandedKeys: $expandedKeys,
                            onRowTap: onRowTap,
                            onDeepNavigate: onDeepNavigate,
                            depth: depth + 1
                        )
                    }
                }
                .padding(.leading, 24) // visual indent for children content (matches Android)
                .transition(.asymmetric(insertion: .opacity.combined(with: .move(edge: .top)),
                                        removal: .opacity.combined(with: .move(edge: .top))))
            }
        }
        // Draw guidelines OUTSIDE the card, to the left.
        .overlay(alignment: .leading) {
            if depth > 0 {
                let gutterWidth = GuidelineMetrics.baseInset
                    + CGFloat(depth) * GuidelineMetrics.step
                    + GuidelineMetrics.gapToCard
                DepthGuidelines(
                    depth: depth,
                    baseInset: GuidelineMetrics.baseInset,
                    step: GuidelineMetrics.step,
                    lineColor: AppColors.Palette.stroke.color // themable stroke color
                )
                .frame(width: gutterWidth)
                .offset(x: -gutterWidth) // push entirely outside the component
                .allowsHitTesting(false)
            }
        }
    }

    private var accentColor: Color {
        if theme.completionFraction >= 0.95 { return .green }
        if theme.completionFraction >= 0.5 { return .yellow }
        return .orange
    }
}

// MARK: - Progress Views

private struct RingProgress: View {
    let fraction: CGFloat
    let color: Color

    var body: some View {
        ZStack {
            Circle()
                .stroke(color.opacity(0.25), lineWidth: 4)

            Circle()
                .trim(from: 0, to: max(0, min(1, fraction)))
                .stroke(
                    AngularGradient(
                        gradient: Gradient(colors: [color.opacity(0.75), color]),
                        center: .center
                    ),
                    style: StrokeStyle(lineWidth: 4, lineCap: .round)
                )
                .rotationEffect(.degrees(-90))
                .animation(.spring(response: 0.32, dampingFraction: 0.95), value: fraction)
        }
    }
}

private struct AnimatedLinearProgress: View {
    let fraction: CGFloat
    let accent: Color

    var body: some View {
        GeometryReader { geo in
            let width = geo.size.width
            let clipped = max(0, min(1, fraction))
            ZStack(alignment: .leading) {
                Capsule()
                    .fill(accent.opacity(0.20))

                Capsule()
                    .fill(LinearGradient(colors: [accent.opacity(0.75), accent],
                                         startPoint: .leading, endPoint: .trailing))
                    .frame(width: width * clipped)
                    .animation(.spring(response: 0.28, dampingFraction: 0.95), value: clipped)
            }
        }
    }
}

// MARK: - Depth Guidelines

private struct DepthGuidelines: View {
    let depth: Int
    let baseInset: CGFloat
    let step: CGFloat
    let lineColor: Color

    var body: some View {
        GeometryReader { geo in

            let scale = UIScreen.main.scale
            let xRaw = baseInset + CGFloat(max(0, depth - 1)) * step
            let alignedX = (xRaw * scale).rounded(.down) / scale + (1.0 / (2.0 * scale))

            Path { path in
                path.move(to: CGPoint(x: alignedX, y: 0))
                path.addLine(to: CGPoint(x: alignedX, y: geo.size.height))
            }
            .stroke(lineColor, style: StrokeStyle(lineWidth: 1, lineCap: .butt, lineJoin: .bevel))
        }
    }
}

// MARK: - States

private struct LoadingStateView: View {
    var body: some View {
        VStack {
            ProgressView()
                .progressViewStyle(.circular)
                .tint(AppColors.Palette.accent.color)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AppColors.Palette.baseBackground.color)
    }
}

private struct ErrorStateView: View {
    let error: String
    let onRetry: () -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: 12) {
                Text("Что-то пошло не так")
                    .font(.headline.weight(.semibold))
                    .foregroundStyle(AppColors.Palette.fontCaptive.color)

                Text(error)
                    .font(.footnote)
                    .foregroundStyle(Color(hex: 0x5F6368))
                    .multilineTextAlignment(.center)

                Button("Повторить") {
                    onRetry()
                }
                .buttonStyle(.borderedProminent)
                .tint(AppColors.Palette.accent.color)
            }
            .padding(32)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AppColors.Palette.baseBackground.color)
    }
}

// MARK: - Helpers

private extension Color {
    init(hex: UInt, alpha: Double = 1.0) {
        let r = Double((hex >> 16) & 0xFF) / 255.0
        let g = Double((hex >> 8) & 0xFF) / 255.0
        let b = Double(hex & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: alpha)
    }
}
