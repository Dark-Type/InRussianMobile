//
//  TextConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import SwiftUI

//struct TextConnectView: View {
//
//    let component: Any
//    let onSetOnEvent: (((() -> Void)?) -> Void)
//
//
//    @State private var rows: [Row] = Row.sample
//
//    var body: some View {
//        VStack(spacing: 12) {
//
//
//            ScrollView {
//                LazyVStack(spacing: 10) {
//                    ForEach(rows) { row in
//                        switch row.kind {
//                        case .pair(let left, let right, let isWrong, let isRightHovered):
//                            PairRowView(
//                                left: left,
//                                right: right,
//                                isRightHovered: isRightHovered,
//                                isWrong: isWrong
//                            )
//
//                        case .unmatched(let left, let right, let isWrong, let isRightHovered):
//                            UnmatchedRowView(
//                                left: left,
//                                right: right,
//                                isRightHovered: isRightHovered,
//                                isWrong: isWrong
//                            )
//                        }
//                    }
//
//                    Spacer(minLength: 24)
//                }
//                .padding(12)
//            }
//        }
//        .frame(maxWidth: .infinity, alignment: .topLeading)
//        .background(secondaryBackground().ignoresSafeArea())
//        .onAppear {
//
//            onSetOnEvent {
//                // TODO: call your real continue action, e.g. component.onContinueClick()
//                
//            }
//        }
//    }
//}
//
//// MARK: - Row Types
//
//private struct Piece: Identifiable, Equatable {
//    let id: String
//    let label: String
//}
//
//private struct Row: Identifiable, Equatable {
//    enum Kind: Equatable {
//        case pair(left: Piece, right: Piece, isWrong: Bool, isRightHovered: Bool)
//        case unmatched(left: Piece?, right: Piece?, isWrong: Bool, isRightHovered: Bool)
//    }
//
//    let id: UUID = UUID()
//    let kind: Kind
//
//    static let sample: [Row] = [
//        Row(kind: .pair(left: .init(id: "l1", label: "Cat"), right: .init(id: "r1", label: "Кот"), isWrong: false, isRightHovered: false)),
//        Row(kind: .unmatched(left: .init(id: "l2", label: "Dog"), right: nil, isWrong: false, isRightHovered: false)),
//        Row(kind: .unmatched(left: nil, right: .init(id: "r3", label: "Дом"), isWrong: false, isRightHovered: false)),
//        Row(kind: .pair(left: .init(id: "l4", label: "Sun"), right: .init(id: "r4", label: "Солнце"), isWrong: true, isRightHovered: false)),
//    ]
//}
//
//// MARK: - Rows
//
//private struct UnmatchedRowView: View {
//    let left: Piece?
//    let right: Piece?
//    let isRightHovered: Bool
//    let isWrong: Bool
//
//    private var rowFill: Color {
//        isWrong ? .red : componentBackground()
//    }
//    private var rowStroke: Color {
//        if isWrong { .red }
//        else if isRightHovered { .accentColor }
//        else { separatorColor() }
//    }
//
//    var body: some View {
//        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
//
//        HStack(alignment: .center, spacing: 12) {
//            // Left column
//            Group {
//                if let left {
//                    PuzzleLayoutIn(tabSide: .right, background: surfaceBackground()) {
//                        Text(left.label)
//                            .font(.body)
//                            .foregroundStyle(.primary)
//                            .padding(.vertical, 4)
//                    }
//                } else {
//                    PuzzleLayoutIn(tabSide: .right, background: emptyBackground()) {
//                        Text("—")
//                            .font(.body)
//                            .foregroundStyle(.secondary)
//                            .padding(.vertical, 4)
//                    }
//                }
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//            .background(surfaceBackground(), in: RoundedRectangle(cornerRadius: 10, style: .continuous))
//            .overlay(
//                RoundedRectangle(cornerRadius: 10, style: .continuous)
//                    .stroke(outlineColor(), lineWidth: 1)
//            )
//
//            // Right column
//            Group {
//                if let right {
//                    PuzzleLayoutOut(tabSide: .left, background: surfaceBackground(), borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered), borderWidth: 1) {
//                        Text(right.label)
//                            .font(.body)
//                            .foregroundStyle(.primary)
//                            .padding(.vertical, 4)
//                    }
//                } else {
//                    PuzzleLayoutOut(tabSide: .left, background: emptyBackground(), borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered), borderWidth: 1) {
//                        Text("—")
//                            .font(.body)
//                            .foregroundStyle(.secondary)
//                            .padding(.vertical, 4)
//                    }
//                }
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//        }
//        .padding(.horizontal, 10)
//        .padding(.vertical, 8)
//        .background(rowFill, in: shape)
//        .overlay(shape.stroke(rowStroke, lineWidth: 1))
//    }
//
//    private func hoveredBorder(isWrong: Bool, hovered: Bool) -> Color {
//        if isWrong { return .red }
//        if hovered { return .accentColor }
//        return outlineColor()
//    }
//}
//
//private struct PairRowView: View {
//    let left: Piece
//    let right: Piece
//    let isRightHovered: Bool
//    let isWrong: Bool
//
//    private var rowFill: Color {
//        isWrong ? Color.red.opacity(0.1) : matchedBackground()
//    }
//    private var rowStroke: Color {
//        if isWrong { return Color.red }
//        if isRightHovered { return .accentColor }
//        return primaryBorder()
//    }
//
//    var body: some View {
//        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
//
//        HStack(alignment: .center, spacing: 12) {
//            // Left column with RIGHT notch
//            PuzzleLayoutIn(tabSide: .right, background: surfaceBackground()) {
//                Text(left.label)
//                    .font(.body)
//                    .foregroundStyle(.primary)
//                    .frame(maxHeight: .infinity, alignment: .center)
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//
//            // Right column with LEFT bulge
//            PuzzleLayoutOut(tabSide: .left, background: surfaceBackground(), borderColor: rowStroke, borderWidth: 1) {
//                Text(right.label)
//                    .font(.body)
//                    .foregroundStyle(.primary)
//                    .frame(maxHeight: .infinity, alignment: .center)
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//        }
//        .padding(.horizontal, 10)
//        .padding(.vertical, 8)
//        .background(rowFill, in: shape)
//        .overlay(shape.stroke(rowStroke, lineWidth: 1))
//    }
//}
//
//// MARK: - Color helpers (fall back gracefully if AppColors isn’t present)
//
//private func secondaryBackground() -> Color {
//    // Preferred: AppColors.secondaryBackground
//    // Fallback: system secondary background
//    if let color = UIColor(named: "SecondaryBackground") {
//        return Color(color)
//    }
//    return Color(.secondarySystemBackground)
//}
//
//private func componentBackground() -> Color {
//    // Preferred: AppColors.componentBackground
//    if let color = UIColor(named: "ComponentBackground") {
//        return Color(color)
//    }
//    return Color(.systemBackground)
//}
//
//private func surfaceBackground() -> Color {
//    // Use the same surface as the parent card for seamless layering
//    componentBackground()
//}
//
//private func emptyBackground() -> Color {
//    // Slightly different tone for empty placeholders
//    Color(.secondarySystemBackground)
//}
//
//private func matchedBackground() -> Color {
//    // A soft background when pair is matched
//    Color(.tertiarySystemFill)
//}
//
//private func separatorColor() -> Color {
//    if let color = UIColor(named: "Separator") {
//        return Color(color)
//    }
//    return Color(.separator)
//}
//
//private func outlineColor() -> Color {
//    separatorColor()
//}
//
//private func primaryBorder() -> Color {
//    // Subtle primary border (can be customized)
//    separatorColor()
//}
//
//private func accentColor() -> Color {
//    .accentColor
//}
//
//
//extension View {
//    @ViewBuilder
//    fileprivate func overlay<S: ShapeStyle, In: InsettableShape>(_ shape: In, stroke style: S, lineWidth: CGFloat) -> some View {
//        self.overlay(shape.stroke(style, lineWidth: lineWidth))
//    }
//}
