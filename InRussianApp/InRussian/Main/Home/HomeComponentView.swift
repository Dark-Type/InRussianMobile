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
        let current = childStack.active.instance
        Group {
            if let courses = current as? HomeComponentChildCoursesChild {
                CoursesListComponentView(component: courses.component)
            } else if let details = current as? HomeComponentChildCourseDetailsChild {
                CourseDetailsComponentView(component: details.component)
            } else {
                EmptyView()
            }
        }
        .animation(.default, value: childStack.active.instance.hashObject())
    }
}


// MARK: - Helpers

private extension HomeComponentChild {
    func hashObject() -> Int {
        (self as AnyObject).hash
    }
}
