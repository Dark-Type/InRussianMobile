//
//  ListenSelectTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import AVFoundation
import SwiftUI
import Shared

struct ListenAndSelectTaskView: View {
    let component: ListenAndSelectComponent
    let onSetOnEvent: ((() -> Void)?) -> Void

    @StateValue private var state: ListenAndSelectComponentState

    init(component: ListenAndSelectComponent,
         onSetOnEvent: @escaping ((() -> Void)?) -> Void) {
        self.component = component
        self.onSetOnEvent = onSetOnEvent
        _state = StateValue(component.state)
    }

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                // Speaker cards
                ForEach(Array(state.audioBlocks.enumerated()), id: \.offset) { _, block in
                    SpeakerCard(audioBlock: block)
                }

                // Variants grid (2 columns)
                if !state.variants.isEmpty {
                    ChoiceGrid(variants: state.variants) { _ in
                        // NOTE: Android sample passed {} for onSelect.
                        // Wire your selection callback here if your KMP component exposes one.
                        // Example (if available): component.onVariantSelected(variantId)
                    }
                    .padding(.top, 8)
                }

                Spacer().frame(height: 8)
            }
            .padding(16)
        }
        .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
        .onAppear { onSetOnEvent { component.onContinueClick() } }
        .onChange(of: state.hash()) { _ in
            onSetOnEvent { component.onContinueClick() }
        }
    }
}

// MARK: - Speaker Card

private struct SpeakerCard: View {
    let audioBlock: AudioBlocks

    @StateObject private var playerModel = InlineAudioPlayerModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Title/label
            Text(audioBlock.name)
                .font(.title3.weight(.semibold))
                .foregroundStyle(.primary)

            HStack(alignment: .center, spacing: 12) {
                // Play/Pause button
                Button(action: playerModel.toggle) {
                    ZStack {
                        Circle()
                            .fill(Color.accentColor.opacity(0.15))
                        Circle()
                            .stroke(Color.accentColor, lineWidth: 2)

                        Image(systemName: playerModel.isPlaying ? "pause.fill" : "play.fill")
                            .font(.title2.weight(.semibold))
                            .foregroundStyle(Color.accentColor)
                            .padding(.leading, playerModel.isPlaying ? 0 : 2)
                    }
                    .frame(width: 50, height: 50)
                    .contentShape(Circle())
                }
                .buttonStyle(.plain)
                .accessibilityLabel(playerModel.isPlaying ? "Pause" : "Play")

                VStack(alignment: .leading, spacing: 4) {
                    if let text = nonEmpty(audioBlock.description) {
                        Text(text)
                            .font(.body.weight(.medium))
                            .foregroundStyle(.primary)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                    if let transcription = nonEmpty(audioBlock.descriptionTranslate) {
                        Text(transcription)
                            .font(.body)
                            .foregroundStyle(.secondary)
                            .fixedSize(horizontal: false, vertical: true)
                    }
                }
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            RoundedRectangle(cornerRadius: 12, style: .continuous)
                .fill(AppColors.Palette.componentBackground.color)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12, style: .continuous)
                .stroke(AppColors.Palette.stroke.color, lineWidth: 1)
        )
        .onAppear {
            playerModel.setURL(URL(string: audioBlock.audio))
        }
        .onChange(of: audioBlock.audio, perform: { newURLString in
            playerModel.setURL(URL(string: newURLString))
        })
        .onDisappear {
            playerModel.stopAndReset()
        }
    }

    private func nonEmpty(_ s: String?) -> String? {
        guard let s, !s.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return nil }
        return s
    }
}

private final class InlineAudioPlayerModel: ObservableObject {
    @Published var isPlaying: Bool = false

    private var player: AVPlayer?
    private var endObserver: NSObjectProtocol?

