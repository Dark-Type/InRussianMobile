//
//  TextInputWithVariantTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//
import Shared
import SwiftUI

struct TextInputWithVariantTaskView: View {
    let component:  TextInputTaskWithVariantComponent
    let onContinueClick: (@escaping () -> Void) -> Void

    @State private var expandedGapId: String?

    var body: some View {
        if let block = component.state.block {
            VStack(spacing: 20) {
                Text(block.label)
                    .font(.system(size: 20, weight: .semibold))
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.center)
                    .padding(.top, 20)
                    .frame(maxWidth: .infinity)

                FlowTextWithGaps(
                    words: block.words,
                    gaps: block.gaps,
                    expandedGapId: $expandedGapId,
                    onVariantSelected: { gapId, variant in
                        component.onVariantSelected(0, gapId, variant)
                        expandedGapId = nil
                    }
                )
                .padding(.horizontal, 10)
                .padding(.vertical, 16)

                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.componentBackground.ignoresSafeArea())
            .onAppear {
                onContinueClick {
                    component.onContinueClick()
                }
            }
        } else {
            EmptyView()
        }
    }
}

struct FlowTextWithGaps: View {
    let words: [String]
    let gaps: [TextInputTaskWithVariantComponent.InputGap]
    @Binding var expandedGapId: String?
    let onVariantSelected: (String, String) -> Void

    
    private var flowItems: [FlowItem] {
        var items: [FlowItem] = []
        var gapIdx = 0

        for i in 0 ..< words.count {
            // Insert gap if gap at this position
            if gapIdx < gaps.count && gaps[gapIdx].pos == i {
                items.append(.gap(gaps[gapIdx]))
                gapIdx += 1
            }
            // Insert word
            items.append(.word(words[i]))
        }
        return items
    }

    var body: some View {
        HStack(spacing: 4) {
            ForEach(Array(flowItems.enumerated()), id: \.offset) { idx, item in
                switch item {
                case .word(let word):
                    Text(word + (idx == flowItems.count - 1 ? "" : " "))
                        .font(.system(size: 15, weight: .medium))
                        .foregroundColor(.primary.opacity(0.7))
                case .gap(let gap):
                    InlineGap(
                        gap: gap,
                        expanded: expandedGapId == gap.id,
                        onExpandedChange: { isExpanded in
                            expandedGapId = isExpanded ? gap.id : nil
                        },
                        onVariantSelected: { variant in
                            onVariantSelected(gap.id, variant)
                        }
                    )
                }
            }
        }
    }

    // Helper enum for flow items
    enum FlowItem {
        case word(String)
        case gap(TextInputTaskWithVariantComponent.InputGap)
    }
}

struct InlineGap: View {
    let gap: TextInputTaskWithVariantComponent.InputGap
    let expanded: Bool
    let onExpandedChange: (Bool) -> Void
    let onVariantSelected: (String) -> Void

    private var borderColor: Color {
        switch gap.state {
        case .entering:
            return .gray.opacity(0.5)
        case .success:
            return .green
        case .error:
            return .red
        }
    }

    private var textColor: Color {
        switch gap.state {
        case .entering:
            return .gray.opacity(0.8)
        case .success:
            return .green
        case .error:
            return .red
        }
    }

    private var displayText: String {
        gap.selected.isEmpty ? "        " : gap.selected
    }

    var body: some View {
        Button(action: { onExpandedChange(true) }) {
            Text(displayText)
                .font(.system(size: 15, weight: .medium))
                .foregroundColor(textColor)
                .frame(minWidth: 40, minHeight: 32)
                .padding(.horizontal, 4)
                .background(
                    RoundedRectangle(cornerRadius: 6)
                        .fill(Color.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 6)
                                .stroke(borderColor, lineWidth: 1)
                        )
                )
        }
        .buttonStyle(.plain)
        .popover(isPresented: .constant(expanded), arrowEdge: .bottom) {
            VStack(alignment: .leading, spacing: 0) {
                ForEach(gap.variants, id: \.self) { variant in
                    Button(action: {
                        onVariantSelected(variant)
                        onExpandedChange(false)
                    }) {
                        Text(variant)
                            .font(.system(size: 15, weight: .medium))
                            .foregroundColor(.primary)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical, 8)
                            .padding(.horizontal, 12)
                    }
                    .background(Color.componentBackground)
                    if variant != gap.variants.last {
                        Divider()
                    }
                }
            }
            .background(Color.white)
            .cornerRadius(8)
            .frame(minWidth: 120)
            .padding(.vertical, 4)
        }
    }
}

// MARK: - Mock Model and Color Extensions (for completeness, replace with your real models)

final class TextInputTaskWithVariantComponent: ObservableObject {
    @Published var state: State = .sample

    func onContinueClick() {}
    func onVariantSelected(_ blockIndex: Int, _ gapId: String, _ variant: String) {
        // update the state according to selection (simplified)
        if let block = state.block {
            if let idx = block.gaps.firstIndex(where: { $0.id == gapId }) {
                state.block?.gaps[idx].selected = variant
                // update state: .success/.error/.entering
                if variant == block.gaps[idx].answer {
                    state.block?.gaps[idx].state = .success
                } else {
                    state.block?.gaps[idx].state = .error
                }
            }
        }
    }

    struct State {
        var block: Block?
        static let sample = State(block: Block.sample)
    }

    struct Block {
        let label: String
        let words: [String]
        var gaps: [InputGap]
        static let sample = Block(
            label: "Translate and select the correct variant.",
            words: ["I", "want", "to", "___", "a", "sandwich", "."],
            gaps: [
                InputGap(
                    id: "gap1", pos: 3, answer: "make", variants: ["make", "do", "cook"], selected: "", state: .entering
                )
            ]
        )
    }

    struct InputGap: Identifiable {
        let id: String
        let pos: Int
        let answer: String
        let variants: [String]
        var selected: String
        var state: GapState
        var uuid: String { id }
        var identity: String { id }
    }

    enum GapState {
        case entering, success, error
    }
}
