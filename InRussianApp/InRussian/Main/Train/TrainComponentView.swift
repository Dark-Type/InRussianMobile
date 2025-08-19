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
    @StateValue private var stack: ChildStack<AnyObject, any TrainComponentChild>

    init(component: TrainComponent) {
        self.component = component
        _stack = StateValue(component.childStack)
    }

    var body: some View {
        let current = stack.active.instance
        Group {
            if let courses = current as? TrainComponentChildCoursesChild {
                TrainCoursesListView(component: courses.component)
            } else if let sectionDetail = current as? TrainComponentChildSectionDetailChild {
                if let def = sectionDetail.component as? DefaultSectionDetailComponent {
                    SectionDetailHostView(component: def)
                } else {
                    Text("Unsupported SectionDetailComponent")
                }
            } else {
                EmptyView()
            }
        }
        .animation(.default, value: stack.active.instance.hashObject())
    }
}


private struct TrainCoursesListView: View {
    let component: TrainCoursesListComponent
    @StateValue private var state: TrainCoursesState

    init(component: TrainCoursesListComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading {
            ProgressCentered()
        } else {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    ForEach(state.courses, id: \.course.id) { bundle in
                        VStack(alignment: .leading, spacing: 12) {
                            Text(bundle.course.name)
                                .font(.title3).bold()
                            ForEach(bundle.sections, id: \.id) { section in
                                SectionCard(section: section) {
                                    component.onSectionClick(sectionId: section.id)
                                }
                            }
                        }
                    }
                }
                .padding(16)
            }
        }
    }
}

private struct SectionCard: View {
    let section: Shared.Section
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, spacing: 8) {
                Text(section.title)
                    .font(.headline)
                    .frame(maxWidth: .infinity, alignment: .leading)
                ProgressView(value: Double(section.progressPercent), total: 100)
                Text("Прогресс: \(section.progressPercent)% (\(section.completedTasks)/\(section.totalTasks))")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            .padding(12)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color(uiColor: .secondarySystemBackground))
            )
        }
        .buttonStyle(.plain)
    }
}

private struct SectionDetailHostView: View {
    let component: DefaultSectionDetailComponent
    @StateValue private var state: SectionDetailState
    @StateValue private var innerStack: ChildStack<any DefaultSectionDetailComponentInnerConfig, any DefaultSectionDetailComponentInnerChild>

    init(component: DefaultSectionDetailComponent) {
        self.component = component
        _state = StateValue(component.state)
        _innerStack = StateValue(component.childStack)
    }

    var body: some View {
        ZStack {
            let inner = innerStack.active.instance
            if inner is DefaultSectionDetailComponentInnerChildDetailsChild {
                SectionDetailsView(component: component, state: state)
            } else if let tasksChild = inner as? DefaultSectionDetailComponentInnerChildTasksChild {
                TasksComponentView(component: tasksChild.component)
            } else {
                EmptyView()
            }
        }
        .overlay {
            if state.showCompletionDialog {
                CompletionDialog { component.onBack() }
            }
        }
    }
}

private struct SectionDetailsView: View {
    let component: SectionDetailComponent
    let state: SectionDetailState

    var body: some View {
        if state.isLoading || state.section == nil {
            ProgressCentered()
        } else if let section = state.section {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    Text(section.title)
                        .font(.title2).bold()

                    ProgressView(value: Double(section.progressPercent), total: 100)
                    Text("Прогресс: \(section.progressPercent)% (\(section.completedTasks)/\(section.totalTasks))")
                        .font(.caption)
                        .foregroundColor(.secondary)

                    Text("Теория: \(section.completedTheory)/\(section.totalTheory)")
                        .font(.footnote)
                    Text("Практика: \(section.completedPractice)/\(section.totalPractice)")
                        .font(.footnote)

                    Divider()
                        .padding(.vertical, 8)

                    Text("Режимы:")
                        .font(.headline)

                    VStack(spacing: 10) {
                        Button {
                            component.openTasks(option: .continue_)
                        } label: {
                            Text("Продолжить (очередь)")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)

                        Button {
                            component.openTasks(option: .all)
                        } label: {
                            Text("Все задачи")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)

                        Button {
                            component.openTasks(option: .theory)
                        } label: {
                            Text("Только теория")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)

                        Button {
                            component.openTasks(option: .practice)
                        } label: {
                            Text("Только практика")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)
                    }

                    Button {
                        component.onBack()
                    } label: {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                    .padding(.top, 4)
                }
                .padding(16)
            }
        }
    }
}