    func setURL(_ url: URL?) {
        stopAndReset()
        guard let url else { return }
        let player = AVPlayer(url: url)
        self.player = player

        endObserver = NotificationCenter.default.addObserver(
            forName: .AVPlayerItemDidPlayToEndTime,
            object: player.currentItem,
            queue: .main
        ) { [weak self] _ in
            self?.player?.seek(to: .zero)
            self?.isPlaying = false
        }
    }

    func toggle() {
        guard let player else { return }
        if isPlaying {
            player.pause()
            isPlaying = false
        } else {
            player.play()
            isPlaying = true
        }
    }

    func stopAndReset() {
        if let player {
            player.pause()
        }
        isPlaying = false
        if let endObserver {
            NotificationCenter.default.removeObserver(endObserver)
            self.endObserver = nil
        }
        player = nil
    }

    deinit {
        stopAndReset()
    }
}

// MARK: - Variants Grid

private struct ChoiceGrid: View {
    let variants: [Variant]
    let onSelect: (VariantIdString) -> Void

    private var columns: [GridItem] {
        [GridItem(.flexible(), spacing: 12), GridItem(.flexible(), spacing: 12)]
    }

    var body: some View {
        LazyVGrid(columns: columns, spacing: 12) {
            ForEach(variants, id: \.selfId) { v in
                ChoiceButton(variant: v) {
                    onSelect(v.selfId)
                    // Add haptic for feedback
                    UIImpactFeedbackGenerator(style: .light).impactOccurred()
                }
            }
        }
        .frame(maxWidth: .infinity)
        .accessibilityElement(children: .contain)
    }
}

private typealias VariantIdString = String

private extension Variant {
    var selfId: VariantIdString { String(describing: id) }
}

private struct ChoiceButton: View {
    let variant: Variant
    let onTap: () -> Void

    private var style: ChoiceStyle {
        switch variantStateKind(variant.state) {
        case .notSelected: return .notSelected
        case .selected:    return .selected
        case .correct:     return .correct
        case .incorrect:   return .incorrect
        }
    }

    var body: some View {
        Button(action: onTap) {
            Text(variant.text)
                .font(.body.weight(.semibold))
                .foregroundStyle(style.textColor)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 12)
                .padding(.horizontal, 12)
                .contentShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
        }
        .buttonStyle(.plain)
        .background(
            RoundedRectangle(cornerRadius: 10, style: .continuous)
                .fill(style.backgroundColor)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10, style: .continuous)
                .stroke(style.borderColor, lineWidth: style.borderWidth)
        )
        .accessibilityLabel(variant.text)
    }
}

// MARK: - Styles

private enum ChoiceStyle {
    case notSelected, selected, correct, incorrect

    var backgroundColor: Color {
        switch self {
        case .notSelected, .incorrect: return AppColors.Palette.componentBackground.color
        case .selected: return .orange.opacity(0.9)
        case .correct: return .green.opacity(0.9)
        }
    }

    var borderColor: Color {
        switch self {
        case .notSelected: return Color.gray.opacity(0.3)
        case .selected: return .orange
        case .correct: return .green
        case .incorrect: return AppColors.Palette.stroke.color
        }
    }

    var borderWidth: CGFloat {
        switch self {
        case .notSelected: return 1
        case .selected, .correct: return 0
        case .incorrect: return 1
        }
    }

    var textColor: Color {
        switch self {
        case .selected, .correct: return .white
        case .notSelected, .incorrect: return .primary.opacity(0.7)
        }
    }
}

// Interpret KMP sealed VariantState via type name (Entering/Selected/Correct/Incorrect).
private enum VariantStateKind { case notSelected, selected, correct, incorrect }
private func variantStateKind(_ state: Any?) -> VariantStateKind {
    let name = String(reflecting: type(of: state as Any))
    if name.localizedCaseInsensitiveContains("Correct") { return .correct }
    if name.localizedCaseInsensitiveContains("Selected") { return .selected }
    if name.localizedCaseInsensitiveContains("Incorrect") { return .incorrect }
    return .notSelected
}
