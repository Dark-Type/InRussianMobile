//
//  TextInputTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//


import SwiftUI
import Shared

struct TextInputTaskView: View {
    let component: TextInputTaskComponent
    let onSetOnEvent: ((() -> Void)?) -> Void

    @StateValue private var state: TextInputTaskComponentState

    init(component: TextInputTaskComponent, onSetOnEvent: @escaping ((() -> Void)?) -> Void) {
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

private extension TextInputTaskView {
    var content: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(Array(state.blocks.enumerated()), id: \.offset) { idx, block in
                    blockView(index: idx, block: block)
                }
                Spacer().frame(height: 24)
            }
            .padding(16)
            .background(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .fill(AppColors.Palette.componentBackground.color)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .stroke(AppColors.Palette.stroke.color, lineWidth: 1)
            )
            .padding(12)
        }
    }

    @ViewBuilder
    func blockView(index: Int, block: TextInputTaskComponentInputBlock) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(block.label)
                .font(.title3.weight(.semibold))
                .foregroundStyle(.primary)

            InlineGapText(
                blockIndex: index,
                words: block.words,
                gaps: block.gaps,
                onTextChange: { gapId, newText in
                    component.onTextChange(blockIndex: Int32(index), gapId: gapId, newText: newText)
                }
            )
            .padding(.top, 4)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

// MARK: - Inline flowing text + gaps

private struct InlineGapText: View {
    let blockIndex: Int
    let words: [String]
    let gaps: [TextInputTaskComponentInputGap]
    let onTextChange: (_ gapId: String, _ newText: String) -> Void

    // Build tokens once
    private var tokens: [InlineToken] {
        buildTokens(words: words, gaps: gaps)
    }

    var body: some View {
        FlowLayout(6, 10) {
            ForEach(Array(tokens.enumerated()), id: \.offset) { _, token in
                switch token {
                case .text(let s):
                    Text(s)
                        .font(.body)
                        .foregroundStyle(.secondary)
                case .gap(let gap):
                    GapField(gap: gap) { newValue in
                        onTextChange(gap.id, newValue)
                    }
                }
            }
        }
        .accessibilityElement(children: .contain)
    }
}

// The tokenized inline content: either a plain text run (a single word)
// or an editable gap field.
private enum InlineToken: Equatable {
    case text(String)
    case gap(TextInputTaskComponentInputGap)
}

private func buildTokens(words: [String], gaps: [TextInputTaskComponentInputGap]) -> [InlineToken] {
    let byPos = Dictionary(grouping: gaps, by: { Int($0.pos) })
    var result: [InlineToken] = []
    for i in 0..<words.count {
        if let gs = byPos[i] {
            // If multiple gaps share a position, render them in the order received
            for g in gs.sorted(by: { Int($0.pos) < Int($1.pos) }) {
                result.append(.gap(g))
            }
        }
        result.append(.text(words[i]))
    }
    return result
}

// MARK: - Gap field

private struct GapField: View {
    let gap: TextInputTaskComponentInputGap
    let onChange: (String) -> Void

    @State private var value: String = ""
    @FocusState private var focused: Bool

    init(gap: TextInputTaskComponentInputGap, onChange: @escaping (String) -> Void) {
        self.gap = gap
        self.onChange = onChange
        _value = State(initialValue: gap.input)
    }

    var body: some View {
        TextField("", text: $value)
            .textFieldStyle(.plain)
            .font(.body.weight(.medium))
            .foregroundStyle(textColor)
            .padding(.horizontal, 6)
            .frame(width: fieldWidth, height: 28)
            .background(
                RoundedRectangle(cornerRadius: 6, style: .continuous)
                    .fill(Color.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 6, style: .continuous)
                    .stroke(borderColor, lineWidth: focused ? 1.5 : 1)
            )
            .focused($focused)
            .onChange(of: value) { new in
                onChange(new)
            }
            .onChange(of: gap.input) { new in
                // Keep local in sync when state updates externally
                if new != value { value = new }
            }
            .accessibilityLabel("Input")
    }

    private var borderColor: Color {
        switch gapStateKind(gap.state) {
        case .entering: return .gray.opacity(0.5)
        case .success: return .green
        case .error: return .red
        }
    }

    private var textColor: Color {
        switch gapStateKind(gap.state) {
        case .entering: return .primary.opacity(0.8)
        case .success: return .green
        case .error: return .red
        }
    }

    private var fieldWidth: CGFloat {
        // Approximate width: max(answer, input, 5) * 10pt
        let chars = max(gap.answer.count, gap.input.count, 5)
        return CGFloat(chars) * 10.0
    }
}

private enum GapStateKind { case entering, success, error }

// The KMP state may be bridged as nested classes; switch by type name safely.
private func gapStateKind(_ state: Any?) -> GapStateKind {
    let name = String(reflecting: type(of: state as Any))
    if name.contains("Success") { return .success }
    if name.contains("Error") { return .error }
    return .entering
}


private struct FlowRowsLayout: Layout {
    let hSpacing: CGFloat
    let vSpacing: CGFloat

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let maxWidth = proposal.width ?? .infinity
        var x: CGFloat = 0
        var y: CGFloat = 0
        var rowHeight: CGFloat = 0

        for sub in subviews {
            let size = sub.sizeThatFits(.unspecified)
            if x + size.width > maxWidth {
                // new line
                x = 0
                y += rowHeight + vSpacing
                rowHeight = 0
            }
            rowHeight = max(rowHeight, size.height)
            x += size.width + hSpacing
        }
        return CGSize(width: maxWidth, height: y + rowHeight)
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let maxWidth = bounds.width
        var x: CGFloat = bounds.minX
        var y: CGFloat = bounds.minY
        var rowHeight: CGFloat = 0

        for sub in subviews {
            let size = sub.sizeThatFits(.unspecified)
            if x + size.width > bounds.minX + maxWidth {
                // new line
                x = bounds.minX
                y += rowHeight + vSpacing
                rowHeight = 0
            }
            sub.place(
                at: CGPoint(x: x, y: y),
                proposal: ProposedViewSize(width: size.width, height: size.height)
            )
            rowHeight = max(rowHeight, size.height)
            x += size.width + hSpacing
        }
    }
}
