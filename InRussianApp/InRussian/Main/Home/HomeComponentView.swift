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
            ZStack {
                Color(uiColor: .secondarySystemBackground)
                    .ignoresSafeArea()

                activeChildView
                    .transition(.opacity.combined(with: .scale))
            }
            .toolbarBackground(.visible, for: .navigationBar)
            .toolbarBackground(Color(uiColor: .secondarySystemBackground), for: .navigationBar)
            .navigationBarTitleDisplayMode(usesLargeTitle(for: childStack.active.instance) ? .large : .inline)
            .navigationTitle(title(for: childStack.active.instance))
            .animation(.easeInOut(duration: 0.25), value: activeIdentity)
        }
    }

    private var activeIdentity: ObjectIdentifier {
        ObjectIdentifier(childStack.active.instance as AnyObject)
    }

    @ViewBuilder
    private var activeChildView: some View {
        let active = childStack.active.instance
        if let courses = active as? HomeComponentChildCoursesChild {
            CoursesListComponentView(component: courses.component)
        } else if let details = active as? HomeComponentChildCourseDetailsChild {
            CourseDetailsComponentView(component: details.component)
        } else {
            EmptyView()
        }
    }

    private func usesLargeTitle(for child: any HomeComponentChild) -> Bool {
        child is HomeComponentChildCoursesChild
    }

    private func title(for child: any HomeComponentChild) -> String {
        if child is HomeComponentChildCoursesChild {
            return "Главная"
        } else {
            return ""
        }
    }
}
