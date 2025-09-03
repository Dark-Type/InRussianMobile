//
//  TrainComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI
import AVFoundation

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

    // The hosting container injects a setter that we call onAppear to hook "Continue"
    private let setOnContinue: (@escaping () -> Void) -> Void

    init(component: AudioConnectTaskComponent, setOnContinue: @escaping (@escaping () -> Void) -> Void) {
        self.component = component
        _state = StateValue(component.state)
        self.setOnContinue = setOnContinue
    }

    var body: some View {
        ZStack {
            Color.clear.ignoresSafeArea()

            ScrollView {
                LazyVStack(spacing: 10) {
                    ForEach(Array(state.rows.enumerated()), id: \.element.key) { _, r in
                        if let pair = r as? RowModelPairRow {
                            PairRowView(
                                left: pair.left,
                                right: pair.right,
                                isRightHovered: state.hoveredRightId == pair.right.id,
                                isWrong: state.invalidLeftIds.contains(pair.left.id),
                                onLeftHandlePositioned: { rect in
                                    component.onPairLeftPositioned(leftId: pair.left.id, rect: rect)
                                },
                                onRightPositioned: { rect in
                                    component.onRightPositioned(rightId: pair.right.id, rect: rect)
                                },
                                onLeftDragStart: {
                                    component.startDrag(fromPair: true, leftId: pair.left.id)
                                },
                                onLeftDragBy: { delta in
                                    component.dragBy(p: CGPoint(x: delta.width, y: delta.height))
                                },
                                onLeftDragEnd: {
                                    component.endDrag()
                                },
                                onLeftDragCancel: {
                                    component.cancelDrag()
                                }
                            )
                            .padding(.horizontal, 12)
                        } else if let unmatched = r as? RowModelUnmatchedRow {
                            UnmatchedRowView(
                                left: unmatched.left,
                                right: unmatched.right,
                                isRightHovered: unmatched.right.map { state.hoveredRightId == $0.id } ?? false,
                                isWrong: unmatched.left.map { state.invalidLeftIds.contains($0.id) } ?? false,
                                onLeftPositioned: { leftId, rect in
                                    component.onLeftPositioned(leftId: leftId, rect: rect)
                                },
                                onRightPositioned: { rightId, rect in
                                    component.onRightPositioned(rightId: rightId, rect: rect)
                                },
                                onLeftDragStart: { leftId in
                                    component.startDrag(fromPair: false, leftId: leftId)
                                },
                                onLeftDragBy: { leftId, delta in
                                    component.dragBy(leftId: leftId, p: CGPoint(x: delta.width, y: delta.height))
                                },
                                onLeftDragEnd: {
                                    component.endDrag()
                                },
                                onLeftDragCancel: {
                                    component.cancelDrag()
                                }
                            )
                            .padding(.horizontal, 12)
                        }
                    }

                    Spacer().frame(height: 24)
                }
                .padding(.vertical, 12)
            }
            .background(ExtraColors.baseBackground)
        }
        .onAppear {
            setOnContinue {
                component.onContinueClick()
            }
        }
    }
}

// MARK: - PairRow

private struct PairRowView: View {
    let left: Piece
    let right: Piece
    let isRightHovered: Bool
    let isWrong: Bool

    let onLeftHandlePositioned: (CGRect) -> Void
    let onRightPositioned: (CGRect) -> Void

    let onLeftDragStart: () -> Void
    let onLeftDragBy: (CGSize) -> Void
    let onLeftDragEnd: () -> Void
    let onLeftDragCancel: () -> Void

    @State private var lastDragLocation: CGPoint?

