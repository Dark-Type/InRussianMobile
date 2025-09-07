//
//  CoursesListComponentView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import Shared
import SwiftUI

struct CoursesListComponentView: View {
    let component: CoursesListComponent
    @StateValue private var state: CoursesListState

    init(component: CoursesListComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    private let screenBG = Color(uiColor: .secondarySystemBackground)
    private let cardBG = Color(uiColor: .systemBackground)

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(alignment: .leading, spacing: 28) {

                headerSection

                if !state.recommended.isEmpty {
                    recommendedSection
                }

                enrolledSection
            }
            .padding(.top, 12)
            .padding(.bottom, 28)
        }
        .background(screenBG.ignoresSafeArea())
        .overlay(loadingOverlay)
        .animation(.easeInOut(duration: 0.25), value: state.recommended.count)
        .animation(.easeInOut(duration: 0.25), value: state.enrolled.count)
    }
}

// MARK: - Sections
private extension CoursesListComponentView {

    var headerSection: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Запишитесь на курс или продолжите обучение")
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.leading)
                .padding(.horizontal, 16)
        }
    }

    var recommendedSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(spacing: 8) {
                Text("Рекомендовано для вас")
                    .font(.headline.weight(.semibold))
                if !state.recommended.isEmpty {
                    Text("\(state.recommended.count)")
                        .font(.caption.weight(.medium))
                        .padding(.horizontal, 8)
                        .padding(.vertical, 2)
                        .background(
                            Capsule()
                                .fill(Color.primary.opacity(0.08))
                        )
                        .accessibilityHidden(true)
                }
                Spacer()
            }
            .padding(.horizontal, 16)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 16) {
                    ForEach(state.recommended, id: \.id) { course in
                        Button {
                            component.onRecommendedCourseClick(courseId: course.id)
                        } label: {
                            CourseCardView(
                                courseTitle: course.name,
                                courseSubtitle: "\(course.pseudoTopicsCountText) • \(course.language)"
                            )
                            .accessibilityElement(children: .combine)
                            .accessibilityLabel("\(course.name), \(course.pseudoTopicsCountText), язык: \(course.language)")
                        }
                        .buttonStyle(.plain)
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 4)
            }
        }
    }

    var enrolledSection: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack(spacing: 8) {
                Text("Ваши курсы")
                    .font(.headline.weight(.semibold))

                if !state.enrolled.isEmpty {
                    ActiveCoursesBadge(count: state.enrolled.count)
                        .transition(.opacity.combined(with: .scale))
                }
                Spacer()
            }
            .padding(.horizontal, 16)

            if state.enrolled.isEmpty {
                EmptyEnrolledStateView()
                    .padding(.horizontal, 16)
                    .transition(.opacity)
            } else {
                LazyVStack(spacing: 12) {
                    ForEach(state.enrolled, id: \.id) { course in
                        EnrolledCourseRow(course: course) {
                            component.onEnrolledCourseClick(courseId: course.id)
                        }
                        .padding(.horizontal, 16)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                    }
                }
            }
        }
        .padding(.bottom, 8)
    }

    @ViewBuilder
    var loadingOverlay: some View {
        if state.isLoading {
            ZStack {
                screenBG.opacity(0.55).ignoresSafeArea()
                VStack(spacing: 12) {
                    ProgressView()
                        .progressViewStyle(.circular)
                        .controlSize(.large)
                    Text("Загрузка…")
                        .font(.subheadline.weight(.medium))
                        .foregroundColor(.secondary)
                }
                .padding(28)
                .background(
                    RoundedRectangle(cornerRadius: 20, style: .continuous)
                        .fill(cardBG)
                        .shadow(color: .black.opacity(0.08), radius: 20, y: 8)
                )
                .accessibilityElement(children: .combine)
                .accessibilityLabel("Загрузка")
                .accessibilityAddTraits(.isModal)
            }
            .transition(.opacity)
        }
    }
}

// MARK: - Subviews

private struct ActiveCoursesBadge: View {
    let count: Int
    var body: some View {
        Text("\(count) активных")
            .font(.caption.weight(.medium))
            .foregroundColor(.secondary)
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(
                Capsule()
                    .fill(Color.secondary.opacity(0.15))
            )
            .accessibilityLabel("\(count) активных курсов")
    }
}

