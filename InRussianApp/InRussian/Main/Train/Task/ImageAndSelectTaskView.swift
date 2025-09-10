//
//  ImageAndSelectTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//


import SwiftUI
import Combine
import UniformTypeIdentifiers


struct ImageAndSelectTaskView: View {
    let component: Any
    let onSetOnEvent: (((() -> Void)?) -> Void)

    @State private var state = TaskState.sample

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 24) {
                ForEach(state.imageBlocks) { block in
                    ImageBlockView(block: block)
                        .padding(.horizontal, 16)
                }

                ChoiceElementView(
                    title: "Choose the correct option",
                    variants: state.variants,
                    selectedID: state.selectedVariantID,
                    onSelect: { selected in
                        state.selectedVariantID = selected
                        // TODO: forward selection to your component: component.onSelect(selected)
                    }
                )
                .padding(.horizontal, 16)

                Spacer(minLength: 28)
            }
            .padding(.vertical, 16)
        }
        .background(.secondaryBackground)
        .frame(maxWidth: .infinity, alignment: .topLeading)
        
        .onAppear {
            
            onSetOnEvent {
                // TODO: call component.onContinueClick()
            }
        }
    }
}

// MARK: - Image Block

private struct ImageBlockView: View {
    let block: ImageBlockModel
    @StateObject private var aspectLoader: ImageAspectLoader

    init(block: ImageBlockModel) {
        self.block = block
        _aspectLoader = StateObject(wrappedValue: ImageAspectLoader(urlString: block.imageURL))
    }

    var body: some View {
        VStack(alignment: .center, spacing: 12) {
            // Title
            HStack {
                Text(block.name)
                    .font(.system(size: 22, weight: .semibold))
                    .foregroundStyle(.fontCaptive)
                Spacer(minLength: 0)
            }
            .frame(maxWidth: .infinity)
            .padding(.top, 12)

            // Image with intrinsic aspect
            ImageWithAspect(urlString: block.imageURL, targetAspect: aspectLoader.aspect)
                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 6)
                .accessibilityLabel(block.name)

            // Description
            if let desc = block.description, !desc.isEmpty {
                Text(desc)
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(.fontCaptive)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 6)
            }

            // Translation
            if let tr = block.descriptionTranslate, !tr.isEmpty {
                Text(tr)
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(.fontInactive)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 6)
            }
        }
        .padding(.vertical, 4)
    }
}

private struct ImageWithAspect: View {
    let urlString: String
    let targetAspect: CGFloat? // width / height

    var body: some View {
        Group {
            if #available(iOS 15.0, *) {
                if let url = URL(string: urlString) {
                    AsyncImage(url: url) { phase in
                        switch phase {
                        case .empty:
                            placeholder
                                .overlay(ProgressView())
                        case .success(let image):
                            image
                                .resizable()
                                .scaledToFit()
                                .modifier(AspectRatioIfAvailable(targetAspect))
                        case .failure:
                            placeholder
                        @unknown default:
                            placeholder
                        }
                    }
                } else {
                    placeholder
                }
            } else {
                // Simple fallback
                placeholder
            }
        }
    }

    private var placeholder: some View {
        ZStack {
            Color(.tertiarySystemFill)
            Image(systemName: "photo")
                .font(.system(size: 20, weight: .semibold))
                .foregroundStyle(.secondary)
        }
        .modifier(AspectRatioIfAvailable(targetAspect))
    }

    private struct AspectRatioIfAvailable: ViewModifier {
        let aspect: CGFloat?
        init(_ aspect: CGFloat?) { self.aspect = aspect }
        func body(content: Content) -> some View {
            if let aspect, aspect.isFinite, aspect > 0 {
                content
                    .aspectRatio(aspect, contentMode: .fit)
            } else {
                content
                    .aspectRatio(1, contentMode: .fit)
            }
        }
    }
}

// Loads the remote image just to compute its intrinsic aspect ratio (width/height).
private final class ImageAspectLoader: ObservableObject {
    @Published var aspect: CGFloat? = nil
    private var task: URLSessionDataTask?

    init(urlString: String) {
        load(urlString: urlString)
    }

    func load(urlString: String) {
        task?.cancel()
        guard let url = URL(string: urlString) else {
            self.aspect = nil
            return
        }
        // Lightweight head-like fetch; we still download image bytes to read metadata safely.
        let request = URLRequest(url: url, cachePolicy: .returnCacheDataElseLoad, timeoutInterval: 30)
        task = URLSession.shared.dataTask(with: request) { [weak self] data, _, _ in
            guard let self = self, let data, let ui = UIImage(data: data) else { return }
            let w = ui.size.width
            let h = ui.size.height
            guard w > 0, h > 0 else { return }
            DispatchQueue.main.async {
                self.aspect = w / h
            }
        }
        task?.resume()
    }

    deinit {
        task?.cancel()
    }
}

// MARK: - Choice Element

private struct ChoiceElementView: View {
    var title: String?
    var variants: [ChoiceVariant]
    var selectedID: String?
    var onSelect: (String) -> Void

    private let grid = [GridItem(.adaptive(minimum: 140), spacing: 12, alignment: .top)]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            if let title, !title.isEmpty {
                Text(title)
                    .font(.headline)
                    .foregroundStyle(.primary)
                    .padding(.horizontal, 4)
            }

            LazyVGrid(columns: grid, spacing: 12) {
                ForEach(variants) { v in
                    ChoiceChip(
                        title: v.title,
                        isSelected: v.id == selectedID,
                        action: { onSelect(v.id) }
                    )
                }
            }
        }
        .padding(.vertical, 8)
    }
}

private struct ChoiceChip: View {
    var title: String
    var isSelected: Bool
    var action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .imageScale(.medium)
                Text(title)
                    .font(.body)
                    .lineLimit(2)
                    .multilineTextAlignment(.leading)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 10)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .fill(isSelected ? .accent.opacity(0.15) : .componentBackground)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .stroke(isSelected ? .accent : .stroke, lineWidth: 1)
            )
        }
        .buttonStyle(.plain)
        .accessibilityLabel(title)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

// MARK: - Demo State Models (replace with your component-bound state)

private struct ImageBlockModel: Identifiable, Equatable {
    let id: UUID = UUID()
    let name: String
    let imageURL: String
    let description: String?
    let descriptionTranslate: String?
}

private struct ChoiceVariant: Identifiable, Equatable {
    let id: String
    let title: String
}

private struct TaskState: Equatable {
    var imageBlocks: [ImageBlockModel]
    var variants: [ChoiceVariant]
    var selectedVariantID: String?

    static let sample = TaskState(
        imageBlocks: [
            .init(
                name: "Московский Кремль",
                imageURL: "https://picsum.photos/id/1011/1200/800",
                description: "Вид на Кремль и набережную.",
                descriptionTranslate: "View of the Kremlin and the embankment."
            ),
            .init(
                name: "Санкт‑Петербург",
                imageURL: "https://picsum.photos/id/1025/800/1200",
                description: "Крыши домов под солнцем.",
                descriptionTranslate: "Rooftops under the sun."
            )
        ],
        variants: [
            .init(id: "v1", title: "Вариант 1"),
            .init(id: "v2", title: "Вариант 2"),
            .init(id: "v3", title: "Вариант 3"),
            .init(id: "v4", title: "Вариант 4")
        ],
        selectedVariantID: nil
    )
}