    var body: some View {
        let shape = RoundedRectangle(cornerRadius: 12)

        HStack(alignment: .center) {
            // Left side (audio button with drag handle)
            PuzzleIn(
                content: {
                    AudioButtonAV(urlString: left.label)
                },
                background: .clear
            )
            .frame(maxHeight: .infinity)
            .contentShape(Rectangle())
            .modifier(GlobalFrameReader { rect in
                onLeftHandlePositioned(rect)
            })
            .gesture(
                DragGesture(minimumDistance: 0)
                    .onChanged { value in
                        if lastDragLocation == nil {
                            onLeftDragStart()
                            lastDragLocation = value.location
                        } else if let prev = lastDragLocation {
                            let dx = value.location.x - prev.x
                            let dy = value.location.y - prev.y
                            onLeftDragBy(CGSize(width: dx, height: dy))
                            lastDragLocation = value.location
                        }
                    }
                    .onEnded { _ in
                        lastDragLocation = nil
                        onLeftDragEnd()
                    }
                    .onEnded { _ in
                        // already ended
                    }
            )
            .simultaneousGesture(DragGesture(minimumDistance: 0).onEnded { _ in })
            .padding(.trailing, 6)
            .padding(.vertical, 8)
            .padding(.horizontal, 10)

            // Right side (text tile, hover/wrong styles)
            PuzzleOut(
                content: { _ in
                    Text(right.label)
                        .frame(maxHeight: .infinity, alignment: .center)
                        .frame(maxWidth: .infinity, alignment: .leading)
                },
                background: isWrong ? Colors.matchedBg.opacity(0.1) : Colors.matchedBg,
                border: {
                    if isWrong {
                        Colors.red
                    } else if isRightHovered {
                        Colors.accent
                    } else {
                        Colors.primary
                    }
                }()
            )
            .modifier(GlobalFrameReader { rect in
                onRightPositioned(rect)
            })
            .padding(.leading, 6)
            .padding(.vertical, 8)
            .padding(.horizontal, 10)
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .padding(.horizontal, 0)
        .background(
            isWrong ? Colors.red.opacity(0.1) : Colors.matchedBg,
            in: shape
        )
        .overlay(
            shape.stroke(
                isWrong ? Colors.red :
                    (isRightHovered ? Colors.accent : Colors.primary),
                lineWidth: 1
            )
        )
        .contentShape(Rectangle())
    }
}

// MARK: - UnmatchedRow

private struct UnmatchedRowView: View {
    let left: Piece?
    let right: Piece?
    let isRightHovered: Bool
    let isWrong: Bool

    let onLeftPositioned: (String, CGRect) -> Void
    let onRightPositioned: (String, CGRect) -> Void

    let onLeftDragStart: (String) -> Void
    let onLeftDragBy: (String, CGSize) -> Void
    let onLeftDragEnd: () -> Void
    let onLeftDragCancel: () -> Void

    @State private var lastDragLocation: CGPoint?

    var body: some View {
        let shape = RoundedRectangle(cornerRadius: 12)

        HStack(alignment: .center) {
            // LEFT
            Group {
                if let left = left {
                    PuzzleIn(
                        content: {
                            AudioButtonAV(urlString: left.label)
                        },
                        background: Colors.surface
                    )
                    .modifier(GlobalFrameReader { rect in
                        onLeftPositioned(left.id, rect)
                    })
                    .gesture(
                        DragGesture(minimumDistance: 0)
                            .onChanged { value in
                                if lastDragLocation == nil {
                                    onLeftDragStart(left.id)
                                    lastDragLocation = value.location
                                } else if let prev = lastDragLocation {
                                    let dx = value.location.x - prev.x
                                    let dy = value.location.y - prev.y
                                    onLeftDragBy(left.id, CGSize(width: dx, height: dy))
                                    lastDragLocation = value.location
                                }
                            }
                            .onEnded { _ in
                                lastDragLocation = nil
                                onLeftDragEnd()
                            }
                    )
                } else {
                    PuzzleIn(
                        content: { TextSmall(text: "—") },
                        background: Colors.emptyBg
                    )
                }
            }
            .padding(.trailing, 6)
            .padding(.vertical, 8)
            .padding(.horizontal, 10)

            // RIGHT
            let rightBorder: Color = {
                guard let right = right else { return Colors.emptyBorder }
                if isWrong { return Colors.red }
                if isRightHovered { return Colors.accent }
                return Colors.outline
            }()

            let rightBg: Color = {
                guard let _ = right else { return Colors.emptyBg }
                if isWrong { return Colors.red }
                if isRightHovered { return Colors.hoverBg }
                return Colors.surface
            }()

            Group {
                if let right = right {
                    PuzzleOut(
                        content: { _ in
                            Text(right.label)
                                .frame(maxHeight: .infinity, alignment: .leading)
                        },
                        background: rightBg,
                        border: rightBorder
                    )
                    .modifier(GlobalFrameReader { rect in
                        onRightPositioned(right.id, rect)
                    })
                } else {
                    PuzzleOut(
                        content: { _ in TextSmall(text: "—") },
                        background: rightBg,
                        border: rightBorder
                    )
                }
            }
            .padding(.leading, 6)
            .padding(.vertical, 8)
            .padding(.horizontal, 10)
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .background(Colors.rowBg, in: shape)
        .overlay(shape.stroke(Colors.rowBorder, lineWidth: 1))
    }
}

// MARK: - Puzzle building blocks (simple SwiftUI equivalents)

private struct PuzzleIn<Content: View>: View {
    let content: () -> Content
    var background: Color = AppColors.Palette.baseBackground.color

