//
//  CoursesListComponentView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI
import Shared
struct CoursesListComponentView: View {
    let component: CoursesListComponent
    @StateValue private var state: CoursesListState

    init(component: CoursesListComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if state.isLoading {
                    Text("Загрузка...")
                        .font(.headline)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding(.top, 40)
                } else {
                    if !state.enrolled.isEmpty {
                        Text("Ваши курсы")
                            .font(.title3).bold()
                        VStack(spacing: 8) {
                            ForEach(state.enrolled, id: \.id) { course in
                                Button {
                                    component.onEnrolledCourseClick(courseId: course.id)
                                } label: {
                                    HStack {
                                        Text(course.name)
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        Image(systemName: "chevron.right")
                                            .foregroundColor(.secondary)
                                    }
                                }
                                .buttonStyle(.borderedProminent)
                            }
                        }
                        Divider().padding(.top, 8)
                    }

                    Text("Рекомендованные")
                        .font(.title3).bold()

                    if state.recommended.isEmpty {
                        Text("Нет рекомендаций")
                            .foregroundColor(.secondary)
                    } else {
                        VStack(spacing: 8) {
                            ForEach(state.recommended, id: \.id) { course in
                                Button {
                                    component.onRecommendedCourseClick(courseId: course.id)
                                } label: {
                                    HStack {
                                        Text(course.name)
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        Image(systemName: "plus")
                                            .foregroundColor(.secondary)
                                    }
                                }
                                .buttonStyle(.bordered)
                            }
                        }
                    }
                }
            }
            .padding(16)
        }
    }
}
