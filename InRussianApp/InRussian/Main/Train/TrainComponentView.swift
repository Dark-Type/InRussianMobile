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
        _stack = StateValue(component.childStack as! Value<ChildStack<AnyObject, any TrainComponentChild>>)
    }

    var body: some View {
        let current = stack.active.instance
        Group {
            if let courses = current as? TrainComponentChildCoursesChild {
                TrainCoursesListView(component: courses.component)
            } else if let sectionDetail = current as? TrainComponentChildSectionDetailChild {
                SectionDetailHostView(component: sectionDetail.component)
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
                            Text(bundle.course.title)
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
    let component: SectionDetailComponent
    @StateValue private var state: SectionDetailState
    @StateValue private var innerStack: ChildStack<any DefaultSectionDetailComponentInnerConfig, any DefaultSectionDetailComponentInnerChild>

    init(component: SectionDetailComponent) {
        self.component = component
        _state = StateValue(component.state)
        if let defaultComponent = component as? DefaultSectionDetailComponent {
            _innerStack = StateValue(defaultComponent.childStack)
        }
        else {
            fatalError()
        }
    }

    var body: some View {
        NavigationView {
            ZStack {
                if let defaultComponent = component as? DefaultSectionDetailComponent {
                    let inner = innerStack.active.instance
                    if inner is DefaultSectionDetailComponentInnerChildDetailsChild {
                        SectionDetailsView(component: component, state: state)
                    } else if let tasksChild = inner as? DefaultSectionDetailComponentInnerChildTasksChild {
                        TasksComponentView(component: tasksChild.component)
                    } else {
                        EmptyView()
                    }
                } else {
                    // Fallback for non-default components
                    SectionDetailsView(component: component, state: state)
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        component.onBack()
                    }) {
                        HStack(spacing: 4) {
                            Image(systemName: "chevron.left")
                            Text("Назад")
                        }
                    }
                }
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
        NavigationView {
            VStack {
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
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: {
                        component.onBack()
                    }) {
                        HStack(spacing: 4) {
                            Image(systemName: "chevron.left")
                            Text("Назад")
                        }
                    }
                }
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

                if let fullTask = state.activeFullTask {
                    TaskContentView(
                        fullTask: fullTask,
                        state: state,
                        component: component
                    )

                    Text("В очереди: \(state.remainingInQueue)")
                        .font(.footnote)
                        .foregroundColor(.secondary)
                } else {
                    Text("Очередь пуста.")
                        .foregroundColor(.secondary)
                }
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

            if let activeTask = state.activeFullTask {
                TaskContentView(
                    fullTask: activeTask,
                    state: state,
                    component: component
                )
            } else if state.filteredTasks.isEmpty {
                Text("Нет задач для отображения")
                    .foregroundColor(.secondary)
            } else {
                Text("Задачи завершены")
                    .foregroundColor(.secondary)
            }
        }
        .padding(16)
    }
}

private struct TaskContentView: View {
    let fullTask: FullTask
    let state: TasksState
    let component: TasksComponent

    private var taskTypeLabel: String {
        switch fullTask.task.isTheory {
        case true:
            return "ТЕОРИЯ"
        case false:
            return "ПРАКТИКА"
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(taskTypeLabel)
                .font(.caption)
                .foregroundColor(.secondary)

            Text(fullTask.task.text ?? "")
                .font(.body)

            ForEach(fullTask.contents, id: \.id) { contentItem in
                TaskContentItemView(contentItem: contentItem)
            }

            if let answer = fullTask.answer {
                TaskInteractionView(
                    fullTask: fullTask,
                    answerType: answer.answerType,
                    state: state,
                    component: component
                )
            }

            SubmissionArea(state: state, component: component)
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(Color(uiColor: .secondarySystemBackground))
        )
    }
}

private struct TaskContentItemView: View {
    let contentItem: TaskContentItem

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            switch contentItem.contentType {
            case ContentType.text:
                if let text = contentItem.description_ {
                    Text(text)
                        .font(.body)
                }
            case ContentType.image:
                if let imageUrl = contentItem.contentId {
                    AsyncImage(url: URL(string: imageUrl)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                    } placeholder: {
                        Rectangle()
                            .foregroundColor(.gray.opacity(0.3))
                            .frame(height: 150)
                    }
                    .frame(maxHeight: 200)
                    .cornerRadius(8)
                }
            case ContentType.audio:
                if let audioUrl = contentItem.contentId {
                    HStack {
                        Image(systemName: "play.circle")
                        Text("Аудио")
                        Spacer()
                    }
                    .padding(12)
                    .background(Color.blue.opacity(0.1))
                    .cornerRadius(8)
                }
            default:
                EmptyView()
            }
        }
    }
}

private struct TaskInteractionView: View {
    let fullTask: FullTask
    let answerType: AnswerType
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        switch answerType {
        case AnswerType.singleChoiceShort, AnswerType.singleChoiceLong:
            SingleChoiceView(fullTask: fullTask, state: state, component: component)
        case AnswerType.multipleChoiceShort, AnswerType.multipleChoiceLong:
            MultipleChoiceView(fullTask: fullTask, state: state, component: component)
        case AnswerType.textInput:
            TextInputView(state: state, component: component)
        case AnswerType.wordOrder:
            WordOrderView(fullTask: fullTask, state: state, component: component)
        case AnswerType.wordSelection:
            WordSelectionView(fullTask: fullTask, state: state, component: component)
        default:
            EmptyView()
        }
    }
}

