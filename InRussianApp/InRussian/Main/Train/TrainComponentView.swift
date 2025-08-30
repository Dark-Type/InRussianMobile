//
//  TrainComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

// Root host that mirrors the Android TrainComponentUi
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

// MARK: - Courses (mirrors TrainCoursesScreen)

private struct TrainCoursesListView: View {
    let component: TrainCoursesListComponent
    @StateValue private var state: TrainCoursesState

    // Predefine grid to reduce type inference work
    private let gridColumns: [GridItem] = [
        GridItem(.flexible(), spacing: 20),
        GridItem(.flexible(), spacing: 20),
        GridItem(.flexible(), spacing: 20)
    ]

    init(component: TrainCoursesListComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        ZStack {
            // Fill the whole screen with secondary background
            Color(uiColor: .secondarySystemBackground)
                .ignoresSafeArea()

            if state.isLoading {
                ProgressCentered()
            } else {
                ScrollView {
                    VStack(alignment: .leading, spacing: 28) {
                        // Courses
                        VStack(spacing: 20) {
                            ForEach(state.courses, id: \.course.id) { bundle in
                                let coursePercent = Int(bundle.course.percent)
                                let accent: Color = colorFor(percent: coursePercent)

                                VStack(alignment: .leading, spacing: 16) {
                                    HStack(spacing: 12) {
                                        Text(bundle.course.title)
                                            .font(.title3).bold()
                                            .foregroundStyle(accent)
                                            .lineLimit(2)
                                            .multilineTextAlignment(.leading)
                                        Spacer()
                                        Text("\(coursePercent)%")
                                            .font(.headline).bold()
                                            .foregroundColor(AppColors.Palette.stroke.color)
                                    }

                                    LazyVGrid(columns: gridColumns, spacing: 20) {
                                        ForEach(bundle.sections, id: \.id) { section in
                                            SectionItemView(
                                                title: section.title,
                                                progress: Int(section.progressPercent),
                                                ringColor: colorFor(percent: Int(section.progressPercent))
                                            ) {
                                                component.onSectionClick(sectionId: section.id)
                                            }
                                        }
                                    }
                                }
                                .padding(16) // comfortable inner padding
                                .background(
                                    // Use component background without borders
                                    RoundedRectangle(cornerRadius: 12)
                                        .fill(AppColors.Palette.componentBackground.color)
                                )
                            }
                        }
                    }
                    .padding(.horizontal, 20)
                    .padding(.bottom, 28)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        // Make "Главная" the navigation title
        .navigationTitle("Главная")
        .navigationBarTitleDisplayMode(.large)
    }

    private func colorFor(percent: Int) -> Color {
        if percent < 30 { return .red }
        if percent < 85 { return .yellow }
        return .green
    }
}

private struct SectionItemView: View {
    let title: String
    let progress: Int
    let ringColor: Color
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: 12) {
                ProgressRing(
                    percent: progress,
                    color: ringColor,
                    size: 60,
                    lineWidth: 6
                )
                if !title.isEmpty {
                    Text(title)
                        .font(.caption)
                        .multilineTextAlignment(.center)
                        .foregroundColor(.primary)
                        .lineLimit(2)
                        .frame(maxWidth: .infinity)
                }
            }
            .padding(14)
            .frame(maxWidth: .infinity)
            .background(
                // Use component background and remove borders
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColors.Palette.componentBackground.color)
            )
        }
        .buttonStyle(.plain)
    }
}

private struct ProgressRing: View {
    let percent: Int
    let color: Color
    let size: CGFloat
    let lineWidth: CGFloat

    var body: some View {
        ZStack {
            Circle()
                .stroke(color.opacity(0.4), style: StrokeStyle(lineWidth: lineWidth))
            Circle()
                .trim(from: 0, to: CGFloat(max(0, min(1, Double(percent) / 100.0))))
                .stroke(color, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
                .rotationEffect(.degrees(-90))
            Text("\(percent)%")
                .font(.caption)
                .bold()
                .foregroundColor(color)
        }
        .frame(width: size, height: size)
    }
}

// MARK: - Section detail host (mirrors SectionDetailHost)

private struct SectionDetailHostView: View {
    let component: SectionDetailComponent
    @StateValue private var state: SectionDetailState
    @StateValue private var innerStack: ChildStack<any DefaultSectionDetailComponentInnerConfig, any DefaultSectionDetailComponentInnerChild>

