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
    @State private var showInfoSheet = false

    private let screenBG = Color(uiColor: .secondarySystemBackground)
    private let cardBG   = Color(uiColor: .systemBackground)

    
    private let basePosterHeight: CGFloat = 220

    init(component: CourseDetailsComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        GeometryReader { rootGeo in
            
            let topInset = rootGeo.safeAreaInsets.top
            ZStack {
                screenBG.ignoresSafeArea()

                ScrollView(showsIndicators: false) {
                    VStack(alignment: .leading, spacing: 24) {
                        if state.isLoading || state.course == nil {
                            ProgressView("Загрузка...")
                                .padding(.top, 80)
                                .frame(maxWidth: .infinity)
                        } else if let course = state.course {
                            posterHeader(course: course, topInset: topInset)
                            descriptionBlock(course: course)
                            enrollButton
                            sectionsList(course: course)
                        }
                    }
                    .padding(.bottom, 40)
                    
                    .padding(.top, -topInset)
                }
            }
            .sheet(isPresented: $showInfoSheet) {
                if let course = state.course {
                    CourseInfoSheet(course: course, dismiss: { showInfoSheet = false })
                        .presentationDetents([.medium, .large])
                }
            }
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showInfoSheet = true
                    } label: {
                        Image(systemName: "info.circle")
                            .symbolRenderingMode(.hierarchical)
                            .foregroundColor(.accentColor)
                            .accessibilityLabel("Информация")
                    }
                }
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        component.onBack()
                    } label: {
                        Image(systemName: "xmark")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(.white)
                            .padding(6)
                            .background(
                                Circle()
                                    .fill(Color( AppColors.Palette.accent.rawValue))
                            )
                            .accessibilityLabel("Закрыть")
                    }
                }
            }
            .toolbarBackground(.hidden, for: .navigationBar)
            .toolbarBackground(.hidden, for: .automatic)
        }
    }

    // MARK: - Poster Header (Safe + Full-Bleed)
    @ViewBuilder
    private func posterHeader(course: Course, topInset: CGFloat) -> some View {
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
        }
        .frame(height: totalHeight)
    }

    // MARK: - Description
    @ViewBuilder
    private func descriptionBlock(course: Course) -> some View {
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

    // MARK: - Enroll Button
    private var enrollButton: some View {
        CustomButton(
            text: state.isEnrolled ? "Отписаться" : "Записаться",
            color: .accentColor,
            isActive: true,
            action: {
                component.toggleEnroll()
            }
        )
        .font(.footnote.weight(.semibold))
        .padding(.horizontal, 16)
    }

    // MARK: - Sections
    @ViewBuilder
    private func sectionsList(course: Course) -> some View {
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
