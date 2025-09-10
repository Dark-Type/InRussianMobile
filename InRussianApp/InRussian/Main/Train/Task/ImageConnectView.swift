//
//  ImageConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import SwiftUI

//struct ImageConnectTaskView: View {
//    let component: Any
//    let onSetOnEvent: ((() -> Void)?) -> Void
//
//    @State private var rows: [Row] = Row.sample
//
//    var body: some View {
//        VStack(spacing: 0) {
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
//        .background(Color(.secondaryBackground).ignoresSafeArea())
//        .onAppear {
//            // Allow parent to register a "continue" action (parity with Compose onContinueClick)
//            onSetOnEvent {
//                // TODO: wire to your real component, e.g. component.onContinueClick()
//            }
//        }
//    }
//}
//
//// MARK: - Model
//
//private struct Piece: Identifiable, Equatable {
//    // left.label can be an image URL string, right.label is a text
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
//    let id: UUID = .init()
//    let kind: Kind
//
//    // Sample content; replace with real state later.
//    static let sample: [Row] = [
//        Row(kind: .pair(
//            left: .init(id: "l1", label: "Image A"),
//            right: .init(id: "r1", label: "Описание A"),
//            isWrong: false, isRightHovered: false
//        )),
//        Row(kind: .unmatched(
//            left: .init(id: "l2", label: "https://picsum.photos/id/1025/400/300"),
//            right: nil,
//            isWrong: false, isRightHovered: false
//        )),
//        Row(kind: .unmatched(
//            left: nil,
//            right: .init(id: "r3", label: "Дом"),
//            isWrong: false, isRightHovered: true
//        )),
//        Row(kind: .pair(
//            left: .init(id: "l4", label: "Image B"),
//            right: .init(id: "r4", label: "Описание B"),
//            isWrong: true, isRightHovered: false
//        ))
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
//        isWrong ? Color.red.opacity(0.1) : .componentBackground
//    }
//
//    private var rowStroke: Color {
//        if isWrong { return .red }
//        if isRightHovered { return .accentColor }
//        return .stroke
//    }
//
//    var body: some View {
//        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
//
//        HStack(alignment: .center, spacing: 12) {
//            // Left image (RIGHT notch)
//            Group {
//                if let left {
//                    PuzzleLayoutIn(tabSide: .right, background: .componentBackground) {
//                        ImageCell(urlString: left.label)
//                            .frame(height: 98)
//                    }
//                } else {
//                    PuzzleLayoutIn(tabSide: .right, background: .componentBackground) {
//                        Text("—")
//                            .font(.body)
//                            .foregroundStyle(.secondary)
//                            .frame(height: 98)
//                    }
//                }
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//            .background(.baseBackground, in: RoundedRectangle(cornerRadius: 10, style: .continuous))
//            .overlay(
//                RoundedRectangle(cornerRadius: 10, style: .continuous)
//                    .stroke(.stroke, lineWidth: 1)
//            )
//
//            // Right text (LEFT bulge)
//            Group {
//                if let right {
//                    PuzzleLayoutOut(
//                        tabSide: .left,
//                        background: .componentBackground,
//                        borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered),
//                        borderWidth: 1
//                    ) {
//                        Text(right.label)
//                            .font(.body)
//                            .foregroundStyle(.primary)
//                            .frame(height: 98)
//                    }
//                } else {
//                    PuzzleLayoutOut(
//                        tabSide: .left,
//                        background: .componentBackground,
//                        borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered),
//                        borderWidth: 1
//                    ) {
//                        Text("—")
//                            .font(.body)
//                            .foregroundStyle(.secondary)
//                            .frame(height: 98)
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
//        if hovered { return .accent }
//        return .stroke
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
//        isWrong ? Color.red.opacity(0.1) : .baseBackground
//    }
//
//    private var rowStroke: Color {
//        if isWrong { return .red }
//        if isRightHovered { return .accent }
//        return .stroke
//    }
//
//    var body: some View {
//        let shape = RoundedRectangle(cornerRadius: 12, style: .continuous)
//
//        HStack(alignment: .center, spacing: 12) {
//            // Left text (RIGHT notch) — mirrors Compose PairRow
//            PuzzleLayoutIn(tabSide: .right, background: .componentBackground) {
//                Text(left.label)
//                    .font(.body)
//                    .foregroundStyle(.primary)
//                    .frame(maxHeight: .infinity, alignment: .center)
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//
//            // Right text (LEFT bulge)
//            PuzzleLayoutOut(
//                tabSide: .left,
//                background: .componentBackground,
//                borderColor: rowStroke,
//                borderWidth: 1
//            ) {
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
//// MARK: - Image Cell
//
//private struct ImageCell: View {
//    let urlString: String?
//    var body: some View {
//        if let urlString, let url = URL(string: urlString), !urlString.isEmpty {
//            if #available(iOS 15.0, *) {
//                AsyncImage(url: url) { phase in
//                    switch phase {
//                    case .empty:
//                        ZStack {
//                            Color(.tertiarySystemFill)
//                            ProgressView()
//                        }
//                        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
//
//                    case .success(let image):
//                        image
//                            .resizable()
//                            .scaledToFit()
//                            .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
//
//                    case .failure:
//                        placeholder
//
//                    @unknown default:
//                        placeholder
//                    }
//                }
//            } else {
//                // Fallback for iOS < 15: simple placeholder
//                placeholder
//            }
//        } else {
//            placeholder
//        }
//    }
//
//    private var placeholder: some View {
//        ZStack {
//            Color(.tertiarySystemFill)
//            Image(systemName: "photo")
//                .font(.system(size: 20, weight: .semibold))
//                .foregroundStyle(.secondary)
//        }
//        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
//    }
//}