    init(component: SectionDetailComponent) {
        self.component = component
        _state = StateValue(component.state)
        if let defaultComponent = component as? DefaultSectionDetailComponent {
            _innerStack = StateValue(defaultComponent.childStack)
        } else {
            fatalError("Unsupported SectionDetailComponent type")
        }
    }

    var body: some View {
        ZStack {
            if let _ = component as? DefaultSectionDetailComponent {
                let inner = innerStack.active.instance
                if inner is DefaultSectionDetailComponentInnerChildDetailsChild {
                    SectionDetailsView(component: component, state: state)
                } else if let tasksChild = inner as? DefaultSectionDetailComponentInnerChildTasksChild {
                    TrainTasksView(component: tasksChild.component)
                } else {
                    Text("Unsupported child").foregroundStyle(.secondary)
                }
            } else {
                Text("Unsupported SectionDetailComponent type")
            }
        }
        .overlay {
            if state.showCompletionDialog {
                CompletionDialog { component.onBack() }
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { component.onBack() }) {
                    HStack(spacing: 4) {
                        Image(systemName: "chevron.left")
                        Text("Назад")
                    }
                }
            }
        }
    }
}

// MARK: - Section details screen (mirrors SectionDetailsScreen)

private struct SectionDetailsView: View {
    let component: SectionDetailComponent
    let state: SectionDetailState

    var body: some View {
        if state.isLoading || state.section == nil {
            ProgressCentered()
        } else if let section = state.section {
            ZStack {
                // Full-screen background
                Color(uiColor: .secondarySystemBackground)
                    .ignoresSafeArea()

                // Content
                GeometryReader { _ in
                    ScrollView {
                        VStack(alignment: .leading, spacing: 28) {
                            // Stats card
                            HStack(spacing: 16) {
                                VStack(alignment: .leading, spacing: 8) {
                                    Text("Освоено: \(section.completedTasks)")
                                        .foregroundColor(.secondary)
                                    Text("К освоению: \(section.completedTasks)")
                                        .foregroundColor(.secondary)
                                    Text("Не пройдено: \(section.completedTasks)")
                                        .foregroundColor(.secondary)
                                }
                                Spacer()
                                let p = Int(section.progressPercent)
                                ProgressRing(
                                    percent: p,
                                    color: percentColor(p),
                                    size: 110,
                                    lineWidth: 6
                                )
                            }
                            .padding(18)
                            .background(
                                RoundedRectangle(cornerRadius: 14)
                                    .fill(AppColors.Palette.componentBackground.color)
                            )

                            Spacer()
                            HStack(spacing: 16) {
                                Button {
                                    component.openTasks(option: .theory)
                                } label: {
                                    Text("Теория")
                                        .font(.title3.bold())
                                        .foregroundColor(.white)
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 250) // double/triple height
                                        .background(
                                            RoundedRectangle(cornerRadius: 14)
                                                .fill(AppColors.Palette.secondary.color)
                                        )
                                }
                                .buttonStyle(.plain)

                                Button {
                                    component.openTasks(option: .practice)
                                } label: {
                                    Text("Практика")
                                        .font(.title3.bold())
                                        .foregroundColor(.white)
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 250) // double/triple height
                                        .background(
                                            RoundedRectangle(cornerRadius: 14)
                                                .fill(AppColors.Palette.secondary.color)
                                        )
                                }
                                .buttonStyle(.plain)
                            }
                            Spacer(minLength: 120)
                            CustomButton(text: "Продолжить обучение", color: .accent) {
                                component.openTasks(option: .continue_)
                            }
                            .padding(.top, 8)
                            .padding(.bottom, 24)
                        }
                        .padding(.horizontal, 20)
                        .padding(.top, 12)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }
            }
            // Navigation title at top
            .navigationTitle(section.title)
            .navigationBarTitleDisplayMode(.large)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItemGroup(placement: .topBarTrailing) {
                    Button(action: { component.onBack() }) {
                        ZStack {
                            Circle()
                                .fill(Color.gray.opacity(0.6))
                                .frame(width: 44, height: 44)
                            Image(systemName: "chevron.left")
                                .foregroundColor(.white)
                                .font(.system(size: 14, weight: .semibold))
                        }
                    }
                }
            }
        }
    }

    private func percentColor(_ percent: Int) -> Color {
        if percent < 35 { return .orange }
        if percent < 85 { return .yellow }
        return Color(red: 0.25, green: 0.70, blue: 0.40)
    }
}