struct TasksComponentView: View {
    let component: TasksComponent
    @StateValue private var state: TasksState

    init(component: TasksComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading {
            ProgressCentered()
        } else {
            switch state.option {
            case .continue_:
                ContinueQueueTasksView(state: state, component: component)
            case .all, .theory, .practice:
                FilteredTasksListView(state: state, component: component)
            default:
                FilteredTasksListView(state: state, component: component)
            }
        }
    }
}

private struct ContinueQueueTasksView: View {
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 18) {
                Text("Очередь задач")
                    .font(.title2).bold()

                ProgressView(value: Double(state.progressPercent), total: 100)
                Text("Прогресс секции: \(state.progressPercent)% (\(state.completedTasks)/\(state.totalTasks))")
                    .font(.caption)
                    .foregroundColor(.secondary)

                if let task = state.currentQueueTask {
                    VStack(alignment: .leading, spacing: 12) {
                        Text(task.kind == TaskKind.theory ? "ТЕОРИЯ" : "ПРАКТИКА")
                            .font(.caption)
                            .foregroundColor(.secondary)
                        Text(task.text)
                            .font(.body)

                        HStack(spacing: 12) {
                            Button {
                                component.markCurrentAs(correct: true)
                            } label: {
                                Text("Верно")
                                    .frame(maxWidth: .infinity)
                            }
                            .buttonStyle(.borderedProminent)

                            Button {
                                component.markCurrentAs(correct: false)
                            } label: {
                                Text("Неверно")
                                    .frame(maxWidth: .infinity)
                            }
                            .buttonStyle(.bordered)
                        }
                    }
                    .padding(14)
                    .background(
                        RoundedRectangle(cornerRadius: 14)
                            .fill(Color(uiColor: .secondarySystemBackground))
                    )

                    Text("В очереди: \(state.remainingInQueue)")
                        .font(.footnote)
                        .foregroundColor(.secondary)
                } else {
                    Text("Очередь пуста.")
                        .foregroundColor(.secondary)
                }

                Button {
                    component.onBack()
                } label: {
                    Text("Назад")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
                .padding(.top, 8)
            }
            .padding(16)
        }
    }
}

private struct FilteredTasksListView: View {
    let state: TasksState
    let component: TasksComponent

    private var title: String {
        switch state.option {
        case .all: return "Все задачи"
        case .theory: return "Теоретические задачи"
        case .practice: return "Практические задачи"
        case .continue_: return "Очередь"
        default: return "Задачи"
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 18) {
            Text(title)
                .font(.title2).bold()

            ProgressView(value: Double(state.progressPercent), total: 100)
            Text("Прогресс: \(state.progressPercent)% (\(state.completedTasks)/\(state.totalTasks))")
                .font(.caption)
                .foregroundColor(.secondary)

            if state.filteredTasks.isEmpty {
                Text("Нет задач для отображения")
                    .foregroundColor(.secondary)
            } else {
                ScrollView {
                    VStack(spacing: 14) {
                        ForEach(state.filteredTasks, id: \.id) { task in
                            VStack(alignment: .leading, spacing: 6) {
                                Text(task.text)
                                    .font(.body)
                                Text(task.kind == TaskKind.theory ? "Теория" : "Практика")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                            .padding(14)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(Color(uiColor: .secondarySystemBackground))
                            )
                        }
                    }
                    .padding(.vertical, 4)
                }
            }

            Button {
                component.onBack()
            } label: {
                Text("Назад")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
            .padding(.top, 4)
        }
        .padding(16)
    }
}

private struct CompletionDialog: View {
    let onClose: () -> Void
    var body: some View {
        ZStack {
            Color.black.opacity(0.35)
                .ignoresSafeArea()
            VStack(spacing: 16) {
                Text("Секция завершена")
                    .font(.headline)
                Text("Вы завершили все задачи секции!")
                    .font(.subheadline)
                    .multilineTextAlignment(.center)
                Button {
                    onClose()
                } label: {
                    Text("OK")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
            }
            .padding(20)
            .frame(maxWidth: 320)
            .background(
                RoundedRectangle(cornerRadius: 20)
                    .fill(Color(uiColor: .systemBackground))
            )
            .shadow(radius: 10)
            .padding(32)
        }
        .transition(.opacity)
    }
}

private func ProgressCentered() -> some View {
    AnyView(
        VStack(spacing: 12) {
            ProgressView()
            Text("Загрузка...")
                .font(.footnote)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    )
}

private extension TrainComponentChild {
    func hashObject() -> Int { (self as AnyObject).hash }
}
