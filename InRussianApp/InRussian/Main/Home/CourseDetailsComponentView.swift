//
//  CourseDetailsComponentView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import Shared
import SwiftUI

private struct ScrollOffsetKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) { value = nextValue() }
}

// MARK: - Main View

struct CourseDetailsComponentView: View {
    let component: CourseDetailsComponent
    @StateValue private var state: CourseDetailsState

    @State private var scrollY: CGFloat = 0

    private let heroBaseHeight: CGFloat = 300
    private let horizontalPad: CGFloat = 16

    // Achievements layout (now: max 2 per row, fixed size logic)
    private let achievementSpacing: CGFloat = 14
    private let desiredAchievementCardWidth: CGFloat = 142
    private let achievementCardHeight: CGFloat = 150

    // Palette
    private var screenBG: Color { AppColors.Palette.secondaryBackground.color }
    private var cardBG: Color { AppColors.Palette.componentBackground.color }

    init(component: CourseDetailsComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        ZStack {
            screenBG.ignoresSafeArea()

            if state.isLoading {
                loadingState
            } else if let course = state.course {
                ScrollViewReader { _ in
                    TrackableScrollView(offsetChanged: { scrollY = $0 }) {
                        VStack(spacing: 32) {
                            heroHeader(course: course)

                            // Content stack (no horizontal padding here to avoid double padding)
                            VStack(spacing: 28) {
                                authorMetaSection(course: course)
                                descriptionSection(course: course)
                                enrollOrProgressSection(course: course)
                                achievementsSectionIfNeeded
                                sectionsListIfNeeded
                                Spacer(minLength: 8)
                            }
                            .padding(.top, 4)
                        }
                        .padding(.bottom, 40)
                    }
                    .overlay(alignment: .topLeading) {
                        navControlsOverlay
                    }
                }
                .navigationBarHidden(true)
            } else {
                emptyState
            }
        }
        .animation(.easeInOut(duration: 0.25), value: state.isEnrolled)
        .animation(.easeInOut(duration: 0.25), value: state.sections.count)
        .statusBarHidden(false)
    }
}

// MARK: - Loading / Empty