// MARK: - Train tasks UI (mirrors TaskUi with TrainComponentCopy)

private struct TrainTasksView: View {
    let component: TrainComponentCopy
    @StateValue private var slot: ChildSlot<AnyObject, any TrainComponentCopyChild>
    @StateValue private var state: TrainStoreState

    @State private var pendingAction: (() -> Void)?

    init(component: TrainComponentCopy) {
        self.component = component
        _slot = StateValue(component.childSlot as! Value<ChildSlot<AnyObject, any TrainComponentCopyChild>>)
        _state = StateValue(component.state)
    }

    var body: some View {
        VStack(spacing: 16) {
            ProgressView(value: Double(truncating: state.percent ?? 0), total: 1)
                .tint(.orange)
                .frame(maxWidth: .infinity)

            TaskDescriptionView(
                text: state.showedTask?.taskText ?? "",
                onInfo: {} // later
            )

            // Task area
            ZStack {
                let current = slot.child?.instance
                if current == nil {
                    Text("тут совсем пусто")
                        .font(.title)
                        .foregroundColor(.primary)
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                        .padding(.vertical, 40)
                } else if current is TrainComponentCopyChildEmptyChild {
                    EmptyTaskPlaceholderView()
                } else if let image = current as? TrainComponentCopyChildImageConnectChild {
                    ImageConnectTaskView(component: image.component) { action in
                        pendingAction = action
                    }
                } else if let textConnect = current as? TrainComponentCopyChildTextConnectChild {
                    TextConnectTaskView(component: textConnect.component) { action in
                        pendingAction = action
                    }
                } else if let audio = current as? TrainComponentCopyChildAudioConnectChild {
                    AudioConnectTaskView(component: audio.component) { action in
                        pendingAction = action
                    }
                } else {
                    Text("Unsupported task")
                        .foregroundStyle(.secondary)
                }
            }
            .frame(maxWidth: .infinity, minHeight: 220, alignment: .topLeading)

            Button(action: { pendingAction?() }) {
                Text(state.isChecking ? "Проверить" : "Продолжить")
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
            }
            .buttonStyle(.borderedProminent)
            .tint(.orange)
            .disabled(!(state.isButtonEnable))

            Spacer().frame(height: 8)
        }
        .padding(.horizontal, 28)
        .padding(.bottom, 102)
        .background(Color(UIColor.secondarySystemBackground))
    }
}

// MARK: - Actual task implementations

// AudioConnect (mirrors @audio_connect.kt)
private struct AudioConnectTaskView: View {
    let component: AudioConnectTaskComponent
    @StateValue private var state: AudioConnectTaskComponentState

    private let setOnContinue: (@escaping () -> Void) -> Void

    init(component: AudioConnectTaskComponent, setOnContinue: @escaping (@escaping () -> Void) -> Void) {
        self.component = component
        _state = StateValue(component.state)
        self.setOnContinue = setOnContinue
    }

