//
//  TrainComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import AVFoundation
import Shared
import SwiftUI

struct TrainComponentView: View {
    let component: TrainComponent
    @StateValue private var stack: ChildStack<AnyObject, any TrainComponentChild>

    init(component: TrainComponent) {
        self.component = component
        _stack = StateValue(component.childStack as! Value<ChildStack<AnyObject, any TrainComponentChild>>)
    }

    var body: some View {
        let current = stack.active.instance

        Group {
            activeChildView(current)
        }

        .animation(.default, value: ObjectIdentifier(current))
    }

    @ViewBuilder
    private func activeChildView(_ child: AnyObject) -> some View {
        if let courses = child as? TrainComponentChildCoursesChild {
            TrainCoursesListView(component: courses.component)
        } else if let themeTasks = child as? TrainComponentChildThemeTasksChild {
            ThemeTasksView(component: themeTasks.component)
        } else {
            EmptyView()
                .onAppear { assertionFailure("Unhandled TrainComponent child: \(child)") }
        }
    }
}


private extension TrainComponentChild {
    func hashObject() -> Int { (self as AnyObject).hash }
}
