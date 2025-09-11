//
//  PuzzleContent.swift
//  InRussian
//
//  Created by dark type on 11.09.2025.
//

import AVFoundation
import SwiftUI

enum PuzzleContent: Equatable {
    case text(String)
    case audio(url: URL)
    case image(url: URL, alt: String? = nil)
}

struct PuzzleTileRow: View {
    let left: PuzzleContent
    let right: PuzzleContent

    var baseHeight: CGFloat = 120
    var imageMaxExtra: CGFloat = 40
    var fillLeft: Color = .white
    var fillRight: Color = .white
    var stroke: Color = .secondary.opacity(0.3)
    var lineWidth: CGFloat = 1

    var rightTileSafeLeadingPadding: CGFloat = 18

    private var rowHeight: CGFloat {
        let hasImage: Bool = {
            if case .image = left { return true }
            if case .image = right { return true }
            return false
        }()
        return baseHeight + (hasImage ? imageMaxExtra : 0)
    }

    var body: some View {
        HStack(spacing: 0) {
            PuzzleTile(side: .right, kind: .outward,
                       fill: fillLeft, stroke: stroke, lineWidth: lineWidth)
            {
                tileContent(left)
                    .padding(14)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }

            PuzzleTile(side: .left, kind: .inward,
                       fill: fillRight, stroke: stroke, lineWidth: lineWidth)
            {
                tileContent(right)
                    .padding(14)
                    .padding(.leading, rightTileSafeLeadingPadding)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
            }
        }
        .frame(height: rowHeight)
        .contentShape(Rectangle())
        .accessibilityElement(children: .contain)
    }

    @ViewBuilder
    private func tileContent(_ content: PuzzleContent) -> some View {
        switch content {
        case .text(let string):
            TextTileContent(text: string)

        case .audio(let url):
            AudioTileContent(url: url)

        case .image(let url, let alt):
            ImageTileContent(url: url, alt: alt)
        }
    }
}

// MARK: - Tile Content Views

private struct TextTileContent: View {
    let text: String

    var body: some View {
        Text(text)
            .font(.body)
            .foregroundStyle(.primary)
            .lineLimit(3)
            .multilineTextAlignment(.leading)
            .accessibilityLabel(text)
    }
}

// Made internal so it can be reused by other views (e.g., AudioConnectTaskView)
final class AudioPlayerModel: ObservableObject {
    @Published var isPlaying: Bool = false

    let player: AVPlayer
    private var endObserver: NSObjectProtocol?

    init(url: URL) {
        player = AVPlayer(url: url)
        endObserver = NotificationCenter.default.addObserver(
            forName: .AVPlayerItemDidPlayToEndTime,
            object: player.currentItem,
            queue: .main
        ) { [weak self] _ in
            self?.player.seek(to: .zero)
            self?.isPlaying = false
        }
    }

    func toggle() {
        if isPlaying {
            player.pause()
            isPlaying = false
        } else {
            player.play()
            isPlaying = true
        }
    }

    deinit {
        if let endObserver {
            NotificationCenter.default.removeObserver(endObserver)
        }
    }
}

// Made internal so it can be reused by other views (e.g., AudioConnectTaskView)
struct AudioTileContent: View {
    @StateObject private var model: AudioPlayerModel

    init(url: URL) {
        _model = StateObject(wrappedValue: AudioPlayerModel(url: url))
    }

    var body: some View {
        HStack {
            Button(action: model.toggle) {
                ZStack {
                    Circle()
                        .fill(Color.accentColor.opacity(0.15))
                    Circle()
                        .stroke(Color.accentColor, lineWidth: 2)

                    Image(systemName: model.isPlaying ? "pause.fill" : "play.fill")
                        .font(.title2.weight(.semibold))
                        .foregroundStyle(Color.accentColor)
                        .padding(.leading, model.isPlaying ? 0 : 2)
                }
                .frame(width: 44, height: 44)
                .contentShape(Circle())
            }
            .buttonStyle(.plain)
            .accessibilityLabel(model.isPlaying ? "Pause" : "Play")

            Spacer(minLength: 0)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

private struct ImageTileContent: View {
    let url: URL
    let alt: String?

    var body: some View {
        GeometryReader { geo in
            AsyncImage(url: url) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .scaledToFit()
                        .frame(maxWidth: geo.size.width)
                        .accessibilityLabel(alt ?? "Image")
                case .failure:
                    fallback
                case .empty:
                    placeholder
                @unknown default:
                    fallback
                }
            }
        }
    }

    private var placeholder: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 8, style: .continuous)
                .fill(.secondary.opacity(0.08))
            ProgressView()
        }
        .accessibilityHidden(true)
    }

    private var fallback: some View {
        HStack(spacing: 8) {
            Image(systemName: "photo")
            Text(alt ?? "Image unavailable")
                .lineLimit(2)
        }
        .font(.footnote)
        .foregroundStyle(.secondary)
        .accessibilityLabel(alt ?? "Image unavailable")
    }
}

// MARK: - Previews

struct PuzzleTileRow_Previews: PreviewProvider {
    static let sampleTextA = "Compact sample text for the left tile."
    static let sampleTextB = "Right tile text, up to three lines are shown before truncation."
    static let audioURL = URL(string: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")!
    static let imageURL = URL(string: "https://forumimage.ru/uploads/20200121/157959990114989382.jpg")!

    static var previews: some View {
        Group {
            PuzzleTileRow(left: .text(sampleTextA),
                          right: .text(sampleTextB))
                .padding()
                .previewDisplayName("Text + Text")

            PuzzleTileRow(left: .audio(url: audioURL),
                          right: .text("Description on the right"))
                .padding()
                .previewDisplayName("Audio + Text")

            PuzzleTileRow(left: .text("Caption or description for the image on the right."),
                          right: .image(url: imageURL, alt: "Random image"))
                .padding()
                .previewDisplayName("Text + Image (slightly taller)")
        }
        .previewLayout(.sizeThatFits)
        .background(Color(white: 0.96))
    }
}