    var body: some View {
        content()
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 10)
            .padding(.vertical, 8)
            .background(background, in: RoundedRectangle(cornerRadius: 10))
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(Colors.outline, lineWidth: 1)
            )
    }
}

private struct PuzzleOut<Content: View>: View {
    let content: (GeometryProxy) -> Content
    var background: Color = AppColors.Palette.baseBackground.color
    var border: Color = AppColors.Palette.stroke.color

    var body: some View {
        GeometryReader { geo in
            content(geo)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 10)
                .padding(.vertical, 8)
                .background(background, in: RoundedRectangle(cornerRadius: 10))
                .overlay(
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(border, lineWidth: 1)
                )
        }
        .frame(height: 98)
    }
}

// MARK: - Audio Button (AVPlayer-based, play/pause)

private struct AudioButtonAV: View {
    let urlString: String?
    var onError: ((Error) -> Void)? = nil

    @State private var isPlaying: Bool = false
    @State private var player: AVPlayer? = nil
    @State private var token: Any?

    var body: some View {
        Button(action: togglePlay) {
            Image(systemName: isPlaying ? "pause.fill" : "play.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 28, height: 28)
                .foregroundColor(Colors.darkGrey.opacity(0.8))
                .padding(11)
                .frame(width: 50, height: 50)
        }
        .onChange(of: urlString) {
            preparePlayer()
        }
        .onAppear {
            preparePlayer()
        }
        .onDisappear {
            cleanup()
        }
    }

    private func preparePlayer() {
        cleanup()

        guard let urlString, !urlString.isEmpty, let url = URL(string: urlString) else {
            isPlaying = false
            return
        }
        let player = AVPlayer(url: url)
        self.player = player

        // Observe playback status
        token = NotificationCenter.default.addObserver(
            forName: .AVPlayerItemDidPlayToEndTime,
            object: player.currentItem,
            queue: .main
        ) { _ in
            self.isPlaying = false
        }
    }

    private func togglePlay() {
        guard let player else { return }
        if isPlaying {
            player.pause()
            isPlaying = false
        } else {
            do {
                try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default, options: [])
                try AVAudioSession.sharedInstance().setActive(true)
            } catch {
                onError?(error)
            }
            player.play()
            isPlaying = true
        }
    }

    private func cleanup() {
        if let token { NotificationCenter.default.removeObserver(token) }
        token = nil
        player?.pause()
        player = nil
        isPlaying = false
    }
}

// MARK: - Helpers

// Read the global frame of a view to pass to the component
private struct GlobalFramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
        value = nextValue()
    }
}

