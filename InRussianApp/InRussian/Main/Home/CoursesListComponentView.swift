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
        ScrollView(showsIndicators: false) {
            VStack(alignment: .leading, spacing: 28) {
                
                if !self.state.recommended.isEmpty {
                    Text("Рекомендованные")
                        .font(.footnote.weight(.semibold))
                        .foregroundColor(.primary)
                        .padding(.horizontal, 16)
                        .padding(.top, 4)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 20) {
                            ForEach(self.state.recommended, id: \.id) { course in
                                Button {
                                    self.component.onRecommendedCourseClick(courseId: course.id)
                                } label: {
                                    CourseCardView(
                                        courseTitle: course.name,
                                        courseSubtitle: "\(course.sectionsCountText) • \(course.languageText)"
                                    )
                                }
                                .buttonStyle(.plain)
                            }
                        }
                        .padding(.horizontal, 16)
                        .padding(.bottom, 4)
                    }
                }

                // Enrolled List
                VStack(alignment: .leading, spacing: 12) {
                    Text("Ваши курсы")
                        .font(.footnote.weight(.semibold))
                        .foregroundColor(.primary)
                        .padding(.horizontal, 16)

                    if self.state.enrolled.isEmpty {
                        Text("Пока нет подписок")
                            .font(.footnote)
                            .foregroundColor(.secondary)
                            .padding(.horizontal, 16)
                    } else {
                        LazyVStack(spacing: 12) {
                            ForEach(self.state.enrolled, id: \.id) { course in
                                self.enrolledRow(course: course)
                                    .padding(.horizontal, 16)
                            }
                        }
                    }
                }
                .padding(.bottom, 8)
            }
            .padding(.top, 8)
        }
        .background(self.screenBG)
        .overlay {
            if self.state.isLoading {
                ZStack {
                    self.screenBG.opacity(0.6).ignoresSafeArea()
                    ProgressView("Загрузка...")
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(self.cardBG)
                        )
                }
            }
        }
    }

    // MARK: - Enrolled Row

    @ViewBuilder
    private func enrolledRow(course: Course) -> some View {
        let percent = Int.random(in: 0...100)
        HStack(alignment: .center, spacing: 12) {
            
            AppImages.image(for: AppImages.Mock.mock)
                .resizable()
                .frame(width: 72, height: 56)
                .cornerRadius(12)
            VStack(alignment: .leading, spacing: 2) {
                Text(course.name)
                    .font(.footnote.weight(.semibold))
                    .lineLimit(2)
                Text("\(course.sectionsCountText) • \(course.languageText)")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
            Spacer()
            ProgressCircleView(progressPercent: percent)
        }
        .padding(12)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(AppColors.Palette.componentBackground.color)
        )
        .contentShape(Rectangle())
        .onTapGesture {
            self.component.onEnrolledCourseClick(courseId: course.id)
        }
    }
}

// MARK: - Helpers for derived text (adapt to real model)

private extension Course {
    var sectionsCountText: String {
        let count = Int.random(in: 0...5)

        switch count {
        case 0: return "0 секций"
        case 1: return "1 секция"
        case 2, 3, 4: return "\(count) секции"
        default: return "\(count) секций"
        }

    }

    var languageText: String {
        self.language
    }
}
