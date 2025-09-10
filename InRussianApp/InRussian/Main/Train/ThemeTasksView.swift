//
//  ThemeTasksView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import Shared
import SwiftUI

struct ThemeTasksView: View {
    let component: ThemeTasksComponent

    @StateValue private var state: TrainStoreState
    @StateValue private var slot: ChildSlot<TaskBodyConfig, any TaskBodyChild>
    @State private var onChildEvent: (() -> Void)?

    init(component: ThemeTasksComponent) {
        self.component = component
        _state = StateValue(component.state)
        _slot = StateValue(component.childSlot)
    }

    private var progress: Double { percentToFraction(state.percent) }

    var body: some View {
        content
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        component.onBack()
                    } label: {
                        Label("Back", systemImage: "chevron.left")
                    }
                    .accessibilityLabel("Back")
                }
            }
            .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
    }

    @ViewBuilder
    private var content: some View {
        if state.isLoading && state.showedTask == nil {
            LoadingStateView()
        } else if state.showedTask == nil {
            EmptyOrFinishedView(
                isFinished: progress >= 0.999,
                onBack: { component.onBack() }
            )
        } else {
            ActiveTaskView(
                themeId: state.showedTask?.themeId ?? component.themeId,
                progress: progress,
                question: state.showedTask?.question ?? "",
                types: (state.showedTask?.types as? [Any]) ?? [],
                bodyChild: slot.child?.instance,
                isCorrect: state.isCorrect as? Bool,
                onSetChildEvent: { onChildEvent = $0 },
                onMarkCorrect: {
                    component.markCorrectAndSubmit()
                    onChildEvent?()
                    UIImpactFeedbackGenerator(style: .light).impactOccurred()
                },
                onMarkIncorrect: {
                    component.markIncorrectAttempt()
                    onChildEvent?()
                    UINotificationFeedbackGenerator().notificationOccurred(.error)
                },
                onContinue: {
                    component.continueAfterCorrect()
                    onChildEvent?()
                    UIImpactFeedbackGenerator(style: .medium).impactOccurred()
                },
                onBack: { component.onBack() }
            )
        }
    }
}

// MARK: - Active Task Screen

private struct ActiveTaskView: View {
    let themeId: String
    let progress: Double
    let question: String
    let types: [Any]
    let bodyChild: (any TaskBodyChild)?
    let isCorrect: Bool?
    let onSetChildEvent: ((() -> Void)?) -> Void

    let onMarkCorrect: () -> Void
    let onMarkIncorrect: () -> Void
    let onContinue: () -> Void
    let onBack: () -> Void

    // Stable identity to ensure SwiftUI refreshes the body child correctly when it changes.
    private var childIdentity: String {
        if let obj = bodyChild as AnyObject? {
            return String(ObjectIdentifier(obj).hashValue)
        }
        return "nil"
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                // Header progress and meta (on screen background)
                VStack(alignment: .leading, spacing: 8) {
                    ProgressView(value: progress)
                        .progressViewStyle(.linear)
                        .tint(.accent)
                        .frame(height: 8)
                        .accessibilityLabel("Progress")
                        .accessibilityValue("\(Int((progress * 100).rounded())) percent")

                    VStack(alignment: .leading, spacing: 6) {
                        Text("Theme: \(themeId)")
                            .font(.footnote)
                            .foregroundStyle(.secondary)
                        Text("Progress: \(Int((progress * 100).rounded()))%")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }
                }

                // THEME BODY CARD: description + child + controls on ONE surface
                ThemeBodyCard(
                    question: question,
                    types: types,
                    childIdentity: childIdentity,
                    bodyChild: bodyChild,
                    isCorrect: isCorrect,
                    onSetChildEvent: onSetChildEvent,
                    onMarkCorrect: onMarkCorrect,
                    onMarkIncorrect: onMarkIncorrect,
                    onContinue: onContinue
                )

                // Secondary action below the card
                Button("Back", action: onBack)
                    .buttonStyle(.bordered)
                    .controlSize(.large)
                    .frame(maxWidth: .infinity)
                    .padding(.top, 8)
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 24)
        }
        .background(AppColors.Palette.secondaryBackground.color.ignoresSafeArea())
    }
}

// MARK: - Theme Body Card (single unified surface)