private extension CourseDetailsComponentView {
    var loadingState: some View {
        VStack(spacing: 16) {
            ProgressView().controlSize(.large)
            Text("Загрузка…")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    var emptyState: some View {
        VStack(spacing: 18) {
            Image(systemName: "exclamationmark.circle")
                .font(.system(size: 40))
                .symbolRenderingMode(.hierarchical)
                .foregroundStyle(.secondary)
            Text("Данные курса недоступны")
                .font(.headline)
            Button("Назад") { component.onBack() }
                .buttonStyle(.borderedProminent)
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Hero Header (Parallax + Width‑Limited Name Pill)

private extension CourseDetailsComponentView {

    @ViewBuilder
    func heroHeader(course: CourseModel) -> some View {
        ZStack(alignment: .bottomLeading) {
            GeometryReader { geo in
                let minY = geo.frame(in: .global).minY
                let safeTop = safeTopInset()
                let pullDown = max(0, minY)                 // positive when pulling down
                let negativeScroll = min(0, minY)           // negative when scrolling up
                // Add safeTop so image always covers under system areas
                let stretchHeight = heroBaseHeight + pullDown + safeTop

                AppImages.image(for: AppImages.Mock.mock)
                    .resizable()
                    .scaledToFill()
                    .frame(width: geo.size.width, height: stretchHeight)
                    .clipped()
                    .overlay(
                        LinearGradient(
                            colors: [
                                Color.black.opacity(0.60),
                                Color.black.opacity(0.25),
                                Color.black.opacity(0.75)
                            ],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
                    .clipShape(
                        RoundedCornerShape(
                            radius: cornerRadius(for: negativeScroll),
                            corners: [.bottomLeft, .bottomRight]
                        )
                    )
                    // Anchor the very top (including safe area extension) so stretching grows downward only.
                    // We offset by both pullDown (to counter stretch expansion upward) and safeTop (to lift image under status bar).
                    .offset(y: -(pullDown + safeTop))
                    // IMPORTANT: We DO NOT apply an additional offset for negative scroll (removed the earlier negativeScroll offset)
                    // so the image scrolls at the same pace as the container when moving up.
                    .ignoresSafeArea(edges: .top)
            }
            .frame(height: heroBaseHeight + safeTopInset()) // internal geometry includes safe area height
            .clipped()

            // Fixed Title Pill (independent of parallax geometry)
            let name = course.name
            if !name.isEmpty {
                Text(name)
                    .font(.system(.title2, weight: .bold))
                    .foregroundColor(.white)
                    .multilineTextAlignment(.leading)
                    .lineLimit(2)
                    .minimumScaleFactor(0.75)
                    // Limit width to 60% of screen (simulate 40% right breathing space); scales with text naturally.
                    .frame(maxWidth: UIScreen.main.bounds.width * 0.60, alignment: .leading)
                    .padding(.horizontal, 20)
                    .padding(.vertical, 14)
                    .background(
                        Capsule(style: .continuous)
                            .fill(Color.accentColor.opacity(0.92))
                    )
                    .padding(.leading, 20)
                    .padding(.bottom, 30)
                    .shadow(color: .black.opacity(0.25), radius: 8, y: 4)
                    .accessibilityAddTraits(.isHeader)
            }
        }
        // External frame remains heroBaseHeight so downstream layout (content start) is unchanged.
        .frame(height: heroBaseHeight)
    }

    func cornerRadius(for negativeScroll: CGFloat) -> CGFloat {
        let base: CGFloat = 32
        guard negativeScroll < 0 else { return base }
        let factor = max(0.65, 1 - (-negativeScroll / 260))
        return base * factor
    }

    // Local helper (kept here to avoid exposing globally)
    func safeTopInset() -> CGFloat {
        (UIApplication.shared.connectedScenes
            .compactMap { ($0 as? UIWindowScene)?.keyWindow?.safeAreaInsets.top }
            .first) ?? 0
    }
}

// MARK: - Overlay Navigation

private extension CourseDetailsComponentView {
    var navControlsOverlay: some View {
        HStack {
            CircleButton(icon: "chevron.backward") { component.onBack() }
            Spacer()
            if state.course != nil {
                EnrollToggleButton(isEnrolled: state.isEnrolled) {
                    component.toggleEnroll()
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.top, safeTopInset() + 6)
    }

}

// MARK: - Sections

private extension CourseDetailsComponentView {
    func authorMetaSection(course: CourseModel) -> some View {
        ContentSection(cardBG: cardBG) {
            HStack(alignment: .center, spacing: 16) {
                Circle()
                    .fill(Color.accentColor.opacity(0.18))
                    .frame(width: 60, height: 60)
                    .overlay(
                        Text(String(course.authorId.prefix(1)).uppercased())
                            .font(.system(size: 28, weight: .bold))
                            .foregroundColor(.accentColor)
                    )
                    .accessibilityHidden(true)

                VStack(alignment: .leading, spacing: 6) {
                    Text("Автор")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text(course.authorId)
                        .font(.subheadline.weight(.semibold))
                        .foregroundColor(.primary)
                        .lineLimit(1)

                    Text("Опубликовано: \(course.createdAt.prefix(10))")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }

                Spacer()

                VStack(alignment: .trailing, spacing: 8) {
                    MetaBadge(text: "\(state.sections.count) тем")
                    MetaBadge(text: "\(state.sections.reduce(0) { $0 + $1.totalLessons }) уроков")
                }
            }
        }
        .padding(.horizontal, horizontalPad)
    }

    func descriptionSection(course: CourseModel) -> some View {
        ContentSection(cardBG: cardBG) {
            VStack(alignment: .leading, spacing: 10) {
                Text("Описание")
                    .font(.footnote.weight(.semibold))
                    .foregroundColor(.secondary)

                Text(course.description_?.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty == false
                    ? (course.description_ ?? "")
                    : "Описание не добавлено")
                    .font(.subheadline)
                    .foregroundColor(.primary)
            }
        }
        .padding(.horizontal, horizontalPad)
    }

    @ViewBuilder
    func enrollOrProgressSection(course: CourseModel) -> some View {
        if !state.isEnrolled {
            ContentSection(cardBG: cardBG) {
                VStack(spacing: 14) {
                    Text("Запишитесь, чтобы отслеживать прогресс и получать достижения.")
                        .font(.footnote)
                        .foregroundColor(.secondary)
                        .multilineTextAlignment(.center)

                    Button {
                        component.toggleEnroll()
                    } label: {
                        Text("Записаться")
                            .font(.subheadline.weight(.semibold))
                            .foregroundColor(.white)
                            .padding(.horizontal, 32)
                            .padding(.vertical, 12)
                            .background(
                                Capsule().fill(Color.accentColor)
                            )
                    }
                    .buttonStyle(.plain)
                }
                .frame(maxWidth: .infinity)
            }
            .padding(.horizontal, horizontalPad)
        } else {
            progressCard
                .padding(.horizontal, horizontalPad)
        }
    }

    var progressCard: some View {
        let percent = Int(state.progressPercent)
        let completed = state.sections.reduce(0) { $0 + $1.completedLessons }
        let total = state.sections.reduce(0) { $0 + $1.totalLessons }
        let remaining = max(0, total - completed)

        return ContentSection(cardBG: cardBG) {
            VStack(alignment: .leading, spacing: 18) {
                HStack {
                    Text("Прогресс")
                        .font(.headline.weight(.semibold))
                    Spacer()
                    Text("\(percent)%")
                        .font(.title3.weight(.bold))
                        .foregroundColor(progressColor(percent))
                }

                ProgressView(value: Double(percent), total: 100)
                    .tint(progressColor(percent))
                    .animation(.easeInOut, value: percent)

                HStack {
                    ProgressMiniStat(label: "Завершено", value: "\(completed)")
                    Divider().frame(height: 28)
                    ProgressMiniStat(label: "Осталось", value: "\(remaining)")
                    Divider().frame(height: 28)
                    ProgressMiniStat(label: "Уроков", value: "\(total)")
                }
            }
        }
    }

    func progressColor(_ percent: Int) -> Color {
        switch percent {
        case 0..<20: return .red
        case 20..<50: return .orange
        case 50..<80: return .yellow
        default: return .green
        }
    }

    var achievementsSectionIfNeeded: some View {
        Group {
            if state.isEnrolled {
                let achievements = derivedAchievements(progress: Int(state.progressPercent))
                if !achievements.isEmpty {
                    ContentSection(cardBG: cardBG) {
                        VStack(alignment: .leading, spacing: 16) {
                            Text("Достижения")
                                .font(.headline.weight(.semibold))

                            AchievementsTwoColumnGrid(
                                achievements: achievements,
                                cardHeight: achievementCardHeight,
                                spacing: achievementSpacing,
                                cardBG: cardBG
                            )
                        }
                    }
                    .padding(.horizontal, horizontalPad)
                }
            }
        }
    }

    var sectionsListIfNeeded: some View {
        Group {
            if !state.sections.isEmpty {
                ContentSection(cardBG: cardBG) {
                    VStack(alignment: .leading, spacing: 18) {
                        Text("Темы курса")
                            .font(.headline.weight(.semibold))

                        VStack(spacing: 12) {
                            ForEach(state.sections, id: \.id) { section in
                                SectionRow(section: section, showProgress: state.isEnrolled, background: cardBG)
                            }
                        }
                    }
                }
                .padding(.horizontal, horizontalPad)
            } else if state.course != nil {
                ContentSection(cardBG: cardBG) {
                    Text("Темы отсутствуют")
                        .font(.footnote)
                        .foregroundColor(.secondary)
                        .frame(maxWidth: .infinity, alignment: .center)
                }
                .padding(.horizontal, horizontalPad)
            }
        }
    }
}

// MARK: - Achievements Model

private extension CourseDetailsComponentView {
    struct Achievement: Identifiable {
        let id: String
        let title: String
        let desc: String
        let unlocked: Bool
        let symbol: String
    }

    func derivedAchievements(progress: Int) -> [Achievement] {
        [
            Achievement(id: "first", title: "Первый шаг", desc: "Начните обучение", unlocked: progress > 0, symbol: "figure.walk"),
            Achievement(id: "consistency", title: "Стабильность", desc: "Достигните 10%", unlocked: progress >= 10, symbol: "flame"),
            Achievement(id: "halfway", title: "Половина", desc: "50% прогресса", unlocked: progress >= 50, symbol: "bolt.fill"),
            Achievement(id: "almost", title: "Почти там", desc: "80% пройдено", unlocked: progress >= 80, symbol: "circle.dashed.inset.filled"),
            Achievement(id: "finish", title: "Финиш", desc: "Полное завершение", unlocked: progress >= 100, symbol: "trophy.fill")
        ]
    }
}

// MARK: - Two Column Achievements Grid

private struct AchievementsTwoColumnGrid: View {
    typealias Achievement = CourseDetailsComponentView.Achievement

    let achievements: [Achievement]
    let cardHeight: CGFloat
    let spacing: CGFloat
    let cardBG: Color

    init(
        achievements: [Achievement],
        cardHeight: CGFloat = 150,
        spacing: CGFloat = 14,
        cardBG: Color
    ) {
        self.achievements = achievements
        self.cardHeight = cardHeight
        self.spacing = spacing
        self.cardBG = cardBG
    }

    var body: some View {
        GeometryReader { geo in
            let totalWidth = geo.size.width
            // Compute exact cell width so that two cells + spacing fit perfectly.
            let cellWidth = max(80, floor((totalWidth - spacing) / 2))

            VStack(spacing: spacing) {
                ForEach(rows.indices, id: \.self) { rowIndex in
                    let row = rows[rowIndex]
                    if row.count == 2 {
                        // Two items: fill row exactly
                        HStack(spacing: spacing) {
                            card(for: row[0], width: cellWidth)
                            card(for: row[1], width: cellWidth)
                        }
                        .frame(maxWidth: .infinity, alignment: .center)
                    } else if let single = row.first {
                        // One item: center it (do NOT stretch width)
                        HStack(spacing: 0) {
                            Spacer(minLength: 0)
                            card(for: single, width: cellWidth)
                            Spacer(minLength: 0)
                        }
                        .frame(maxWidth: .infinity)
                    }
                }
            }
            .frame(width: totalWidth, alignment: .top)
        }
        .frame(height: totalHeight) // Prevent layout jumps while measuring
    }

    // MARK: - Rows (chunk into 2)

    private var rows: [[Achievement]] {
        stride(from: 0, to: achievements.count, by: 2).map {
            Array(achievements[$0..<min($0 + 2, achievements.count)])
        }
    }

    // Pre-computed total height for outer GeometryReader frame
    private var totalHeight: CGFloat {
        let rowCount = rows.count
        guard rowCount > 0 else { return 0 }
        return CGFloat(rowCount) * cardHeight + CGFloat(max(0, rowCount - 1)) * spacing
    }

    // MARK: - Card Builder

    @ViewBuilder
    private func card(for achievement: Achievement, width: CGFloat) -> some View {
        AchievementCardView(
            achievement: achievement,
            cardBG: cardBG
        )
        .frame(width: width, height: cardHeight, alignment: .topLeading)
        // Force internal content to align; prevents implicit width expansion
        .contentShape(Rectangle())
    }
}

// MARK: - Reusable Components

private struct CircleButton: View {
    let icon: String
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            Image(systemName: icon)
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(.white)
                .frame(width: 44, height: 44)
                .background(
                    Circle()
                        .fill(Color.accentColor)
                        .shadow(color: .black.opacity(0.25), radius: 6, y: 3)
                )
        }
        .buttonStyle(.plain)
        .accessibilityLabel("Назад")
    }
}

private struct EnrollToggleButton: View {
    let isEnrolled: Bool
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            Text(isEnrolled ? "Вы записаны" : "Записаться")
                .font(.footnote.weight(.semibold))
                .foregroundColor(.white)
                .padding(.horizontal, 18)
                .padding(.vertical, 10)
                .background(
                    Capsule()
                        .fill(isEnrolled ? Color.green : Color.accentColor)
                        .shadow(color: .black.opacity(0.25), radius: 6, y: 3)
                )
        }
        .buttonStyle(.plain)
        .accessibilityLabel(isEnrolled ? "Вы записаны на курс" : "Записаться на курс")
    }
}

private struct MetaBadge: View {
    let text: String
    var body: some View {
        Text(text)
            .font(.caption2.weight(.medium))
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(
                Capsule()
                    .fill(Color.accentColor.opacity(0.12))
            )
            .foregroundColor(.accentColor)
    }
}

private struct ProgressMiniStat: View {
    let label: String
    let value: String
    var body: some View {
        VStack(spacing: 4) {
            Text(value)
                .font(.subheadline.weight(.semibold))
            Text(label.uppercased())
                .font(.caption2.weight(.medium))
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
    }
}

private struct AchievementCardView: View {
    let achievement: CourseDetailsComponentView.Achievement
    let cardBG: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Image(systemName: achievement.symbol)
                .font(.system(size: 22, weight: .semibold))
                .foregroundColor(achievement.unlocked ? .accentColor : .secondary)

            Text(achievement.title)
                .font(.subheadline.weight(.semibold))
                .lineLimit(1)
                .truncationMode(.tail)

            Text(achievement.desc)
                .font(.caption)
                .foregroundColor(.secondary)
                .lineLimit(2)
                .fixedSize(horizontal: false, vertical: true)

            Spacer(minLength: 0)

            if achievement.unlocked {
                Text("Открыто")
                    .font(.caption2.weight(.medium))
                    .foregroundColor(.green)
                    .padding(.top, 2)
            }
        }
        .padding(14)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .background(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .fill(
                    achievement.unlocked
                        ? Color.accentColor.opacity(0.12)
                        : cardBG.opacity(0.85)
                )
        )
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(
                    achievement.unlocked
                        ? Color.accentColor.opacity(0.35)
                        : Color.secondary.opacity(0.15),
                    lineWidth: 1
                )
        )
        .contentShape(Rectangle())
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(achievement.title). \(achievement.unlocked ? "Открыто" : "Закрыто")")
    }
}

// MARK: - Content Section Wrapper

private struct ContentSection<Content: View>: View {
    @Environment(\.colorScheme) private var scheme
    let cardBG: Color
    let content: () -> Content
    init(cardBG: Color, @ViewBuilder content: @escaping () -> Content) {
        self.cardBG = cardBG
        self.content = content
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            content()
                .padding(20)
        }
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 24, style: .continuous)
                .fill(cardBG)
        )
        .shadow(color: .black.opacity(scheme == .dark ? 0.30 : 0.05), radius: 10, y: 4)
    }
}

// MARK: - Trackable Scroll

private struct TrackableScrollView<Content: View>: View {
    let axis: Axis.Set
    let showsIndicators: Bool
    let offsetChanged: (CGFloat) -> Void
    let content: Content

    init(axis: Axis.Set = .vertical,
         showsIndicators: Bool = false,
         offsetChanged: @escaping (CGFloat) -> Void,
         @ViewBuilder content: () -> Content)
    {
        self.axis = axis
        self.showsIndicators = showsIndicators
        self.offsetChanged = offsetChanged
        self.content = content()
    }

    var body: some View {
        ScrollView(axis, showsIndicators: showsIndicators) {
            GeometryReader { geo in
                Color.clear
                    .preference(
                        key: ScrollOffsetKey.self,
                        value: geo.frame(in: .global).minY
                    )
            }
            .frame(height: 0)
            content
        }
        .onPreferenceChange(ScrollOffsetKey.self, perform: offsetChanged)
    }
}