    var body: some View {
        let columns = [
            GridItem(.flexible(), spacing: 30),
            GridItem(.flexible(), spacing: 30)
        ]
        ZStack {
            Color.clear
                .frame(maxWidth: .infinity, maxHeight: .infinity)

            LazyVGrid(columns: columns, spacing: 32) {
                let total = state.elements.count
                ForEach(0..<total, id: \.self) { index in
                    let element = state.elements[index]

                    if index % 2 == 0, let audio = element as? AudioTask {
                        let base = element
                        PuzzleTile(background: colorFor(taskState: audio.state)) {
                            HStack {
                                Spacer()
                                Image(systemName: audio.isPlay ? "play.fill" : "pause.fill")
                                    .foregroundColor(Color(UIColor.darkGray))
                                Spacer()
                            }
                        }
                        .onTapGesture {
                            component.onTaskClick(taskId: base.id)
                        }
                    } else if let text = element as? TextTaskModel {
                        let base = element
                        PuzzleTile(background: colorFor(taskState: text.state)) {
                            Text(text.text)
                                .foregroundColor(textColorFor(taskState: text.state))
                                .multilineTextAlignment(.center)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 8)
                        }
                        .onTapGesture {
                            component.onTaskClick(taskId: base.id)
                        }
                    } else {
                        Color.clear.frame(height: 44)
                    }
                }
            }
            .padding(.vertical, 4)
        }
        .onAppear {
            setOnContinue {
                component.onContinueClick()
            }
        }
    }

    private func colorFor(taskState: TaskState?) -> Color {
        switch taskState {
        case is TaskStateCorrect: return .green
        case is TaskStateIncorrect: return .red
        case is TaskStateSelected: return .orange
        case is TaskStateConnect: return Color.orange.opacity(0.5)
        default: return .white
        }
    }

    private func textColorFor(taskState: TaskState?) -> Color {
        switch taskState {
        case is TaskStateCorrect, is TaskStateIncorrect, is TaskStateSelected, is TaskStateConnect:
            return .white
        default:
            return .black
        }
    }
}

// ImageConnect (mirrors @image_connect_task.kt)
private struct ImageConnectTaskView: View {
    let component: ImageConnectTaskComponent
    @StateValue private var state: ImageConnectTaskComponentState

    private let setOnContinue: (@escaping () -> Void) -> Void

    init(component: ImageConnectTaskComponent, setOnContinue: @escaping (@escaping () -> Void) -> Void) {
        self.component = component
        _state = StateValue(component.state)
        self.setOnContinue = setOnContinue
    }

    var body: some View {
        let columns = [GridItem(.flexible()), GridItem(.flexible())]
        LazyVGrid(columns: columns, spacing: 24) {
            let pairsCount = state.elements.count
            ForEach(0..<pairsCount, id: \.self) { idx in
                let pair = state.elements[idx] as! KotlinPair<AnyObject, AnyObject>
                let first = pair.first
                let second = pair.second

                if let left = first as? TextTaskModel, let baseLeft = first as? Task_ {
                    PuzzleTile(background: colorFor(taskState: left.state)) {
                        Text(left.text)
                            .foregroundColor(.primary)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.vertical, 8)
                    }
                    .onTapGesture {
                        component.onTaskClick(taskId: baseLeft.id)
                    }
                } else {
                    Color.clear.frame(height: 44)
                }

                if let right = second as? ImageConnectTaskModel, let baseRight = second as? Task_ {
                    PuzzleTile(background: colorFor(taskState: right.state)) {
                        let urlString = right.imageUrl
                        if let url = URL(string: urlString) {
                            AsyncImage(url: url) { image in
                                image
                                    .resizable()
                                    .scaledToFit()
                            } placeholder: {
                                Rectangle()
                                    .foregroundColor(.gray.opacity(0.3))
                                    .frame(height: 45)
                            }
                            .frame(height: 45)
                        } else {
                            Rectangle()
                                .foregroundColor(.gray.opacity(0.2))
                                .frame(height: 45)
                        }
                    }
                    .onTapGesture {
                        component.onTaskClick(taskId: baseRight.id)
                    }
                } else {
                    Color.clear.frame(height: 44)
                }
            }
        }
        .onAppear {
            setOnContinue {
                component.onContinueClick()
            }
        }
    }

    private func colorFor(taskState: TaskState?) -> Color {
        switch taskState {
        case is TaskStateCorrect: return .green
        case is TaskStateIncorrect: return .red
        case is TaskStateSelected: return .orange
        case is TaskStateConnect: return Color.orange.opacity(0.5)
        default: return .white
        }
    }
}

// TextConnect (mirrors @text_connect_screen.kt)
private struct TextConnectTaskView: View {
    let component: TextConnectTaskComponent
    @StateValue private var state: TextConnectTaskComponentState

