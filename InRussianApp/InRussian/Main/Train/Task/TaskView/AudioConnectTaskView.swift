//
//  AudioConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//


import SwiftUI
import AVFoundation
import Shared

struct AudioConnectTaskView: View {
    let component: AudioConnectTaskComponent
    let onSetOnEvent: ((() -> Void)?) -> Void

    @StateValue private var state: AudioConnectTaskComponentState

    // Drag overlay state (renders ABOVE everything)
    @State private var dragging: DragOverlay?
    @State private var leftFrames: [String: CGRect] = [:] // leftId -> frame in board space

    private let boardSpace = "audio-board"
    private let unmatchedGap: CGFloat = 16
    private let tileHeight: CGFloat = 120

    init(component: AudioConnectTaskComponent, onSetOnEvent: @escaping ((() -> Void)?) -> Void) {
        self.component = component
        self.onSetOnEvent = onSetOnEvent
        _state = StateValue(component.state)
    }

    var body: some View {
        ZStack(alignment: .topLeading) {
            ScrollView {
                LazyVStack(spacing: 10) {
                    // Use index for id to avoid duplicate IDs when sides are nil in multiple rows
                    ForEach(Array(rows.enumerated()), id: \.offset) { _, row in
                        rowView(row)
                    }
                    Spacer().frame(height: 24)
                }
                .padding(12)
            }
            .coordinateSpace(name: boardSpace)

            // Floating overlay for the dragged LEFT tile (always on top)
            if let d = dragging {
                PuzzleTile(side: .right, kind: .outward) {
                    AudioOverlayVisual()
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                        .padding(14)
                }
                .frame(width: d.startFrame.width, height: d.startFrame.height)
                .position(x: d.startFrame.midX + d.totalTranslation.width,
                          y: d.startFrame.midY + d.totalTranslation.height)
                .scaleEffect(1.02) // subtle lift
                .shadow(color: .black.opacity(0.12), radius: 10, x: 0, y: 6)
                .allowsHitTesting(false) // gestures remain on original tile
                .zIndex(10_000) // above everything
                .transition(.identity)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
        .onAppear { onSetOnEvent { component.onContinueClick() } }
        .onChange(of: state.hash()) { _ in
            onSetOnEvent { component.onContinueClick() }
        }
    }
}

// MARK: - Decomposition

private extension AudioConnectTaskView {
    // Precompute rows once per render pass
    var rows: [ViewRow] {
        buildViewRows(state)
    }

    @ViewBuilder
    func rowView(_ row: ViewRow) -> some View {
        switch row {
        case .pair(let left, let right, _, _):
            PairRowView(
                left: left,
                right: right,
                tileHeight: tileHeight,
                draggingLeftId: dragging?.leftId,
                onLeftFrameChange: { frame in
                    leftFrames[left.id] = frame
                    component.onPairLeftPositioned(
                        leftId: left.id,
                        rect: makeRectFGlobal(frame)
                    )
                },
                onRightFrameChange: { frame in
                    component.onRightPositioned(
                        rightId: right.id,
                        rect: makeRectFGlobal(frame)
                    )
                },
                onLeftDragStart: {
                    component.startDrag(fromPair: true, leftId: left.id)
                    startOverlay(for: left)
                },
                onLeftDragBy: { delta in
                    component.dragBy(delta: makePointF(delta))
                    if var d = dragging {
                        d.totalTranslation.width += delta.width
                        d.totalTranslation.height += delta.height
                        dragging = d
                    }
                },
                onLeftDragEnd: {
                    component.endDrag()
                    stopOverlay()
                }
            )

        case .unmatched(let leftOpt, let rightOpt, _, _):
            UnmatchedRowView(
                left: leftOpt,
                right: rightOpt,
                gap: unmatchedGap,
                tileHeight: tileHeight,
                draggingLeftId: dragging?.leftId,
                onLeftFrameChange: { leftId, frame in
                    leftFrames[leftId] = frame
                    component.onLeftPositioned(
                        leftId: leftId,
                        rect: makeRectFGlobal(frame)
                    )
                },
                onRightFrameChange: { rightId, frame in
                    component.onRightPositioned(
                        rightId: rightId,
                        rect: makeRectFGlobal(frame)
                    )
                },
                onLeftDragStart: { leftId in
                    component.startDrag(fromPair: false, leftId: leftId)
                    if let piece = state.leftPieces.first(where: { $0.id == leftId }) {
                        startOverlay(for: piece)
                    }
                },
                onLeftDragBy: { leftId, delta in
                    component.dragBy(leftId: leftId, delta: makePointF(delta))
                    if var d = dragging {
                        d.totalTranslation.width += delta.width
                        d.totalTranslation.height += delta.height
                        dragging = d
                    }
                },
                onLeftDragEnd: {
                    component.endDrag()
                    stopOverlay()
                }
            )
        }
    }

    // Overlay control
    func startOverlay(for left: Piece) {
        guard let frame = leftFrames[left.id] else { return }
        dragging = DragOverlay(leftId: left.id, startFrame: frame, totalTranslation: .zero)
    }

    func stopOverlay() {
        withAnimation(.spring(response: 0.25, dampingFraction: 0.85)) {
            dragging = nil
        }
    }
}

// MARK: - View Row model built from state

private enum ViewRow: Equatable {
    case pair(left: Piece, right: Piece, isWrong: Bool, isRightHovered: Bool)
    case unmatched(left: Piece?, right: Piece?, isWrong: Bool, isRightHovered: Bool)
}

private func buildViewRows(_ state: AudioConnectTaskComponentState) -> [ViewRow] {
    let rightById = Dictionary(uniqueKeysWithValues: state.rightPieces.map { ($0.id, $0) })

    var rows: [ViewRow] = []
    var usedLeft = Set<String>()
    var usedRight = Set<String>()

    // Pairs (ordered by leftPieces)
    for left in state.leftPieces {
        if let rightId = state.matches[left.id], let right = rightById[rightId] {
            let isWrong = state.invalidLeftIds.contains(left.id)
            let isHovered = (state.hoveredRightId == right.id)
            rows.append(.pair(left: left, right: right, isWrong: isWrong, isRightHovered: isHovered))
            usedLeft.insert(left.id)
            usedRight.insert(right.id)
        }
    }

    // Unmatched rows: interleave remaining left and right
    let remainingLeft = state.leftPieces.filter { !usedLeft.contains($0.id) }
    let remainingRight = state.rightPieces.filter { !usedRight.contains($0.id) }
    let N = max(remainingLeft.count, remainingRight.count)
    for i in 0..<N {
        let l = i < remainingLeft.count ? remainingLeft[i] : nil
        let r = i < remainingRight.count ? remainingRight[i] : nil
        let isWrong = l.map { state.invalidLeftIds.contains($0.id) } ?? false
        let isHovered = r.map { state.hoveredRightId == $0.id } ?? false
        rows.append(.unmatched(left: l, right: r, isWrong: isWrong, isRightHovered: isHovered))
    }

    return rows
}

// MARK: - Rows

private struct PairRowView: View {
    let left: Piece
    let right: Piece
    let tileHeight: CGFloat

    let draggingLeftId: String?

    let onLeftFrameChange: (CGRect) -> Void
    let onRightFrameChange: (CGRect) -> Void

    let onLeftDragStart: () -> Void
    let onLeftDragBy: (CGSize) -> Void
    let onLeftDragEnd: () -> Void

    @State private var lastTranslation: CGSize = .zero

    var body: some View {
        HStack(spacing: 0) {
            // LEFT tile (audio) — gesture source; hidden while overlay is active
            PuzzleTile(side: .right, kind: .outward) {
                LeftAudioTile(urlString: left.label)
                    .padding(14)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }
            .frame(height: tileHeight)
            .contentShape(Rectangle())
            .modifier(GlobalFrameReporter(space: "audio-board", onChange: onLeftFrameChange))
            .highPriorityGesture(dragGesture)
            .opacity(draggingLeftId == left.id ? 0 : 1)

            // RIGHT tile (text)
            PuzzleTile(side: .left, kind: .inward) {
                Text(right.label)
                    .font(.body)
                    .foregroundStyle(.primary)
                    .lineLimit(3)
                    .multilineTextAlignment(.leading)
                    .padding(14)
                    .padding(.leading, 18) // safe from inward bite
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }
            .frame(height: tileHeight)
            .modifier(GlobalFrameReporter(space: "audio-board", onChange: onRightFrameChange))
        }
        .accessibilityElement(children: .contain)
    }

    private var dragGesture: some Gesture {
        DragGesture(minimumDistance: 1, coordinateSpace: .named("audio-board"))
            .onChanged { value in
                if lastTranslation == .zero {
                    onLeftDragStart()
                }
                let delta = CGSize(
                    width: value.translation.width - lastTranslation.width,
                    height: value.translation.height - lastTranslation.height
                )
                lastTranslation = value.translation
                onLeftDragBy(delta)
            }
            .onEnded { _ in
                onLeftDragEnd()
                lastTranslation = .zero
            }
    }
}

private struct UnmatchedRowView: View {
    let left: Piece?
    let right: Piece?
    let gap: CGFloat
    let tileHeight: CGFloat

    let draggingLeftId: String?

    let onLeftFrameChange: (_ leftId: String, _ frame: CGRect) -> Void
    let onRightFrameChange: (_ rightId: String, _ frame: CGRect) -> Void

    let onLeftDragStart: (_ leftId: String) -> Void
    let onLeftDragBy: (_ leftId: String, _ delta: CGSize) -> Void
    let onLeftDragEnd: () -> Void

    @State private var lastTranslation: CGSize = .zero

    var body: some View {
        HStack(spacing: gap) {
            // LEFT tile (audio) — gesture source; hidden while overlay is active
            Group {
                if let left {
                    PuzzleTile(side: .right, kind: .outward) {
                        LeftAudioTile(urlString: left.label)
                            .padding(14)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    }
                    .frame(height: tileHeight)
                    .contentShape(Rectangle())
                    .modifier(GlobalFrameReporter(space: "audio-board") { frame in
                        onLeftFrameChange(left.id, frame)
                    })
                    .highPriorityGesture(dragGesture(leftId: left.id))
                    .opacity(draggingLeftId == left.id ? 0 : 1)
                } else {
                    PuzzleTile(side: .right, kind: .outward) {
                        Text("—")
                            .font(.callout)
                            .foregroundStyle(.secondary)
                            .padding(14)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    }
                    .frame(height: tileHeight)
                }
            }

            // RIGHT tile (text)
            Group {
                if let right {
                    PuzzleTile(side: .left, kind: .inward) {
                        Text(right.label)
                            .font(.body)
                            .foregroundStyle(.primary)
                            .lineLimit(3)
                            .multilineTextAlignment(.leading)
                            .padding(14)
                            .padding(.leading, 18)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    }
                    .frame(height: tileHeight)
                    .modifier(GlobalFrameReporter(space: "audio-board") { frame in
                        onRightFrameChange(right.id, frame)
                    })
                } else {
                    PuzzleTile(side: .left, kind: .inward) {
                        Text("—")
                            .font(.callout)
                            .foregroundStyle(.secondary)
                            .padding(14)
                            .padding(.leading, 18)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    }
                    .frame(height: tileHeight)
                }
            }
        }
        .accessibilityElement(children: .contain)
    }

    private func dragGesture(leftId: String) -> some Gesture {
        DragGesture(minimumDistance: 1, coordinateSpace: .named("audio-board"))
            .onChanged { value in
                if lastTranslation == .zero {
                    onLeftDragStart(leftId)
                }
                let delta = CGSize(
                    width: value.translation.width - lastTranslation.width,
                    height: value.translation.height - lastTranslation.height
                )
                lastTranslation = value.translation
                onLeftDragBy(leftId, delta)
            }
            .onEnded { _ in
                onLeftDragEnd()
                lastTranslation = .zero
            }
    }
}

// MARK: - Visuals

// The non-interactive visual used for the floating overlay
private struct AudioOverlayVisual: View {
    var body: some View {
        HStack {
            ZStack {
                Circle()
                    .fill(Color.accentColor.opacity(0.15))
                Circle()
                    .stroke(Color.accentColor, lineWidth: 2)
                Image(systemName: "play.fill")
                    .font(.title2.weight(.semibold))
                    .foregroundStyle(Color.accentColor)
                    .padding(.leading, 2)
            }
            .frame(width: 44, height: 44)
            Spacer(minLength: 0)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

// Reuse the audio "tile" look for the left cell (interactive)
private struct LeftAudioTile: View {
    let urlString: String?

    var body: some View {
        if let s = urlString, let u = URL(string: s) {
            AudioTileContent(url: u)
                .frame(maxWidth: .infinity, alignment: .leading)
        } else {
            HStack(spacing: 8) {
                Image(systemName: "speaker.slash.fill")
                Text("Audio")
            }
            .font(.body)
            .foregroundStyle(.secondary)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}

// MARK: - Utilities

private struct DragOverlay {
    let leftId: String
    let startFrame: CGRect
    var totalTranslation: CGSize
}

private struct GlobalFrameReporter: ViewModifier {
    let space: String
    let onChange: (CGRect) -> Void

    func body(content: Content) -> some View {
        content
            .background(
                GeometryReader { proxy in
                    Color.clear
                        .preference(
                            key: GlobalFramePreferenceKey.self,
                            value: proxy.frame(in: .named(space))
                        )
                }
            )
            .onPreferenceChange(GlobalFramePreferenceKey.self, perform: onChange)
    }
}

private struct GlobalFramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) { value = nextValue() }
}

private func makePointF(_ delta: CGSize) -> AudioConnectTaskComponentPointF {
    AudioConnectTaskComponentPointF(x: Float(delta.width), y: Float(delta.height))
}

private func makeRectFGlobal(_ rect: CGRect) -> AudioConnectTaskComponentRectF {
    AudioConnectTaskComponentRectF(
        left: Float(rect.minX),
        top: Float(rect.minY),
        right: Float(rect.maxX),
        bottom: Float(rect.maxY)
    )
}
