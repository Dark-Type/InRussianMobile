//
//  TextInputWithVariantTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//
import Shared
import SwiftUI

fileprivate func TIVLog(_ msg: String) {

    print("[TextInputVariant] \(msg)")

}

struct TextInputWithVariantTaskView: View {
    let component: any TextInputTaskWithVariantComponent
    let onContinueClick: (@escaping () -> Void) -> Void

    @StateValue private var state: TextInputTaskWithVariantComponentState
    // Local echo of user selections so the UI updates immediately (even before state returns)
    @State private var localSelections: [String: String] = [:]

    init(component: any TextInputTaskWithVariantComponent,
         onContinueClick: @escaping (@escaping () -> Void) -> Void) {
        self.component = component
        self.onContinueClick = onContinueClick
        _state = StateValue(component.state)
    }

    var body: some View {
        Group {
            if let block = state.blocks {
                content(block: block)
            } else {
                EmptyView()
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
        .onAppear {
            TIVLog("onAppear")
            dumpState(state)
            onContinueClick {
                TIVLog("onContinueClick() forwarded to component")
                component.onContinueClick()
            }
        }
        // When state updates, prefer server truth and clear echoed overrides that are now applied
        .onChange(of: state.hash()) { _ in
            TIVLog("state.hash() changed")
            dumpState(state)
            guard let block = state.blocks else { return }
            var cleaned = localSelections
            for g in block.gaps where !g.selected.isEmpty {
                if cleaned[g.id] != nil {
                    TIVLog("Clearing local echo for gap \(g.id) -> server selected='\(g.selected)'")
                    cleaned[g.id] = nil
                }
            }
            localSelections = cleaned
        }
    }

    @ViewBuilder
    private func content(block: TextInputTaskWithVariantComponentInputBlock) -> some View {
        VStack(spacing: 0) {
            VStack(spacing: 16) {
                Text(block.label)
                    .font(.system(size: 20, weight: .semibold))
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: .infinity)

                FlowTextWithVariant(
                    words: block.words,
                    gaps: block.gaps.sorted(by: { Int($0.pos) < Int($1.pos) }),
                    localSelections: $localSelections,
                    onVariantSelected: { gapId, variant in
                        // Update local echo so UI immediately shows the user's pick
                        TIVLog("onVariantSelected UI: gapId=\(gapId), variant='\(variant)' (updating local echo and notifying component)")
                        localSelections[gapId] = variant
                        // Single-block task; pass index 0 like Android does.
                        component.onVariantSelected(blockIndex: 0, gapId: gapId, selectedVariant: variant)
                    }
                )
                .padding(.horizontal, 10)
                .padding(.vertical, 16)
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
            .padding(12)

            Spacer(minLength: 0)
        }
        .onAppear {
            TIVLog("content(block:) appear with \(block.words.count) words, \(block.gaps.count) gaps")
        }
    }

    // MARK: - Debug helpers

    private func dumpState(_ s: TextInputTaskWithVariantComponentState) {
        TIVLog("Dump state: isButtonEnabled=\(s.isButtonEnabled), hasBlock=\(s.blocks != nil)")
        guard let b = s.blocks else { return }
        TIVLog("Block: label='\(b.label)', words=\(b.words.count), gaps=\(b.gaps.count)")
        for (idx, g) in b.gaps.enumerated() {
            TIVLog("  Gap[\(idx)] id=\(g.id) pos=\(g.pos) selected='\(g.selected)' answer='\(g.answer)' state=\(gapStateKind(g.state)) variants=\(g.variants.count)")
        }
    }
}

// MARK: - Flow text with inline selectable gaps

private struct FlowTextWithVariant: View {
    let words: [String]
    let gaps: [TextInputTaskWithVariantComponentInputGap]
    @Binding var localSelections: [String: String]
    let onVariantSelected: (String, String) -> Void

    private var flowItems: [FlowItem] {
        var items: [FlowItem] = []
        var gapIdx = 0

        for i in 0 ..< words.count {
            if gapIdx < gaps.count, Int(gaps[gapIdx].pos) == i {
                items.append(.gap(gaps[gapIdx]))
                gapIdx += 1
            }
            items.append(.word(words[i]))
        }
        return items
    }

    var body: some View {
        WrapLayout(spacing: 6, runSpacing: 10) {
            ForEach(Array(flowItems.enumerated()), id: \.offset) { idx, item in
                switch item {
                case .word(let word):
                    Text(word + (idx == flowItems.count - 1 ? "" : " "))
                        .font(.system(size: 15, weight: .medium))
                        .foregroundColor(.primary.opacity(0.7))

                case .gap(let gap):
                    // Merge current selection (local echo first, then state)
                    let currentSelection = localSelections[gap.id] ?? gap.selected
                    InlineGapMenu(
                        gap: gap,
                        currentSelection: currentSelection,
                        onPick: { variant in
                            TIVLog("User picked variant '\(variant)' for gap \(gap.id)")
                            onVariantSelected(gap.id, variant)
                        }
                    )
                    .onAppear {
                        TIVLog("Render Gap \(gap.id): currentSelection='\(currentSelection)', state=\(gapStateKind(gap.state))")
                    }
                    .onChange(of: localSelections[gap.id]) { _ in
                        let sel = localSelections[gap.id] ?? gap.selected
                        TIVLog("Gap \(gap.id) local selection changed -> '\(sel)'")
                    }
                }
            }
        }
        .accessibilityElement(children: .contain)
    }

    enum FlowItem {
        case word(String)
        case gap(TextInputTaskWithVariantComponentInputGap)
    }
}

// MARK: - iOS-native dropdown using Menu (best practice)

private struct InlineGapMenu: View {
    let gap: TextInputTaskWithVariantComponentInputGap
    let currentSelection: String
    let onPick: (String) -> Void

    // Treat empty selection as Entering visually, even if backend state came as Success by default
    private var effectiveState: GapStateKind {
        if currentSelection.isEmpty { return .entering }
        return gapStateKind(gap.state)
    }

    private var borderColor: Color {
        switch effectiveState {
        case .entering: .gray.opacity(0.5)
        case .success: .green
        case .error: .red
        }
    }

    private var textColor: Color {
        switch effectiveState {
        case .entering: currentSelection.isEmpty ? .secondary : .primary
        case .success: .green
        case .error: .red
        }
    }

    private var displayText: String {
        currentSelection.isEmpty ? "Select" : currentSelection
    }

    var body: some View {
        Menu {
            ForEach(gap.variants, id: \.self) { variant in
                Button {
                    TIVLog("Menu pick gap \(gap.id): '\(variant)'")
                    onPick(variant)
                } label: {
                    HStack {
                        Text(variant)
                        Spacer(minLength: 0)
                        if (!currentSelection.isEmpty && currentSelection == variant)
                            || (!gap.selected.isEmpty && gap.selected == variant) {
                            Image(systemName: "checkmark")
                        }
                    }
                }
            }
        } label: {
            HStack(spacing: 6) {
                Text(displayText)
                    .font(.system(size: 15, weight: .medium))
                    .foregroundColor(textColor)
                    .baselineOffset(0)
                Image(systemName: "chevron.down")
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundColor(.secondary)
                    .opacity(0.8)
                    .alignmentGuide(.firstTextBaseline) { d in d[.bottom] }
            }
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(
                RoundedRectangle(cornerRadius: 6, style: .continuous)
                    .fill(Color.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 6, style: .continuous)
                    .stroke(borderColor, lineWidth: 1)
            )
            // Nudge slightly up to better align with surrounding baseline
            .offset(y: -1)
        }
        // Ensure this inline control aligns nicely with text baseline
        .alignmentGuide(.firstTextBaseline) { d in d[.firstTextBaseline] }
        .accessibilityLabel(displayText)
        .accessibilityHint("Double tap to choose an option")
    }
}

// Interpret KMP sealed GapState (Entering/Success/Error) via type name
private enum GapStateKind { case entering, success, error }
private func gapStateKind(_ state: Any?) -> GapStateKind {
    let name = String(reflecting: type(of: state as Any))
    if name.localizedCaseInsensitiveContains("Success") { return .success }
    if name.localizedCaseInsensitiveContains("Error") { return .error }
    return .entering
}

// MARK: - Simple wrap layout (row items centered vertically for better inline alignment)

private struct WrapLayout<Content: View>: View {
    let spacing: CGFloat
    let runSpacing: CGFloat
    @ViewBuilder let content: () -> Content

    init(spacing: CGFloat = 6, runSpacing: CGFloat = 10, @ViewBuilder content: @escaping () -> Content) {
        self.spacing = spacing
        self.runSpacing = runSpacing
        self.content = content
    }

    var body: some View {
        _WrapRowsLayout(spacing: spacing, runSpacing: runSpacing) { content() }
    }
}

private struct _WrapRowsLayout: Layout {
    let spacing: CGFloat
    let runSpacing: CGFloat

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let maxWidth = proposal.width ?? .infinity
        var x: CGFloat = 0
        var y: CGFloat = 0
        var rowHeight: CGFloat = 0

        for sub in subviews {
            let size = sub.sizeThatFits(.unspecified)
            if x + size.width > maxWidth {
                x = 0
                y += rowHeight + runSpacing
                rowHeight = 0
            }
            rowHeight = max(rowHeight, size.height)
            x += size.width + spacing
        }
        return CGSize(width: maxWidth, height: y + rowHeight)
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let maxWidth = bounds.width
        var x: CGFloat = bounds.minX
        var y: CGFloat = bounds.minY
        var rowHeight: CGFloat = 0
        var rowStartIndex = 0

        func placeRow(from start: Int, to end: Int, rowHeight: CGFloat, y: CGFloat) {
            var xCursor = bounds.minX
            for i in start..<end {
                let sub = subviews[i]
                let size = sub.sizeThatFits(.unspecified)
                let yOffset = y + (rowHeight - size.height) / 2
                sub.place(
                    at: CGPoint(x: xCursor, y: yOffset),
                    proposal: ProposedViewSize(width: size.width, height: size.height)
                )
                xCursor += size.width + spacing
            }
        }

        for (index, sub) in subviews.enumerated() {
            let size = sub.sizeThatFits(.unspecified)
            if x + size.width > bounds.minX + maxWidth {
                placeRow(from: rowStartIndex, to: index, rowHeight: rowHeight, y: y)
                x = bounds.minX
                y += rowHeight + runSpacing
                rowHeight = 0
                rowStartIndex = index
            }
            rowHeight = max(rowHeight, size.height)
            x += size.width + spacing
        }
        placeRow(from: rowStartIndex, to: subviews.count, rowHeight: rowHeight, y: y)
    }
}