    private let setOnContinue: (@escaping () -> Void) -> Void

    init(component: TextConnectTaskComponent, setOnContinue: @escaping (@escaping () -> Void) -> Void) {
        self.component = component
        _state = StateValue(component.state)
        self.setOnContinue = setOnContinue
    }

    var body: some View {
        let columns = [GridItem(.flexible()), GridItem(.flexible())]
        LazyVGrid(columns: columns, spacing: 24) {
            let pairsCount = state.elements.count
            ForEach(0..<pairsCount, id: \.self) { idx in
                let pair = state.elements[idx] as! KotlinPair<AnyObject, AnyObject>

                // LEFT (Text)
                if let left = pair.first as? TextTaskModel, let baseLeft = pair.first as? Task_ {
                    PuzzleTile(background: colorFor(taskState: left.state)) {
                        Text(left.text)
                            .foregroundColor(.primary)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.vertical, 8)
                    }
                    .onTapGesture {
                        component.onTaskClick(taskId: baseLeft.id)
                    }
                } else {
                    Color.clear.frame(height: 44)
                }

                // RIGHT (Text)
                if let right = pair.second as? TextTaskModel, let baseRight = pair.second as? Task_ {
                    PuzzleTile(background: colorFor(taskState: right.state)) {
                        Text(right.text)
                            .foregroundColor(.primary)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.vertical, 8)
                    }
                    .onTapGesture {
                        component.onTaskClick(taskId: baseRight.id)
                    }
                } else {
                    Color.clear.frame(height: 44)
                }
            }
        }
        .onAppear {
            setOnContinue {
                component.onContinueClick()
            }
        }
    }

    private func colorFor(taskState: TaskState?) -> Color {
        switch taskState {
        case is TaskStateCorrect: return .green
        case is TaskStateIncorrect: return .red
        case is TaskStateSelected: return .orange
        case is TaskStateConnect: return Color.orange.opacity(0.5)
        default: return .white
        }
    }
}

// TextInputTask (mirrors @text_input_task.kt)
// This is a generic view; wire it with your component when integrating the text-input child.
private struct TextInputTaskView: View {
    let elements: [TextInsertTask]
    let onGapChange: (Gap) -> Void

    init(elements: [TextInsertTask], onGapChange: @escaping (Gap) -> Void) {
        self.elements = elements
        self.onGapChange = onGapChange
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                ForEach(Array(elements.enumerated()), id: \.offset) { _, element in
                    Text(element.label)
                        .font(.title3).bold()

                    VStack(alignment: .leading, spacing: 12) {
                        ForEach(Array(element.sentence.enumerated()), id: \.offset) { _, sentence in
                            GapSentenceView(sentence: sentence, onGapChange: onGapChange)
                        }
                    }
                    .padding(.horizontal, 8)
                }
            }
            .padding(16)
            .background(Color.white)
        }
    }
}

// MARK: - Helpers used by task UIs

private struct PuzzleTile<Content: View>: View {
    let background: Color
    let content: Content

    init(background: Color, @ViewBuilder content: () -> Content) {
        self.background = background
        self.content = content()
    }

    var body: some View {
        content
            .frame(maxWidth: .infinity)
            .padding(10)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(background)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(borderColor, lineWidth: 1)
                    )
            )
    }

    private var borderColor: Color {
        if background == .white { return Color(UIColor.darkGray).opacity(0.3) }
        return .clear
    }
}

// A simple wrapping layout to place text and inline TextFields for gaps
@available(iOS 16.0, *)
private struct WrapLayout: Layout {
    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let width = proposal.width ?? .infinity
        var x: CGFloat = 0
        var y: CGFloat = 0
        var rowHeight: CGFloat = 0

        for subview in subviews {
            let size = subview.sizeThatFits(.unspecified)
            if x + size.width > width {
                x = 0
                y += rowHeight + 4
                rowHeight = 0
            }
            rowHeight = max(rowHeight, size.height)
            x += size.width + 4
        }
        return CGSize(width: width, height: y + rowHeight)
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let width = bounds.width
        var x: CGFloat = 0
        var y: CGFloat = 0
        var rowHeight: CGFloat = 0