private struct ThemeBodyCard: View {
    let question: String
    let types: [Any]
    let childIdentity: String
    let bodyChild: (any TaskBodyChild)?
    let isCorrect: Bool?
    let onSetChildEvent: ((() -> Void)?) -> Void
    let onMarkCorrect: () -> Void
    let onMarkIncorrect: () -> Void
    let onContinue: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            // Content-only description (no own background)
            TaskDescriptionView(onInfo: nil, text: question, taskTypes: types)

            // Content-only body child (no own background)
            TaskBodyChildRenderer(child: bodyChild, onSetOnEvent: onSetChildEvent)
                .id(childIdentity)
                .animation(.default, value: childIdentity)

            Divider().background(AppColors.Palette.stroke.color)

            // CONTROLS live INSIDE the theme body card surface
            ControlsInsideCardView(
                isCorrect: isCorrect,
                onMarkCorrect: onMarkCorrect,
                onMarkIncorrect: onMarkIncorrect,
                onContinue: onContinue
            )
        }
        .padding(16)
        // Use view-builder overload; ensure single, uniform surface color
        .background {
            RoundedRectangle(cornerRadius: 12, style: .continuous)
                .fill(AppColors.Palette.componentBackground.color)
        }
        .overlay(
            RoundedRectangle(cornerRadius: 12, style: .continuous)
                .stroke(AppColors.Palette.stroke.color, lineWidth: 1)
        )
        .accessibilityElement(children: .contain)
    }
}

// Controls inside the component card (Continue is INSIDE here)
private struct ControlsInsideCardView: View {
    let isCorrect: Bool?
    let onMarkCorrect: () -> Void
    let onMarkIncorrect: () -> Void
    let onContinue: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            switch isCorrect {
            case nil:
                HStack(spacing: 12) {
                    Button("Check (Mock Correct)", action: onMarkCorrect)
                        .buttonStyle(.borderedProminent)
                        .controlSize(.large)
                        .frame(maxWidth: .infinity)

                    Button("Mark Wrong", action: onMarkIncorrect)
                        .buttonStyle(.bordered)
                        .controlSize(.large)
                        .frame(maxWidth: .infinity)
                }

            case true?:
                VStack(alignment: .leading, spacing: 12) {
                    Text("Correct!")
                        .font(.headline)
                        .foregroundStyle(.tint)
                        .transition(.opacity.combined(with: .move(edge: .top)))

                    Button("Continue", action: onContinue)
                        .buttonStyle(.borderedProminent)
                        .controlSize(.large)
                        .frame(maxWidth: .infinity)
                }

            case false?:
                VStack(alignment: .leading, spacing: 12) {
                    Text("Incorrect, try again.")
                        .font(.headline)
                        .foregroundStyle(.red)
                        .transition(.opacity.combined(with: .move(edge: .top)))

                    HStack(spacing: 12) {
                        Button("Force Correct", action: onMarkCorrect)
                            .buttonStyle(.borderedProminent)
                            .controlSize(.large)
                            .frame(maxWidth: .infinity)

                        Button("Retry Wrong", action: onMarkIncorrect)
                            .buttonStyle(.bordered)
                            .controlSize(.large)
                            .frame(maxWidth: .infinity)
                    }
                }
            }
        }
        .animation(.default, value: isCorrect)
    }
}

// MARK: - States

private struct EmptyOrFinishedView: View {
    let isFinished: Bool
    let onBack: () -> Void

    var body: some View {
        ZStack {
            AppColors.Palette.secondaryBackground.color.ignoresSafeArea()
            VStack(spacing: 16) {
                Text(isFinished ? "Theme Completed!" : "No tasks")
                    .font(.title3).bold()

                Button("Back to Courses", action: onBack)
                    .buttonStyle(.borderedProminent)
            }
            .padding(24)
        }
    }
}

private struct LoadingStateView: View {
    var body: some View {
        ZStack {
            AppColors.Palette.secondaryBackground.color.ignoresSafeArea()
            ProgressView()
                .progressViewStyle(.circular)
                .accessibilityLabel("Loading")
        }
    }
}

// MARK: - Helpers

private func percentToFraction(_ value: Any?) -> Double {
    let raw: Double
    if let d = value as? Double { raw = d }
    else if let f = value as? Float { raw = Double(f) }
    else if let i = value as? Int { raw = Double(i) }
    else if let n = value as? NSNumber { raw = n.doubleValue }
    else { raw = 0.0 }

    if raw.isNaN || raw.isInfinite { return 0.0 }
    return min(max(raw, 0.0), 1.0)
}