private struct GlobalFrameReader: ViewModifier {
    let onChange: (CGRect) -> Void
    func body(content: Content) -> some View {
        content
            .background(
                GeometryReader { geo in
                    Color.clear
                        .preference(key: GlobalFramePreferenceKey.self, value: geo.frame(in: .global))
                }
            )
            .onPreferenceChange(GlobalFramePreferenceKey.self, perform: onChange)
    }
}

private struct TextSmall: View {
    let text: String
    var color: Color = Colors.textSecondary
    var body: some View {
        Text(text)
            .font(.system(size: 13))
            .foregroundColor(color)
    }
}

// MARK: - Colors (approximate matches to Compose side)

private enum Colors {
    static let rowBg = Color(white: 0.96)
    static let rowBorder = Color(white: 0.85)
    static let matchedBg = Color(white: 0.97)
    static let surface = Color(white: 1.0)
    static let outline = Color(white: 0.85)
    static let emptyBg = Color(white: 0.94)
    static let emptyBorder = Color(white: 0.85)
    static let accent = Color.orange
    static let hoverBg = Color.orange.opacity(0.15)
    static let primary = Color.blue.opacity(0.5)
    static let textSecondary = Color.gray
    static let red = Color.red
    static let white = Color.white
    static let darkGrey = Color(UIColor.darkGray)
}

// Supply a background similar to LocalExtraColors.baseBackground
private enum ExtraColors {
    static let baseBackground = Color(white: 0.98)
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
        ZStack {
            AppColors.baseBackground.color.ignoresSafeArea()

            ScrollView {
                LazyVStack(spacing: 10) {
                    ForEach(Array(state.rows.enumerated()), id: \.element.key) { _, r in
                        if let pair = r as? PairRow {
                            PairRowView(
                                left: pair.left,
                                right: pair.right,
                                isRightHovered: state.hoveredRightId == pair.right.id,
                                isWrong: state.invalidLeftIds.contains(pair.left.id),
                                onLeftHandlePositioned: { rect in
                                    component.onPairLeftPositioned(leftId: pair.left.id, rect: rect.toRectF())
                                },
                                onRightPositioned: { rect in
                                    component.onRightPositioned(rightId: pair.right.id, rect: rect.toRectF())
                                },
                                onLeftDragStart: {
                                    component.startDrag(fromPair: true, leftId: pair.left.id)
                                },
                                onLeftDragBy: { delta in
                                    component.dragBy(p: CGPoint(x: delta.width, y: delta.height).toPointF())
                                },
                                onLeftDragEnd: {
                                    component.endDrag()
                                },
                                onLeftDragCancel: {
                                    component.cancelDrag()
                                }
                            )
                            .padding(.horizontal, 12)
                        } else if let unmatched = r as? RowModelUnmatchedRow {
                            UnmatchedRowView(
                                left: unmatched.left,
                                right: unmatched.right,
                                isRightHovered: unmatched.right.map { state.hoveredRightId == $0.id } ?? false,
                                isWrong: unmatched.left.map { state.invalidLeftIds.contains($0.id) } ?? false,
                                onLeftPositioned: { leftId, rect in
                                    component.onLeftPositioned(leftId: leftId, rect: rect.toRectF())
                                },
                                onRightPositioned: { rightId, rect in
                                    component.onRightPositioned(rightId: rightId, rect: rect.toRectF())
                                },
                                onLeftDragStart: { leftId in
                                    component.startDrag(fromPair: false, leftId: leftId)
                                },
                                onLeftDragBy: { leftId, delta in
                                    component.dragBy(leftId: leftId, p: CGPoint(x: delta.width, y: delta.height).toPointF())
                                },
                                onLeftDragEnd: {
                                    component.endDrag()
                                },
                                onLeftDragCancel: {
                                    component.cancelDrag()
                                }
                            )
                            .padding(.horizontal, 12)
                        }
                    }
                    Spacer().frame(height: 24)
                }
                .padding(.vertical, 12)
            }
        }
        .onAppear {
            setOnContinue {
                component.onContinueClick()
            }
        }
    }
}