        for subview in subviews {
            let size = subview.sizeThatFits(.unspecified)
            if x + size.width > width {
                x = 0
                y += rowHeight + 4
                rowHeight = 0
            }
            subview.place(at: CGPoint(x: bounds.minX + x, y: bounds.minY + y),
                          proposal: ProposedViewSize(width: size.width, height: size.height))
            rowHeight = max(rowHeight, size.height)
            x += size.width + 4
        }
    }
}

private struct GapSentenceView: View {
    let sentence: Sentence
    let onGapChange: (Gap) -> Void

    var body: some View {
        if #available(iOS 16.0, *) {
            WrapLayout {
                ForEach(segments(from: sentence), id: \.id) { seg in
                    switch seg.kind {
                    case .text(let t):
                        Text(t)
                            .font(.system(size: 15))
                            .foregroundColor(Color(UIColor.darkGray).opacity(0.7))
                    case .gap(let gap):
                        GapField(gap: gap, onGapChange: onGapChange)
                    }
                }
            }
        } else {
            VStack(alignment: .leading, spacing: 8) {
                Text(sentence.text)
                    .font(.system(size: 15))
                    .foregroundColor(Color(UIColor.darkGray).opacity(0.7))
                ForEach(Array((sentence.gaps).enumerated()), id: \.offset) { _, gap in
                    GapField(gap: gap, onGapChange: onGapChange)
                        .frame(maxWidth: 200)
                }
            }
        }
    }

    private struct Segment: Identifiable {
        enum Kind {
            case text(String)
            case gap(Gap)
        }

        let id = UUID()
        let kind: Kind
    }

    private func segments(from sentence: Sentence) -> [Segment] {
        var result: [Segment] = []
        var start = 0
        let text = sentence.text
        let gaps: [Gap] = sentence.gaps

        for i in 0..<gaps.count {
            let gap = gaps[i]
            let idx = Int(gap.index)
            if idx > start && idx <= text.count {
                let to = min(idx + 1, text.count)
                let startIndex = text.index(text.startIndex, offsetBy: start)
                let endIndex = text.index(text.startIndex, offsetBy: to)
                let part = String(text[startIndex..<endIndex])
                result.append(Segment(kind: .text(part)))
            }
            result.append(Segment(kind: .gap(gap)))
            start = idx
        }
        if start < text.count {
            let startIndex = text.index(text.startIndex, offsetBy: start)
            let part = String(text[startIndex...])
            result.append(Segment(kind: .text(part)))
        }
        return result
    }
}

private struct GapField: View {
    let gap: Gap
    let onGapChange: (Gap) -> Void

    @State private var text: String = ""

    var body: some View {
        let correct = gap.correctWord
        let width: CGFloat = max(44, CGFloat(correct.count) * 9)

        TextField("", text: Binding(
            get: { gap.enter.isEmpty ? text : gap.enter },
            set: { new in
                text = new
                // Use doCopy from KMM bridge (or create a new Gap with init)
                onGapChange(gap.doCopy(enter: new, correctWord: gap.correctWord, index: gap.index))
            }
        ))
        .textFieldStyle(.roundedBorder)
        .font(.system(size: 15, weight: .medium))
        .foregroundColor(Color(UIColor.darkGray).opacity(0.8))
        .frame(width: width, height: 28)
    }
}

// MARK: - Shared bits

private struct EmptyTaskPlaceholderView: View {
    var body: some View {
        VStack {
            Text("тут совсем пусто")
                .font(.title)
                .foregroundColor(.primary)
        }
        .frame(maxWidth: .infinity, minHeight: 180)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color(uiColor: .systemBackground))
        )
    }
}

private struct TaskDescriptionView: View {
    let text: String
    let onInfo: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Spacer()
                Button(action: onInfo) {
                    Image(systemName: "exclamationmark.circle")
                        .foregroundColor(.orange)
                        .font(.title3)
                }
            }
            Text(text)
                .font(.body)
        }
        .padding(.horizontal, 10)
        .padding(.bottom, 16)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color(uiColor: .systemBackground))
        )
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
