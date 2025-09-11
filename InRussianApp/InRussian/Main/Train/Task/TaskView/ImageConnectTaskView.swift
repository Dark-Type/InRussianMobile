//
//  ImageConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import SwiftUI
import Shared

struct ImageConnectTaskView: View {
    let component: ImageConnectTaskComponent
    let onSetOnEvent: ((() -> Void)?) -> Void

    @StateValue private var state: ImageConnectTaskComponentState

    init(component: ImageConnectTaskComponent, onSetOnEvent: @escaping ((() -> Void)?) -> Void) {
        self.component = component
        self.onSetOnEvent = onSetOnEvent
        _state = StateValue(component.state)
    }

    var body: some View {
        content
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
            .onAppear { onSetOnEvent { component.onContinueClick() } }
            .onChange(of: state.hash()) { _ in
                onSetOnEvent { component.onContinueClick() }
            }
    }
}

// MARK: - Decomposition

private extension ImageConnectTaskView {
    var content: some View {
        VStack(spacing: 0) {
            listScrollView
        }
    }

    var listScrollView: some View {
        ScrollView {
            rowsStack
        }
    }

    var rowsStack: some View {
        LazyVStack(spacing: 10) {
            rowsForEach
            Spacer().frame(height: 24)
        }
        .padding(12)
    }

    var rows: [ViewRow] {
        buildViewRows(state)
    }

    var rowsForEach: some View {
        ForEach(rows, id: \.id) { row in
            rowView(row)
        }
    }

    @ViewBuilder
    func rowView(_ row: ViewRow) -> some View {
        switch row {
        case .pair(let left, let right, let isWrong, let isRightHovered):
            ImagePairRowView(
                left: left,
                right: right,
                isRightHovered: isRightHovered,
                isWrong: isWrong,
                onLeftHandlePositioned: { frame in
                    component.onPairLeftPositioned(
                        leftId: left.id,
                        rect_: makeRectF(frame)
                    )
                },
                onRightPositioned: { frame in
                    component.onRightPositioned(
                        rightId: right.id,
                        rect_: makeRectF(frame)
                    )
                },
                onLeftDragStart: {
                    component.startDrag(fromPair: true, leftId: left.id)
                },
                onLeftDragBy: { delta in
                    component.dragBy(delta_: makePointF(delta))
                },
                onLeftDragEnd: { component.endDrag() },
                onLeftDragCancel: { component.cancelDrag() }
            )

        case .unmatched(let leftOpt, let rightOpt, let isWrong, let isRightHovered):
            ImageUnmatchedRowView(
                left: leftOpt,
                right: rightOpt,
                isRightHovered: isRightHovered,
                isWrong: isWrong,
                onLeftPositioned: { leftId, frame in
                    component.onLeftPositioned(
                        leftId: leftId,
                        rect_: makeRectF(frame)
                    )
                },
                onRightPositioned: { rightId, frame in
                    component.onRightPositioned(
                        rightId: rightId,
                        rect_: makeRectF(frame)
                    )
                },
                onLeftDragStart: { leftId in
                    component.startDrag(fromPair: false, leftId: leftId)
                },
                onLeftDragBy: { leftId, delta in
                    component.dragBy(leftId: leftId, delta_: makePointF(delta))
                },
                onLeftDragEnd: { component.endDrag() },
                onLeftDragCancel: { component.cancelDrag() }
            )
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
            let lid = l?.id ?? "nil"
            let rid = r?.id ?? "nil"
            return "unmatch:\(lid)|\(rid)"
        }
    }
}

