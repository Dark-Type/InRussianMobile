//
//  CourseDetailsComponentView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import Shared
import SwiftUI

struct CourseDetailsComponentView: View {
    let component: CourseDetailsComponent
    @StateValue private var state: CourseDetailsState

    private let screenBG = Color(uiColor: .secondarySystemBackground)
    private let basePosterHeight: CGFloat = 220

    init(component: CourseDetailsComponent) {
        self.component = component
        _state = StateValue(component.state) // mirror subscribeAsState() from Compose
    }

    var body: some View {
        GeometryReader { rootGeo in
            let topInset = rootGeo.safeAreaInsets.top

            ZStack {
                screenBG.ignoresSafeArea()

                if state.isLoading || state.course == nil {
                    ProgressView("Загрузка...")
                        .padding(.top, 80)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if let course = state.course {
                    ScrollView(showsIndicators: false) {
                        VStack(alignment: .leading, spacing: 24) {
                            header(course: course, topInset: topInset)

                            descriptionBlock(course: course)

                            sectionsList(course: course)

                            Color.clear.frame(height: 24)
                        }
                        .padding(.top, -topInset)
                        .padding(.bottom, 40)
                    }
                }
            }
        }
    }

    // MARK: - Poster Header (keeps same component usage: onBack + toggleEnroll)
    @ViewBuilder
    private func header(course: CourseModel, topInset: CGFloat) -> some View {
        let totalHeight = basePosterHeight + topInset

        ZStack(alignment: .bottomLeading) {
            AppImages.image(for: AppImages.Mock.mock)
                .resizable()
                .scaledToFill()
                .frame(height: totalHeight)
                .frame(maxWidth: .infinity)
                .clipped()
                .clipShape(
                    RoundedCornerShape(radius: 24, corners: [.bottomLeft, .bottomRight])
                )
                .ignoresSafeArea(edges: .top)

            // Course title banner
            ZStack(alignment: .leading) {
                Rectangle()
                    .fill(Color.accentColor)
                    .frame(width: 350, height: 72)
                    .clipShape(
                        RoundedCornerShape(radius: 12, corners: [.topRight, .bottomRight])
                    )
                Text(course.name)
                    .font(.headline)
                    .foregroundColor(.white)
                    .padding(.leading, 20)
                    .padding(.trailing, 12)
                    .lineLimit(2)
                    .minimumScaleFactor(0.8)
            }
            .padding(.leading, -8)
            .padding(.bottom, 64)

            // Top controls mirrored from Compose: onBack + toggleEnroll
            HStack {
                Button(action: { component.onBack() }) {
                    Image(systemName: "chevron.backward")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                        .padding(8)
                        .background(
                            Circle().fill(Color( AppColors.Palette.accent.rawValue))
                        )
                }
                .accessibilityLabel("Назад")

                Spacer()

                Button(action: { component.toggleEnroll() }) {
                    Text(state.isEnrolled ? "Вы записаны" : "Записаться")
                        .font(.footnote.weight(.semibold))
                        .foregroundColor(.white)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 8)
                        .background(
                            Capsule().fill(Color( AppColors.Palette.accent.rawValue))
                        )
                }
                .accessibilityLabel(state.isEnrolled ? "Вы записаны" : "Записаться")
            }
            .padding(.horizontal, 16)
            .padding(.top, topInset + 8)
            .frame(maxHeight: .infinity, alignment: .top)
        }
        .frame(height: totalHeight)
    }

    // MARK: - Description
    @ViewBuilder
    private func descriptionBlock(course: CourseModel) -> some View {
        VStack(alignment: .leading, spacing: 14) {
            Text(course.description_ ?? "Описание не добавлено")
                .font(.footnote)

            if state.isEnrolled {
                VStack(alignment: .leading, spacing: 6) {
                    ProgressView(value: Double(state.progressPercent), total: 100)
                        .tint(progressColor(Int(state.progressPercent)))
                    Text("Прогресс: \(state.progressPercent)%")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }
        }
        .padding(.horizontal, 16)
    }

    // MARK: - Sections
    @ViewBuilder
    private func sectionsList(course: CourseModel) -> some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Секции")
                .font(.footnote.weight(.semibold))

            if state.sections.isEmpty {
                Text("Нет секций")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            } else {
                VStack(spacing: 10) {
                    ForEach(state.sections, id: \.id) { section in
                        SectionProgressRow(
                            section: section,
                            showProgress: state.isEnrolled
                        )
                        // If your component exposes a section open action, wire it here:
                        // .contentShape(Rectangle())
                        // .onTapGesture { component.openSection(section.id) }
                    }
                }
            }
        }
        .padding(.horizontal, 16)
    }
}

// MARK: - Info Sheet
private struct CourseInfoSheet: View {
    let course: Course
    let dismiss: () -> Void

    private let screenBG = Color(uiColor: .secondarySystemBackground)

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    HStack(alignment: .center, spacing: 16) {
                        Circle()
                            .fill(Color.accentColor.opacity(0.25))
                            .frame(width: 64, height: 64)
                            .overlay(
                                Image(systemName: "person.fill")
                                    .font(.system(size: 28))
                                    .foregroundColor(.accentColor)
                            )
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Автор: \(course.authorId)")
                                .font(.footnote.weight(.semibold))
                            Text("Дата публикации: \(formattedPublishDate(course))")
                                .font(.caption2)
                                .foregroundColor(.secondary)
                        }
                    }

                    Text(course.description_ ?? " ")
                        .font(.footnote)

                    Spacer(minLength: 0)
                }
                .padding(24)
            }
            .navigationTitle("Информация")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Закрыть") { dismiss() }
                }
            }
        }
    }

    private func formattedPublishDate(_ course: Course) -> String {
        course.createdAt
    }
}
