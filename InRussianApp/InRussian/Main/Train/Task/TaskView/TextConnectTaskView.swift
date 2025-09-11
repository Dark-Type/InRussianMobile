//
//  TextConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import Shared
import SwiftUI

struct TextConnectTaskView: View {
    let component: TextConnectTaskComponent
    let onSetOnEvent: ((() -> Void)?) -> Void

    @StateValue private var state: TextConnectTaskComponentState

    // Drag overlay state (renders ABOVE everything)
    @State private var dragging: DragOverlay?
    @State private var leftFrames: [String: CGRect] = [:] // leftId -> frame in "board" space

    private let boardSpace = "board"
    private let tileHeight: CGFloat = 120
    private let unmatchedGap: CGFloat = 16

    init(component: TextConnectTaskComponent, onSetOnEvent: @escaping ((() -> Void)?) -> Void) {
        self.component = component
        self.onSetOnEvent = onSetOnEvent
        _state = StateValue(component.state)
    }

    var body: some View {
        ZStack(alignment: .topLeading) {
            ScrollView {
                LazyVStack(spacing: 10) {
                    ForEach(buildViewRows(state), id: \.id) { row in
                        switch row {
                        case .pair(let left, let right, _, _):
                            PairRowView(
                                left: left,
                                right: right,
                                draggingLeftId: dragging?.leftId,
                                onLeftFrameChange: { frame in
                                    leftFrames[left.id] = frame
                                    component.onPairLeftPositioned(leftId: left.id, rect__: makeRectFGlobal(frame))
                                },
                                onRightFrameChange: { frame in
                                    component.onRightPositioned(rightId: right.id, rect__: makeRectFGlobal(frame))
                                },
                                onLeftDragStart: {
                                    component.startDrag(fromPair: true, leftId: left.id)
                                    startOverlay(for: left)
                                },
                                onLeftDragBy: { delta in
                                    component.dragBy(delta__: makePointF(delta))
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
                                draggingLeftId: dragging?.leftId,
                                onLeftFrameChange: { leftId, frame in
                                    leftFrames[leftId] = frame
                                    component.onLeftPositioned(leftId: leftId, rect__: makeRectFGlobal(frame))
                                },
                                onRightFrameChange: { rightId, frame in
                                    component.onRightPositioned(rightId: rightId, rect__: makeRectFGlobal(frame))
                                },
                                onLeftDragStart: { leftId in
                                    component.startDrag(fromPair: false, leftId: leftId)
                                    if let piece = state.leftPieces.first(where: { $0.id == leftId }) {
                                        startOverlay(for: piece)
                                    }
                                },
                                onLeftDragBy: { leftId, delta in
                                    component.dragBy(leftId: leftId, delta__: makePointF(delta))
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
                    Spacer().frame(height: 24)
                }
                .padding(12)
            }
            .coordinateSpace(name: boardSpace)

            // Floating overlay for the dragged LEFT tile (always on top)
            if let d = dragging {
                PuzzleTile(side: .right, kind: .outward) {
                    Text(d.label)
                        .font(.body)
                        .foregroundStyle(.primary)
                        .lineLimit(3)
                        .multilineTextAlignment(.leading)
                        .padding(14)
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                }
                .frame(width: d.startFrame.width, height: d.startFrame.height)
                .position(x: d.startFrame.midX + d.totalTranslation.width,
                          y: d.startFrame.midY + d.totalTranslation.height)
                .scaleEffect(1.02) // subtle lift for UX
                .shadow(color: .black.opacity(0.12), radius: 10, x: 0, y: 6)
                .allowsHitTesting(false) // let gestures stay on the original tile
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

    // MARK: - Overlay control

    private func startOverlay(for left: Piece) {
        guard let frame = leftFrames[left.id] else { return }
        dragging = DragOverlay(leftId: left.id, label: left.label, startFrame: frame, totalTranslation: .zero)
    }

    private func stopOverlay() {
        withAnimation(.spring(response: 0.25, dampingFraction: 0.85)) {
            dragging = nil
        }
    }
}

// MARK: - View Row model built from state

private enum ViewRow: Identifiable, Equatable {
    case pair(left: Piece, right: Piece, isWrong: Bool, isRightHovered: Bool)
    case unmatched(left: Piece?, right: Piece?, isWrong: Bool, isRightHovered: Bool)

    var id: String {
        switch self {
        case .pair(let l, let r, _, _): return "pair:\(l.id)|\(r.id)"
        case .unmatched(let l, let r, _, _):
            let lid = l?.id ?? "nil-\(UUID().uuidString)"
            let rid = r?.id ?? "nil-\(UUID().uuidString)"
            return "unmatch:\(lid)|\(rid)"
        }
    }
}

private func buildViewRows(_ state: TextConnectTaskComponentState) -> [ViewRow] {
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
    for i in 0 ..< N {
        let l = i < remainingLeft.count ? remainingLeft[i] : nil
        let r = i < remainingRight.count ? remainingRight[i] : nil
        let isWrong = l.map { state.invalidLeftIds.contains($0.id) } ?? false
        let isHovered = r.map { state.hoveredRightId == $0.id } ?? false
        rows.append(.unmatched(left: l, right: r, isWrong: isWrong, isRightHovered: isHovered))
    }

    return rows
}

// MARK: - Pair Row (matched: no gap). Drag handled by parent overlay.

private struct PairRowView: View {
    let left: Piece
    let right: Piece

    let draggingLeftId: String?

    let onLeftFrameChange: (CGRect) -> Void
    let onRightFrameChange: (CGRect) -> Void

    let onLeftDragStart: () -> Void
    let onLeftDragBy: (CGSize) -> Void
    let onLeftDragEnd: () -> Void

    @State private var lastTranslation: CGSize = .zero

    private let tileHeight: CGFloat = 120

    var body: some View {
        HStack(spacing: 0) {
            // LEFT tile (gesture source; hidden when overlay is active for this tile)
            PuzzleTile(side: .right, kind: .outward) {
                Text(left.label)
                    .font(.body)
                    .foregroundStyle(.primary)
                    .lineLimit(3)
                    .multilineTextAlignment(.leading)
                    .padding(14)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }
            .frame(height: tileHeight)
            .contentShape(Rectangle())
            .modifier(GlobalFrameReporter(space: "board", onChange: onLeftFrameChange))
            .highPriorityGesture(dragGesture)
            .opacity(draggingLeftId == left.id ? 0 : 1) // hide original while overlay is shown

            // RIGHT tile (static)
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
            .modifier(GlobalFrameReporter(space: "board", onChange: onRightFrameChange))
        }
        .accessibilityElement(children: .contain)
    }

    private var dragGesture: some Gesture {
        DragGesture(minimumDistance: 1, coordinateSpace: .named("board"))
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

// MARK: - Unmatched Row (unmatched: visible gap). Drag handled by parent overlay.

private struct UnmatchedRowView: View {
    let left: Piece?
    let right: Piece?
    let gap: CGFloat

    let draggingLeftId: String?

    let onLeftFrameChange: (_ leftId: String, _ frame: CGRect) -> Void
    let onRightFrameChange: (_ rightId: String, _ frame: CGRect) -> Void

    let onLeftDragStart: (_ leftId: String) -> Void
    let onLeftDragBy: (_ leftId: String, _ delta: CGSize) -> Void
    let onLeftDragEnd: () -> Void

    @State private var lastTranslation: CGSize = .zero

    private let tileHeight: CGFloat = 120

    var body: some View {
        HStack(spacing: gap) {
            Group {
                if let left {
                    PuzzleTile(side: .right, kind: .outward) {
                        Text(left.label)
                            .font(.body)
                            .foregroundStyle(.primary)
                            .lineLimit(3)
                            .multilineTextAlignment(.leading)
                            .padding(14)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    }
                    .frame(height: tileHeight)
                    .contentShape(Rectangle())
                    .modifier(GlobalFrameReporter(space: "board") { frame in
                        onLeftFrameChange(left.id, frame)
                    })
                    .highPriorityGesture(leftDragGesture(left.id))
                    .opacity(draggingLeftId == left.id ? 0 : 1) // hide original while overlay is shown
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
                    .modifier(GlobalFrameReporter(space: "board") { frame in
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

    private func leftDragGesture(_ leftId: String) -> some Gesture {
        DragGesture(minimumDistance: 1, coordinateSpace: .named("board"))
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

// MARK: - Utilities

private struct DragOverlay {
    let leftId: String
    let label: String
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

private func makePointF(_ delta: CGSize) -> TextConnectTaskComponentPointF {
    TextConnectTaskComponentPointF(x: Float(delta.width), y: Float(delta.height))
}

private func makeRectFGlobal(_ rect: CGRect) -> TextConnectTaskComponentRectF {
    TextConnectTaskComponentRectF(
        left: Float(rect.minX),
        top: Float(rect.minY),
        right: Float(rect.maxX),
        bottom: Float(rect.maxY)
    )
}
