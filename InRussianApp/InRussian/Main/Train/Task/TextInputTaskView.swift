//
//  TextInputTaskView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//


import SwiftUI


//struct TextInputTaskView: View {
//    let component: Any
//    let onSetOnEvent: (((() -> Void)?) -> Void)
//
//    @State private var blocks: [TextBlock] = TextBlock.sample
//
//    var body: some View {
//        MainView(
//            blocks: $blocks, // Pass as binding for mutation in children
//            tokensFor: tokens(for:block:), // Pass the token generator
//            secondaryBackground: { Color.gray.opacity(0.1) },
//            onSetOnEvent: onSetOnEvent,
//            validateAll: validateAll
//        )
//        .background(secondaryBackground().ignoresSafeArea())
//        .onAppear {
//            onSetOnEvent {
//                // TODO: wire to your real component: component.onContinueClick()
//                validateAll()
//                UIImpactFeedbackGenerator(style: .light).impactOccurred()
//            }
//        }
//    }
//
//    private func tokens(for blockIndex: Int, block: TextBlock) -> [FlowToken] {
//        let words = block.words
//        let sortedGaps = block.gaps.sorted(by: { $0.pos < $1.pos })
//        var currentWordIndex = 0
//        var tokens: [FlowToken] = []
//
//        func wordToken(_ s: String) {
//            tokens.append(.text(s))
//        }
//
//        for gap in sortedGaps {
//            while currentWordIndex < gap.pos && currentWordIndex < words.count {
//                wordToken(words[currentWordIndex] + " ")
//                currentWordIndex += 1
//            }
//
//            // Insert gap field
//            if let gapIndex = blocks[blockIndex].gaps.firstIndex(where: { $0.id == gap.id }) {
//                tokens.append(.gap(
//                    GapBinding(
//                        id: gap.id,
//                        answer: blocks[blockIndex].gaps[gapIndex].answer,
//                        state: blocks[blockIndex].gaps[gapIndex].state,
//                        text: Binding(
//                            get: { blocks[blockIndex].gaps[gapIndex].input },
//                            set: { newValue in
//                                blocks[blockIndex].gaps[gapIndex].input = newValue
//                                blocks[blockIndex].gaps[gapIndex].state = updatedState(
//                                    input: newValue,
//                                    answer: blocks[blockIndex].gaps[gapIndex].answer
//                                )
//                                // TODO: forward to KMM: component.onTextChange(blockIndex, gap.id, newValue)
//                            }
//                        )
//                    )
//                ))
//            }
//
//            if currentWordIndex < words.count {
//                wordToken(" " + words[currentWordIndex] + " ")
//                currentWordIndex += 1
//            }
//        }
//
//        while currentWordIndex < words.count {
//            wordToken(words[currentWordIndex] + " ")
//            currentWordIndex += 1
//        }
//
//        return tokens
//    }
//
//    private func updatedState(input: String, answer: String) -> GapState {
//        if input.isEmpty { return .entering }
//        if input.caseInsensitiveCompare(answer) == .orderedSame { return .success }
//        return input.count >= answer.count ? .error : .entering
//    }
//
//    private func validateAll() {
//        for bi in blocks.indices {
//            for gi in blocks[bi].gaps.indices {
//                let g = blocks[bi].gaps[gi]
//                blocks[bi].gaps[gi].state = updatedState(input: g.input, answer: g.answer)
//            }
//        }
//    }
//    
//
//    private func secondaryBackground() -> Color { Color.gray.opacity(0.1) }
//}
//
//fileprivate struct MainView: View {
//    @Binding var blocks: [TextBlock]
//    let tokensFor: (Int, TextBlock) -> [FlowToken]
//    var secondaryBackground: () -> Color = { Color.gray.opacity(0.1) }
//    var onSetOnEvent: (@escaping () -> Void) -> Void = { _ in }
//    var validateAll: () -> Void = {}
//
//    var body: some View {
//        ScrollView {
//            LazyVStack(spacing: 12) {
//                ForEach(Array(blocks.enumerated()), id: \.element.id) { (blockIndex, block) in
//                    BlockView(
//                        block: block,
//                        tokens: tokensFor(blockIndex, block)
//                    )
//                }
//                Spacer(minLength: 24)
//            }
//            .padding(.bottom, 16)
//        }
//        .background(secondaryBackground().ignoresSafeArea())
//        .onAppear {
//            onSetOnEvent {
//                validateAll()
//                UIImpactFeedbackGenerator(style: .light).impactOccurred()
//            }
//        }
//    }
//}
//
//fileprivate struct BlockView: View {
//    let block: TextBlock
//    let tokens: [FlowToken]
//
//    var body: some View {
//        VStack(alignment: .leading, spacing: 8) {
//            Text(block.label)
//                .font(.system(size: 20, weight: .semibold))
//
//            VStack(alignment: .leading, spacing: 0) {
//                FlowTextWithGaps(
//                    tokens: tokens,
//                    font: .preferredFont(forTextStyle: .body)
//                )
//                .padding(.top, 2)
//            }
//            .padding(16)
//            .background(
//                RoundedRectangle(cornerRadius: 12, style: .continuous)
//                    .fill(Color.white)
//            )
//            .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
//        }
//        .padding(.horizontal, 16)
//        .padding(.top, 8)
//    }
//}
//
//// MARK: - Flow Text With Gaps
//
//private enum FlowToken: Identifiable, Equatable {
//    case text(String)
//    case gap(GapBinding)
//
//    var id: String {
//        switch self {
//        case .text(let s): return "t:\(s.hashValue)"
//        case .gap(let g): return "g:\(g.id)"
//        }
//    }
//}
//
//private struct GapBinding: Equatable {
//    let id: String
//    let answer: String
//    var state: GapState
//    var text: Binding<String>
//    static func == (lhs: Self, rhs: Self) -> Bool {
//        lhs.id == rhs.id
//    }
//}
//
//
//private enum GapState: Equatable {
//    case entering
//    case success
//    case error
//}
//
//private struct FlowTextWithGaps: View {
//    let tokens: [FlowToken]
//    let font: UIFont
//
//    init(tokens: [FlowToken], font: UIFont = .systemFont(ofSize: 15, weight: .medium)) {
//        self.tokens = tokens
//        self.font = font
//    }
//
//    var body: some View {
//        FlowLayout(spacing: 4, runSpacing: 6) {
//            ForEach(tokens) { token in
//                switch token {
//                case .text(let s):
//                    Text(s)
//                        .font(.system(size: font.pointSize, weight: .medium))
//                        .foregroundColor(Color(.darkGray).opacity(0.7))
//                        .fixedSize() 
//
//                case .gap(let binding):
//                    GapField(binding: binding, font: font)
//                        .fixedSize()
//                }
//            }
//        }
//    }
//}
//
//private struct GapField: View {
//    let binding: GapBinding
//    let font: UIFont
//
//    private var uiColorBorder: UIColor {
//        switch binding.state {
//        case .entering: return UIColor.darkGray.withAlphaComponent(0.5)
//        case .success:  return UIColor.systemGreen
//        case .error:    return UIColor.systemRed
//        }
//    }
//
//    private var uiColorText: UIColor {
//        switch binding.state {
//        case .entering: return UIColor.darkGray.withAlphaComponent(0.8)
//        case .success:  return UIColor.systemGreen
//        case .error:    return UIColor.systemRed
//        }
//    }
//
//    var body: some View {
//        let width = gapWidth(chars: max(max(binding.answer.count, binding.text.wrappedValue.count), 5), font: font)
//
//        TextField("", text: binding.text)
//            .textFieldStyle(.plain)
//            .font(.system(size: font.pointSize, weight: .medium))
//            .foregroundColor(Color(uiColorText))
//            .padding(.horizontal, 6)
//            .frame(width: width, height: 28)
//            .background(Color.white)
//            .overlay(
//                RoundedRectangle(cornerRadius: 4, style: .continuous)
//                    .stroke(Color(uiColorBorder), lineWidth: 1)
//            )
//            .submitLabel(.done)
//            .disableAutocorrection(true)
//            .textInputAutocapitalization(.none)
//    }
//
//    private func gapWidth(chars: Int, font: UIFont) -> CGFloat {
//        let charSample = "W" as NSString
//        let sampleWidth = charSample.size(withAttributes: [.font: font]).width
//        return max(CGFloat(chars) * sampleWidth * 0.9, 50) // minimum width ~50pt
//    }
//}
//
//
//
//// MARK: - Models (replace with your KMM-bound models)
//
//private struct TextBlock: Identifiable, Equatable {
//    let id: UUID = UUID()
//    var label: String
//    var words: [String]
//    var gaps: [Gap]
//
//    struct Gap: Identifiable, Equatable {
//        let id: String
//        let pos: Int // position within words
//        let answer: String
//        var input: String
//        var state: GapState
//    }
//
//    static let sample: [TextBlock] = [
//        TextBlock(
//            label: "Fill in the blanks:",
//            words: ["I", "am", "learning", "SwiftUI", "and", "it", "is", "fun", "!"],
//            gaps: [
//                .init(id: "g1", pos: 3, answer: "SwiftUI", input: "", state: .entering),
//                .init(id: "g2", pos: 7, answer: "fun", input: "", state: .entering)
//            ]
//        ),
//        TextBlock(
//            label: "Complete the sentence:",
//            words: ["Kotlin", "and", "Swift", "are", "both", "modern", "languages", "."],
//            gaps: [
//                .init(id: "g3", pos: 0, answer: "Kotlin", input: "", state: .entering),
//                .init(id: "g4", pos: 2, answer: "Swift", input: "", state: .entering)
//            ]
//        )
//    ]
//}
//
//// MARK: - Color helpers (fallbacks)
//
//private func secondaryBackground() -> Color {
//    if let color = UIColor(named: "SecondaryBackground") { return Color(color) }
//    return Color(.secondarySystemBackground)
//}
