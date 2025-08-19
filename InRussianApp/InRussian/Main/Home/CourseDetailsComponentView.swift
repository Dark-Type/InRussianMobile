//
//  CourseDetailsComponentView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI
import Shared

struct CourseDetailsComponentView: View {
    let component: CourseDetailsComponent
    @StateValue private var state: CourseDetailsState

    init(component: CourseDetailsComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                if state.isLoading || state.course == nil {
                    Text("Загрузка...")
                        .font(.headline)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding(.top, 40)
                } else if let course = state.course {
                    header(course: course)
                    enrollButton
                    sectionsList
                }
                backButton
            }
            .padding(16)
        }
    }

    @ViewBuilder
    private func header(course: Course) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(course.name)
                .font(.title).bold()
            Text(course.description)
                .font(.body)

            Text("Автор: \(course.authorId)")
                .font(.subheadline)
                .foregroundColor(.secondary)

            VStack(alignment: .leading, spacing: 6) {
                ProgressView(value: Double(state.progressPercent), total: 100)
                Text("Прогресс: \(state.progressPercent)%")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
    }

    private var enrollButton: some View {
        Button {
            component.toggleEnroll()
        } label: {
            Text(state.isEnrolled ? "Отписаться" : "Записаться")
                .frame(maxWidth: .infinity)
        }
        .buttonStyle(state.isEnrolled ? .borderedProminent : .borderedProminent)
    }

    @ViewBuilder
    private var sectionsList: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Секции")
                .font(.title3).bold()

            if state.sections.isEmpty {
                Text("Нет секций")
                    .foregroundColor(.secondary)
            } else {
                ForEach(state.sections, id: \.id) { section in
                    SectionProgressRow(section: section)
                }
            }
        }
    }

    private var backButton: some View {
        Button {
            component.onBack()
        } label: {
            Text("Назад")
                .frame(maxWidth: .infinity)
        }
        .buttonStyle(.bordered)
        .padding(.top, 8)
    }
}
