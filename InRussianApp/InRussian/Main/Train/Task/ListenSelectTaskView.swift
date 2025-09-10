//
//  ListenSelectTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import AVFoundation
import SwiftUI

struct ListenAndSelectTaskView: View {
    let component: ListenAndSelectComponent
    let onContinueClick: (@escaping () -> Void) -> Void

    @State private var audioPlayers: [UUID: AVPlayer] = [:]
    @State private var playingBlockId: UUID?

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                ForEach(component.state.audioBlocks) { audioBlock in
                    SpeakerElement(
                        audioBlock: audioBlock,
                        isPlaying: playingBlockId == audioBlock.id,
                        onPlayPause: {
                            handlePlayPause(for: audioBlock)
                        }
                    )
                }
                ChoiceElement(
                    variants: component.state.variants,
                    onSelect: { id in
                        component.onSelectVariant(id)
                    }
                )
                Spacer(minLength: 32)
            }
            .padding(.horizontal, 20)
            .padding(.top, 28)
        }
        .background(Color.componentBackground.ignoresSafeArea())
        .onAppear {
            onContinueClick {
                component.onContinueClick()
            }
        }
    }

    private func handlePlayPause(for audioBlock: AudioBlock) {
        let blockId = audioBlock.id
        if let player = audioPlayers[blockId], blockId == playingBlockId {
            player.pause()
            playingBlockId = nil
        } else {
            stopAllPlayers()
            let player = AVPlayer(url: audioBlock.audio)
            audioPlayers[blockId] = player
            playingBlockId = blockId
            player.play()
        }
    }

    private func stopAllPlayers() {
        for player in audioPlayers.values {
            player.pause()
        }
        playingBlockId = nil
    }
}

// MARK: - SpeakerElement

struct SpeakerElement: View {
    let audioBlock: AudioBlock
    let isPlaying: Bool
    let onPlayPause: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(audioBlock.name)
                .font(.system(size: 20, weight: .semibold))
                .foregroundColor(.primary)

            HStack(alignment: .center, spacing: 16) {
                Button(action: onPlayPause) {
                    Image(systemName: isPlaying ? "pause.fill" : "play.fill")
                        .resizable()
                        .frame(width: 32, height: 32)
                        .foregroundColor(.accentColor)
                        .background(Circle().fill(Color.secondaryBackground))
                }
                VStack(alignment: .leading, spacing: 4) {
                    if let desc = audioBlock.description, !desc.isEmpty {
                        Text(desc)
                            .font(.system(size: 16, weight: .medium))
                            .foregroundColor(.primary)
                    }
                    if let trans = audioBlock.descriptionTranslate, !trans.isEmpty {
                        Text(trans)
                            .font(.system(size: 15, weight: .medium))
                            .foregroundColor(.fontInactive)
                    }
                }
            }
            .padding(.vertical, 8)
        }
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 14, style: .continuous)
                .fill(Color.white)
                .shadow(color: Color.black.opacity(0.04), radius: 6, x: 0, y: 1)
        )
    }
}

// MARK: - ChoiceElement

struct ChoiceElement: View {
    let variants: [Variant]
    let onSelect: (UUID) -> Void

    var body: some View {
        VStack(spacing: 16) {
            ForEach(variants.chunked(into: 2), id: \.first!.id) { rowVariants in
                HStack(spacing: 15) {
                    ForEach(rowVariants) { variant in
                        ChoiceItem(
                            state: variant.state,
                            text: variant.text,
                            onSelect: { onSelect(variant.id) }
                        )
                    }
                    if rowVariants.count == 1 {
                        Spacer()
                    }
                }
            }
        }
        .padding(12)
        .background(
            RoundedRectangle(cornerRadius: 14, style: .continuous)
                .fill(Color.white)
                .shadow(color: Color.black.opacity(0.03), radius: 4, x: 0, y: 1)
        )
    }
}

// MARK: - ChoiceItem

struct ChoiceItem: View {
    let state: VariantState
    let text: String
    let onSelect: () -> Void

    var body: some View {
        Button(action: onSelect) {
            Text(text)
                .foregroundColor(labelColor(for: state))
                .font(.system(size: 16, weight: .semibold))
                .frame(maxWidth: .infinity, minHeight: 44)
                .padding(.horizontal, 8)
                .background(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(backgroundColor(for: state))
                        .overlay(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(borderColor(for: state), lineWidth: 1)
                        )
                )
        }
        .disabled(state == .correct || state == .incorrect)
    }

    private func backgroundColor(for state: VariantState) -> Color {
        switch state {
        case .selected: return .orange
        case .correct: return .green
        default: return Color.componentBackground
        }
    }

    private func borderColor(for state: VariantState) -> Color {
        switch state {
        case .notSelected: return Color.gray.opacity(0.3)
        default: return .clear
        }
    }

    private func labelColor(for state: VariantState) -> Color {
        switch state {
        case .correct, .selected: return .white
        default: return .primary.opacity(0.7)
        }
    }
}

// MARK: - Helpers & Mock Models

extension Array {
    func chunked(into size: Int) -> [[Element]] {
        var chunks: [[Element]] = []
        var start = 0
        while start < count {
            let end = Swift.min(start + size, count)
            chunks.append(Array(self[start ..< end]))
            start += size
        }
        return chunks
    }
}

// MARK: - Mock Models & Colors

final class ListenAndSelectComponent: ObservableObject {
    @Published var state: ListenSelectState = .sample
    func onContinueClick() {}
    func onSelectVariant(_ id: UUID) {}
}

struct ListenSelectState {
    var audioBlocks: [AudioBlock]
    var variants: [Variant]
    static let sample = ListenSelectState(
        audioBlocks: [
            AudioBlock(
                id: UUID(), audio: URL(string: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")!,
                name: "Speaker 1", description: "Hello!", descriptionTranslate: "Привет!"
            )
        ],
        variants: [
            Variant(id: UUID(), text: "A", state: .notSelected),
            Variant(id: UUID(), text: "B", state: .selected)
        ]
    )
}

struct AudioBlock: Identifiable {
    let id: UUID
    let audio: URL
    let name: String
    let description: String?
    let descriptionTranslate: String?
}

struct Variant: Identifiable {
    let id: UUID
    let text: String
    let state: VariantState
}

enum VariantState {
    case selected
    case correct
    case incorrect
    case notSelected
}
