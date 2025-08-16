//
//  TrainComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct TrainComponentView: View {
    let component: TrainComponent

    @StateValue private var childStack: ChildStack<AnyObject, any TrainComponentChild>

    init(component: TrainComponent) {
        self.component = component
        _childStack = StateValue(component.childStack)
    }

    var body: some View {
        let current = childStack.active.instance

        Group {
            if let coursesChild = current as? TrainComponentChildCoursesChild {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Список курсов")
                        .font(.headline)
                    ForEach(coursesChild.component.sections, id: \.id) { section in
                        Button(action: {
                            coursesChild.component.onSectionClick(sectionId: section.id)
                        }) {
                            Text(section.title)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .buttonStyle(.bordered)
                    }
                }
                .padding()
            } else if let sectionDetailChild = current as? TrainComponentChildSectionDetailChild {
                if let defaultSectionDetail = sectionDetailChild.component as? DefaultSectionDetailComponent {
                    SectionDetailComponentView(defaultSectionDetail: defaultSectionDetail)
                } else {
                    EmptyView()
                }
            } else {
                EmptyView()
            }
        }
    }
}

struct SectionDetailComponentView: View {
    @ObservedObject private var childStack: ObservableValue<ChildStack<any DefaultSectionDetailComponentInnerConfig, any DefaultSectionDetailComponentInnerChild>>
    let defaultSectionDetail: DefaultSectionDetailComponent

    init(defaultSectionDetail: DefaultSectionDetailComponent) {
        self.defaultSectionDetail = defaultSectionDetail
        self.childStack = ObservableValue(defaultSectionDetail.childStack)
    }

    var body: some View {
        let innerCurrent = childStack.value.active.instance

        Group {
            if innerCurrent is DefaultSectionDetailComponentInnerChildDetailsChild {
                VStack(spacing: 12) {
                    Text("Детали секции: \(defaultSectionDetail.sectionId)")
                        .font(.headline)
                    Button(action: {
                        defaultSectionDetail.openTasks(option: TasksOption.all)
                    }) {
                        Text("Открыть задачи")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.borderedProminent)
                    Button(action: {
                        defaultSectionDetail.onBack()
                    }) {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
                .padding()
            } else if let tasksChild = innerCurrent as? DefaultSectionDetailComponentInnerChildTasksChild {
                TasksComponentView(component: tasksChild.component)
            } else {
                EmptyView()
            }
        }
    }
}