private struct SingleChoiceView: View {
    let fullTask: FullTask
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        VStack(spacing: 8) {
            ForEach(fullTask.options, id: \.id) { option in
                SingleOptionButton(
                    option: option,
                    isSelected: state.singleSelection == option.id,
                    onTap: {
                        component.selectOption(optionId: option.id)
                    }
                )
            }
        }
    }
}

private struct SingleOptionButton: View {
    let option: TaskAnswerOptionItem
    let isSelected: Bool
    let onTap: () -> Void
    
    var body: some View {
        Button(action: onTap) {
            HStack {
                Text(option.optionText ?? "")
                Spacer()
                optionIcon
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(backgroundView)
        }
        .buttonStyle(.plain)
    }
    
    private var optionIcon: some View {
        Group {
            if isSelected {
                Image(systemName: "checkmark.circle.fill")
                    .foregroundColor(.blue)
            } else {
                Image(systemName: "circle")
                    .foregroundColor(.gray)
            }
        }
    }
    
    private var backgroundView: some View {
        RoundedRectangle(cornerRadius: 8)
            .fill(isSelected ? Color.blue.opacity(0.1) : Color.clear)
    }
}

private struct MultipleChoiceView: View {
    let fullTask: FullTask
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        VStack(spacing: 8) {
            ForEach(fullTask.options, id: \.id) { option in
                Button(action: {
                    component.toggleOption(optionId: option.id)
                }) {
                    HStack {
                        Text(option.optionText ?? "")
                        Spacer()
                        if state.multiSelection.contains(option.id) {
                            Image(systemName: "checkmark.square.fill")
                                .foregroundColor(.blue)
                        } else {
                            Image(systemName: "square")
                                .foregroundColor(.gray)
                        }
                    }
                    .padding(.horizontal, 12)
                    .padding(.vertical, 8)
                    .background(
                        RoundedRectangle(cornerRadius: 8)
                            .fill(state.multiSelection.contains(option.id) ? Color.blue.opacity(0.1) : Color.clear)
                    )
                }
                .buttonStyle(.plain)
            }
        }
    }
}

private struct TextInputView: View {
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        TextField("Введите ответ...", text: Binding(
            get: { state.textInput },
            set: { component.updateTextInput(text: $0) }
        ))
        .textFieldStyle(.roundedBorder)
    }
}

private struct WordOrderView: View {
    let fullTask: FullTask
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Расставьте слова в правильном порядке:")
                .font(.caption)
                .foregroundColor(.secondary)

            LazyVGrid(columns: [GridItem(.adaptive(minimum: 80))], spacing: 8) {
                ForEach(Array(state.wordOrder.enumerated()), id: \.offset) { _, wordId in
                    if let option = fullTask.options.first(where: { $0.id == wordId }) {
                        Text(option.optionText ?? "")
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(Color.blue.opacity(0.2))
                            .cornerRadius(8)
                    }
                }
            }

            Divider()

            LazyVGrid(columns: [GridItem(.adaptive(minimum: 80))], spacing: 8) {
                ForEach(fullTask.options.filter { !state.wordOrder.contains($0.id) }, id: \.id) { option in
                    Button(action: {
                        component.selectOption(optionId: option.id)
                    }) {
                        Text(option.optionText ?? "")
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(Color.gray.opacity(0.2))
                            .cornerRadius(8)
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}

private struct WordSelectionView: View {
    let fullTask: FullTask
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Выберите правильные слова:")
                .font(.caption)
                .foregroundColor(.secondary)

            LazyVGrid(columns: [GridItem(.adaptive(minimum: 80))], spacing: 8) {
                ForEach(fullTask.options, id: \.id) { option in
                    Button(action: {
                        component.selectOption(optionId: option.id)
                    }) {
                        Text(option.optionText ?? "")
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(state.wordSelection.contains(option.id) ? Color.blue.opacity(0.2) : Color.gray.opacity(0.1))
                            .cornerRadius(8)
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}

private struct SubmissionArea: View {
    let state: TasksState
    let component: TasksComponent

    var body: some View {
        if state.showResult {
            let correct = state.lastSubmissionCorrect?.boolValue == true
            let color = correct ? Color.green : Color.red
            let message = correct ? "Верно!" : "Неверно"

            HStack {
                Text(message)
                    .foregroundColor(color)
                Spacer()
                Button(action: {
                    component.nextAfterResult()
                }) {
                    Text(correct ? "Далее" : "Попробовать ещё")
                }
                .buttonStyle(.borderedProminent)
            }
            .padding(12)
            .background(color.opacity(0.1))
            .cornerRadius(8)
        } else {
            Button(action: {
                component.submitAnswer()
            }) {
                Text("Ответить")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .disabled(!state.canSubmit)
        }
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

private extension TrainComponentChild {
    func hashObject() -> Int { (self as AnyObject).hash }
}
