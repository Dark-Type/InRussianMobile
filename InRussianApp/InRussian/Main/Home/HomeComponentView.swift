//
//  HomeComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct HomeComponentView: View {
    let component: HomeComponent
    @StateValue private var childStack: ChildStack<AnyObject, any HomeComponentChild>

    init(component: HomeComponent) {
        self.component = component
        _childStack = StateValue(component.childStack)
    }

    var body: some View {
        NavigationStack {
            let active = childStack.active.instance
            ZStack {
                Color(uiColor: .secondarySystemBackground)
                    .ignoresSafeArea()

                Group {
                    if let courses = active as? HomeComponentChildCoursesChild {
                        CoursesListComponentView(component: courses.component)
                            .background(Color.clear)
                            .modifier(HomeNavTitleModifier(showTitle: true))
                    } else if let details = active as? HomeComponentChildCourseDetailsChild {
                        CourseDetailsComponentView(component: details.component)
                            .background(Color.clear)
                            .modifier(HomeNavTitleModifier(showTitle: false))
                    } else {
                        EmptyView()
                            .modifier(HomeNavTitleModifier(showTitle: true))
                    }
                }
            }
            .animation(.default, value: childStack.active.instance.hashObject())
        }
    }
}

// MARK: - Navigation Title Control
private struct HomeNavTitleModifier: ViewModifier {
    let showTitle: Bool
    func body(content: Content) -> some View {
        content
            .if(showTitle) { view in
                view
                    .navigationTitle("Курсы")
                    .navigationBarTitleDisplayMode(.large)
            }
            .if(!showTitle) { view in
                view
                    .navigationTitle("") 
                    .navigationBarTitleDisplayMode(.inline)
            }
    }
}

// MARK: - Helpers
private extension HomeComponentChild {
    func hashObject() -> Int {
        (self as AnyObject).hash
    }
}

private extension View {
    @ViewBuilder
    func `if`<ContentOut: View>(_ condition: Bool, transform: (Self) -> ContentOut) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}