// MARK: - PairRow (text on left, text on right – mirrors Compose)



// MARK: - Building blocks

private struct PuzzleTile<Content: View>: View {
    let background: Color
    let border: Color
    let content: () -> Content

    init(background: Color, border: Color, @ViewBuilder content: @escaping () -> Content) {
        self.background = background
        self.border = border
        self.content = content
    }

    var body: some View {
        content()
            .padding(.horizontal, 10)
            .padding(.vertical, 8)
            .background(background, in: RoundedRectangle(cornerRadius: 10))
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(border, lineWidth: 1)
            )
    }
}

// These are light-weight stand-ins for your Compose "PuzzleLayoutIn/Out" containers.
// You can replace them with more complex shapes if you already have equivalents on iOS.

private enum TabSide { case right }

private struct PuzzleLayoutIn<Content: View>: View {
    let tabSide: TabSide
    let onClick: () -> Void
    let modifier: AnyViewModifier
    let content: () -> Content

    init(tabSide: TabSide, onClick: @escaping () -> Void, modifier: AnyViewModifier = AnyViewModifier(AnyView(EmptyView())), @ViewBuilder content: @escaping () -> Content) {
        self.tabSide = tabSide
        self.onClick = onClick
        self.modifier = modifier
        self.content = content
    }

    var body: some View {
        content()
            .padding(.horizontal, 10)
            .padding(.vertical, 8)
            .background(Colors.white.opacity(0.0))
            .modifier(modifier)
    }
}

private struct PuzzleLayoutOut<Content: View>: View {
    let onClick: () -> Void
    let modifier: AnyViewModifier
    let content: () -> Content

    init(onClick: @escaping () -> Void, modifier: AnyViewModifier = AnyViewModifier(AnyView(EmptyView())), @ViewBuilder content: @escaping () -> Content) {
        self.onClick = onClick
        self.modifier = modifier
        self.content = content
    }

    var body: some View {
        content()
            .padding(.horizontal, 10)
            .padding(.vertical, 8)
            .background(Colors.matchedBg, in: RoundedRectangle(cornerRadius: 10))
            .modifier(modifier)
    }
}

private struct AnyViewModifier: ViewModifier {
    let view: AnyView
    init(_ view: AnyView) { self.view = view }
    func body(content: Content) -> some View { content.background(view) }
}

// Remote image with AsyncImage, matching Compose AsyncImage
private struct RemoteImage: View {
    let urlString: String?

    var body: some View {
        if let s = urlString, let url = URL(string: s) {
            if #available(iOS 15.0, *) {
                SwiftUI.AsyncImage(url: url) { phase in
                    switch phase {
                    case .empty:
                        Rectangle()
                            .foregroundColor(.gray.opacity(0.3))
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFit()
                    case .failure:
                        Rectangle()
                            .foregroundColor(.gray.opacity(0.2))
                    @unknown default:
                        Rectangle()
                            .foregroundColor(.gray.opacity(0.2))
                    }
                }
            } else {
                // Fallback simple placeholder on iOS < 15
                Rectangle().foregroundColor(.gray.opacity(0.2))
            }
        } else {
            Rectangle().foregroundColor(.gray.opacity(0.2))
        }
    }
}

// MARK: - Geometry helpers


// MARK: - Text



// Hex initializer for Color
private extension Color {
    init(_ hex: UInt32, opacity: Double = 1.0) {
        let r = Double((hex >> 16) & 0xFF) / 255.0
        let g = Double((hex >> 8) & 0xFF) / 255.0
        let b = Double(hex & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: opacity)
    }
}

// MARK: - RectF / PointF adapters (KMM types)

private extension CGRect {
    func toRectF() -> RectF {
        RectF(
            left: Float(minX),
            top: Float(minY),
            right: Float(maxX),
            bottom: Float(maxY)
        )
    }
}

private extension CGPoint {
    func toPointF() -> PointF {
        PointF(x: Float(x), y: Float(y))
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
