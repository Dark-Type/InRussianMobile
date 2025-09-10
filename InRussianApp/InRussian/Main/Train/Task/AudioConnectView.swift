//
//  AudioConnectView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//


import SwiftUI
import AVFoundation
import AVKit

//struct AudioConnectView: View {
//    
//    let component: Any
//    let onSetOnEvent: (((() -> Void)?) -> Void)
//
//    // Mock state so the view is immediately testable; replace with your KMM state binding.
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
//        .background(secondaryBackground().ignoresSafeArea())
//        .onAppear {
//            // Register a continue callback so the parent can invoke it (parity with Compose onContinueClick).
//            onSetOnEvent {
//                // TODO: wire to your real component, e.g. component.onContinueClick()
//                // Currently a no-op to remain compile-safe without KMM bindings.
//            }
//        }
//    }
//}
//
//// MARK: - Model
//
//private struct Piece: Identifiable, Equatable {
//    // left.label is expected to be an audio URL string, right.label a text
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
//    // Sample data for quick preview/testing. Replace as needed.
//    static let sample: [Row] = [
//        // Use publicly accessible small audio URLs if needed; left.label can be any URL string.
//        Row(kind: .pair(
//            left: .init(id: "l1", label: "https://www2.cs.uic.edu/~i101/SoundFiles/StarWars60.wav"),
//            right: .init(id: "r1", label: "Звёздные войны"),
//            isWrong: false,
//            isRightHovered: false
//        )),
//        Row(kind: .unmatched(
//            left: .init(id: "l2", label: "https://www2.cs.uic.edu/~i101/SoundFiles/ImperialMarch60.wav"),
//            right: nil,
//            isWrong: false,
//            isRightHovered: false
//        )),
//        Row(kind: .unmatched(
//            left: nil,
//            right: .init(id: "r3", label: "Дом"),
//            isWrong: false,
//            isRightHovered: false
//        )),
//        Row(kind: .pair(
//            left: .init(id: "l4", label: "https://www2.cs.uic.edu/~i101/SoundFiles/CantinaBand60.wav"),
//            right: .init(id: "r4", label: "Кантинa"),
//            isWrong: true,
//            isRightHovered: false
//        )),
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
//        isWrong ? Color.red.opacity(0.1) : componentBackground()
//    }
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
//            // Left audio (RIGHT notch)
//            Group {
//                if let left {
//                    PuzzleLayoutIn(tabSide: .right, background: surfaceBackground()) {
//                        AudioPlayButton(urlString: left.label)
//                            .frame(height: 36)
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
//                    .stroke(.stroke, lineWidth: 1)
//            )
//
//            // Right text (LEFT bulge)
//            Group {
//                if let right {
//                    PuzzleLayoutOut(
//                        tabSide: .left,
//                        background: surfaceBackground(),
//                        borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered),
//                        borderWidth: 1
//                    ) {
//                        Text(right.label)
//                            .font(.body)
//                            .foregroundStyle(.primary)
//                            .padding(.vertical, 4)
//                    }
//                } else {
//                    PuzzleLayoutOut(
//                        tabSide: .left,
//                        background: emptyBackground(),
//                        borderColor: hoveredBorder(isWrong: isWrong, hovered: isRightHovered),
//                        borderWidth: 1
//                    ) {
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
//        isWrong ? Color.red.opacity(0.1) : matchedBackground()
//    }
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
//            // Left audio (RIGHT notch)
//            PuzzleLayoutIn(tabSide: .right, background: surfaceBackground()) {
//                AudioPlayButton(urlString: left.label)
//                    .frame(height: 36)
//                    .padding(.vertical, 4)
//            }
//            .frame(maxWidth: .infinity)
//            .padding(.horizontal, 6)
//            .padding(.vertical, 4)
//
//            // Right text (LEFT bulge)
//            PuzzleLayoutOut(
//                tabSide: .left,
//                background: surfaceBackground(),
//                borderColor: rowStroke,
//                borderWidth: 1
//            ) {
//                Text(right.label)
//                    .font(.body)
//                    .foregroundStyle(.primary)
//                    .frame(maxHeight: .infinity, alignment: .center)
//                    .padding(.vertical, 4)
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
//// MARK: - Audio Button (AVFoundation)
//
//private struct AudioPlayButton: View {
//    let urlString: String?
//    var size: CGFloat = 50
//    var onError: ((Error) -> Void)? = nil
//
//    @State private var player: AVPlayer?
//    @State private var timeControlObs: NSKeyValueObservation?
//    @State private var statusObs: NSKeyValueObservation?
//    @State private var endObserver: Any?
//    @State private var isPlaying: Bool = false
//
//    var body: some View {
//        Button(action: toggle) {
//            ZStack {
//                Circle()
//                    .fill(Color(.secondarySystemBackground))
//                Image(systemName: isPlaying ? "pause.fill" : "play.fill")
//                    .font(.system(size: size * 0.42, weight: .semibold))
//                    .foregroundColor(Color(.darkGray))
//            }
//            .frame(width: size, height: size)
//            .contentShape(Circle())
//        }
//        .buttonStyle(.plain)
//        .onAppear(perform: configurePlayer)
//        .onChange(of: urlString) { _ in
//            configurePlayer()
//        }
//        .onDisappear {
//            cleanup()
//        }
//        .accessibilityLabel(isPlaying ? "Pause audio" : "Play audio")
//    }
//
//    private func configurePlayer() {
//        cleanup()
//
//        guard let urlString, let url = URL(string: urlString), !urlString.isEmpty else {
//            isPlaying = false
//            player = nil
//            return
//        }
//
//        let player = AVPlayer(url: url)
//        self.player = player
//
//        // Observe playback state
//        timeControlObs = player.observe(\.timeControlStatus, options: [.initial, .new]) { player, _ in
//            DispatchQueue.main.async {
//                switch player.timeControlStatus {
//                case .playing:
//                    self.isPlaying = true
//                default:
//                    self.isPlaying = false
//                }
//            }
//        }
//
//        // Observe item status for errors
//        if let item = player.currentItem {
//            statusObs = item.observe(\.status, options: [.new, .initial]) { item, _ in
//                if item.status == .failed, let e = item.error {
//                    DispatchQueue.main.async {
//                        self.onError?(e)
//                    }
//                }
//            }
//
//            endObserver = NotificationCenter.default.addObserver(
//                forName: .AVPlayerItemDidPlayToEndTime,
//                object: item,
//                queue: .main
//            ) { _ in
//                self.player?.seek(to: .zero)
//                self.isPlaying = false
//            }
//        }
//    }
//
//    private func toggle() {
//        guard let player else { return }
//        if isPlaying {
//            player.pause()
//        } else {
//            player.play()
//        }
//        // isPlaying will be updated by KVO
//    }
//
//    private func cleanup() {
//        timeControlObs?.invalidate()
//        timeControlObs = nil
//        statusObs?.invalidate()
//        statusObs = nil
//        if let endObserver {
//            NotificationCenter.default.removeObserver(endObserver)
//            self.endObserver = nil
//        }
//        player?.pause()
//        player = nil
//        isPlaying = false
//    }
//}
//
//// MARK: - Color helpers (fallback to system colors if AppColors isn't present)
//
//private func secondaryBackground() -> Color {
//    if let color = UIColor(named: "SecondaryBackground") {
//        return Color(color)
//    }
//    return Color(.secondarySystemBackground)
//}
//
//private func componentBackground() -> Color {
//    if let color = UIColor(named: "ComponentBackground") {
//        return Color(color)
//    }
//    return Color(.systemBackground)
//}
//
//private func surfaceBackground() -> Color {
//    componentBackground()
//}
//
//private func emptyBackground() -> Color {
//    Color(.secondarySystemBackground)
//}
//
//private func matchedBackground() -> Color {
//    Color(.tertiarySystemFill)
//}
//
