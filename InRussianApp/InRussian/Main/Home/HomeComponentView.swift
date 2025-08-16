//
//  HomeComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct HomeComponentView: View {
    let component: HomeComponent

    @StateValue private var childStack: ChildStack<AnyObject, any HomeComponentChild>

    init(component: HomeComponent) {
        self.component = component
        _childStack = StateValue(component.childStack)
    }

    var body: some View {
        let current = childStack.active.instance

        Group {
            if let coursesChild = current as? HomeComponentChildCoursesChild {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Список курсов:")
                        .font(.headline)
                    ForEach(coursesChild.component.items, id: \.id) { course in
                        Button(action: {
                            coursesChild.component.onItemClick(courseId: course.id)
                        }) {
                            Text(course.title)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .buttonStyle(.bordered)
                    }
                }
            } else if let detailsChild = current as? HomeComponentChildCourseDetailsChild {
                VStack(alignment: .leading, spacing: 16) {
                    Text("Детали курса: \(detailsChild.component.courseId)")
                        .font(.headline)
                    Button(action: {
                        detailsChild.component.onBack()
                    }) {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
            } else {
                EmptyView()
            }
        }
        .padding()
    }
}