private struct EmptyEnrolledStateView: View {
    var body: some View {
        VStack(spacing: 14) {
            Image(systemName: "sparkles")
                .font(.system(size: 32, weight: .medium))
                .symbolRenderingMode(.hierarchical)
                .foregroundStyle(.secondary)
                .padding(.top, 4)

            Text("Пока нет подписок")
                .font(.subheadline.weight(.semibold))

            Text("Посмотрите рекомендованные курсы, чтобы начать обучение.")
                .font(.footnote)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 4)
        }
        .padding(.vertical, 28)
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .fill(Color(uiColor: .systemBackground))
                .shadow(color: .black.opacity(0.05), radius: 8, y: 4)
        )
        .accessibilityElement(children: .combine)
    }
}

private struct EnrolledCourseRow: View {
    let course: CourseModel
    let tap: () -> Void

    // Derived placeholders (stable)
    private var progress: Int { course.stableProgress }
    private var tasksCompleted: Int { course.stableCompletedTasks }
    private var tasksTotal: Int { course.stableTotalTasks }
    private var streakDays: Int { course.stableStreak }
    private var language: String { course.language }

    var body: some View {
        Button(action: tap) {
            HStack(alignment: .center, spacing: 12) {
                AppImages.image(for: AppImages.Mock.mock)
                    .resizable()
                    .scaledToFill()
                    .frame(width: 72, height: 56)
                    .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                    .accessibilityHidden(true)

                VStack(alignment: .leading, spacing: 8) {
                    Text(course.name)
                        .font(.subheadline.weight(.semibold))
                        .lineLimit(2)

                    Text("\(course.pseudoSectionsCountText) • \(language)")
                        .font(.caption)
                        .foregroundColor(.secondary)

                    progressView
                }

                Spacer(minLength: 8)

                ProgressCircleView(progressPercent: progress)
                    .frame(width: 38, height: 38)
                    .accessibilityHidden(true)
            }
            .padding(14)
            .background(
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .fill(AppColors.Palette.componentBackground.color)
            )
        }
        .buttonStyle(.plain)
        .accessibilityElement(children: .ignore)
        .accessibilityLabel("\(course.name). Прогресс \(progress) процентов. Завершено \(tasksCompleted) из \(tasksTotal) задач.")
    }

    private var progressView: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack {
                Text("Прогресс")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Spacer()
                Text("\(progress)%")
                    .font(.caption2.weight(.medium))
            }
            ProgressView(value: Double(progress), total: 100)
                .progressViewStyle(.linear)
                .tint(.accentColor)

            HStack {
                Text("\(tasksCompleted)/\(tasksTotal) задач")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Spacer()
                if streakDays > 0 {
                    Text("\(streakDays) дн. подряд")
                        .font(.caption2.weight(.medium))
                        .foregroundColor(.orange)
                }
            }
        }
    }
}

// MARK: - Placeholder / Pseudo-Random Extensions

private extension CourseModel {
    var _stableSeed: UInt64 {
        var result: UInt64 = 1469598103934665603
        for byte in id.utf8 {
            result ^= UInt64(byte)
            result &*= 1099511628211
        }
        return result
    }

    func _stableRandom(in range: ClosedRange<Int>, salt: UInt64) -> Int {
        var x = _stableSeed &+ salt
        // xorshift64*
        x ^= x >> 12
        x ^= x << 25
        x ^= x >> 27
        let normalized = x &* 2685821657736338717
        let span = range.upperBound - range.lowerBound + 1
        let value = Int(normalized % UInt64(span)) + range.lowerBound
        return value
    }

    // Recommended topics count imitation (Compose used 5..15 random)
    var pseudoTopicsCount: Int {
        _stableRandom(in: 5...15, salt: 11)
    }

    var pseudoTopicsCountText: String {
        "\(pseudoTopicsCount) тем"
    }

    
    var pseudoSectionsCount: Int {
        _stableRandom(in: 5...15, salt: 21)
    }

    var pseudoSectionsCountText: String {
        switch pseudoSectionsCount {
        case 1: return "1 секция"
        case 2,3,4: return "\(pseudoSectionsCount) секции"
        default: return "\(pseudoSectionsCount) секций"
        }
    }

    var stableProgress: Int {
        _stableRandom(in: 25...95, salt: 31)
    }

    var stableCompletedTasks: Int {
        Int(Double(stableProgress) * 0.3)
    }

    var stableTotalTasks: Int {
        max(stableCompletedTasks + _stableRandom(in: 2...6, salt: 41), stableCompletedTasks + 1)
    }

    var stableStreak: Int {
        _stableRandom(in: 0...14, salt: 51)
    }
}