private func buildViewRows(_ state: ImageConnectTaskComponentState) -> [ViewRow] {
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

// MARK: - Pair Row (matched)

private struct ImagePairRowView: View {
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

    @GestureState private var dragging = false
    @State private var lastTranslation: CGSize = .zero

    private var shape: RoundedRectangle { .init(cornerRadius: 12, style: .continuous) }

    var body: some View {
        let rowBg = isWrong ? Color.red.opacity(0.10) : AppColors.Palette.componentBackground.color
        let rowStroke: Color = isWrong ? .red : (isRightHovered ? .accentColor : AppColors.Palette.accent.color)

        HStack(spacing: 12) {
            PuzzleTile(side: .right, kind: .outward) {
                Text(left.label)
                    .font(.body)
                    .foregroundStyle(.primary)
                    .lineLimit(2)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            }
            .modifier(GlobalFrameReporter(onChange: onLeftHandlePositioned))
            .contentShape(Rectangle())
            .gesture(dragGesture)
            .padding(.vertical, 8)
            .padding(.leading, 10)

            PuzzleTile(side: .left, kind: .inward) {
                Text(right.label)
                    .font(.body)
                    .foregroundStyle(.primary)
                    .lineLimit(2)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                    .padding(.leading, 12)
            }
            .modifier(GlobalFrameReporter(onChange: onRightPositioned))
            .padding(.vertical, 8)
            .padding(.trailing, 10)
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .padding(.horizontal, 10)
        .padding(.vertical, 8)
        .background(rowBg, in: shape)
        .overlay(shape.stroke(rowStroke, lineWidth: 1.5))
        .animation(.default, value: isRightHovered)
        .animation(.default, value: isWrong)
    }

    private var dragGesture: some Gesture {
        DragGesture(minimumDistance: 0, coordinateSpace: .global)
            .updating($dragging) { _, state, _ in
                if state == false {
                    state = true
                    onLeftDragStart()
                    lastTranslation = .zero
                }
            }
            .onChanged { value in
                let delta = CGSize(width: value.translation.width - lastTranslation.width,
                                   height: value.translation.height - lastTranslation.height)
                lastTranslation = value.translation
                onLeftDragBy(delta)
            }
            .onEnded { _ in
                onLeftDragEnd()
                lastTranslation = .zero
            }
            .onEnded { _ in
                lastTranslation = .zero
            }
    }
}

// MARK: - Unmatched Row

private struct ImageUnmatchedRowView: View {
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

    @GestureState private var dragging = false
    @State private var lastTranslation: CGSize = .zero

    private var shape: RoundedRectangle { .init(cornerRadius: 12, style: .continuous) }

    var body: some View {
        let baseStroke = AppColors.Palette.stroke.color
        let rightBorder: Color = {
            if right == nil { return baseStroke }
            if isWrong { return .red }
            if isRightHovered { return .accentColor }
            return baseStroke
        }()

        HStack(spacing: 12) {
            // LEFT CELL (Image in a knob-right tile)
            Group {
                if let left {
                    PuzzleTile(side: .right, kind: .outward) {
                        TileAsyncImage(urlString: left.label)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                            .frame(height: 98) // mirror Android sizing hint
                    }
                    .modifier(GlobalFrameReporter { frame in onLeftPositioned(left.id, frame) })
                    .contentShape(Rectangle())
                    .gesture(dragGesture(for: left.id))
                } else {
                    PuzzleTile(side: .right, kind: .outward) {
                        Text("—")
                            .font(.callout)
                            .foregroundStyle(.secondary)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }
            }
            .padding(.vertical, 8)
            .padding(.leading, 10)

            Group {
                if let right {
                    PuzzleTile(side: .left, kind: .inward) {
                        Text(right.label)
                            .font(.body)
                            .foregroundStyle(.primary)
                            .lineLimit(2)
                            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                            .padding(.leading, 12)
                            .frame(height: 98)
                    }
                    .modifier(GlobalFrameReporter { frame in onRightPositioned(right.id, frame) })
                    .overlay(
                        RoundedRectangle(cornerRadius: 12, style: .continuous)
                            .stroke(rightBorder, lineWidth: 1)
                            .padding(1)
                    )
                } else {
                    PuzzleTile(side: .left, kind: .inward) {
                        Text("—")
                            .font(.callout)
                            .foregroundStyle(.secondary)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.leading, 12)
                            .frame(height: 98)
                    }
                    .overlay(
                        RoundedRectangle(cornerRadius: 12, style: .continuous)
                            .stroke(rightBorder, lineWidth: 1)
                            .padding(1)
                    )
                }
            }
            .padding(.vertical, 8)
            .padding(.trailing, 10)
        }
        .frame(maxWidth: .infinity, alignment: .center)
        .padding(.horizontal, 10)
        .padding(.vertical, 8)

        .animation(.default, value: isRightHovered)
        .animation(.default, value: isWrong)
    }

    private func dragGesture(for leftId: String) -> some Gesture {
        DragGesture(minimumDistance: 0, coordinateSpace: .global)
            .updating($dragging) { _, state, _ in
                if state == false {
                    state = true
                    onLeftDragStart(leftId)
                    lastTranslation = .zero
                }
            }
            .onChanged { value in
                let delta = CGSize(width: value.translation.width - lastTranslation.width,
                                   height: value.translation.height - lastTranslation.height)
                lastTranslation = value.translation
                onLeftDragBy(leftId, delta)
            }
            .onEnded { _ in
                onLeftDragEnd()
                lastTranslation = .zero
            }
            .onEnded { _ in
                lastTranslation = .zero
            }
    }
}

// MARK: - Image Loader inside a tile

private struct TileAsyncImage: View {
    let urlString: String
    var body: some View {
        let url = URL(string: urlString)
        AsyncImage(url: url) { phase in
            switch phase {
            case .success(let image):
                image
                    .resizable()
                    .scaledToFit()
                    .accessibilityLabel("Image")
            case .failure:
                HStack(spacing: 8) {
                    Image(systemName: "photo")
                    Text("Image unavailable").lineLimit(2)
                }
                .font(.footnote)
                .foregroundStyle(.secondary)
            case .empty:
                ZStack {
                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                        .fill(.secondary.opacity(0.08))
                    ProgressView()
                }
                .accessibilityHidden(true)
            @unknown default:
                Color.clear
            }
        }
    }
}

// MARK: - Geometry/Delta bridging helpers

private struct GlobalFrameReporter: ViewModifier {
    let onChange: (CGRect) -> Void
    func body(content: Content) -> some View {
        content
            .background(
                GeometryReader { proxy in
                    Color.clear
                        .preference(key: GlobalFramePreferenceKey.self, value: proxy.frame(in: .global))
                }
            )
            .onPreferenceChange(GlobalFramePreferenceKey.self) { frame in
                onChange(frame)
            }
    }
}

private struct GlobalFramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
        value = nextValue()
    }
}

private func makePointF(_ delta: CGSize) -> ImageConnectTaskComponentPointF {
    ImageConnectTaskComponentPointF(x: Float(delta.width), y: Float(delta.height))
}

private func makeRectF(_ rect: CGRect) -> ImageConnectTaskComponentRectF {
    ImageConnectTaskComponentRectF(
        left: Float(rect.minX),
        top: Float(rect.minY),
        right: Float(rect.maxX),
        bottom: Float(rect.maxY)
    )
}
