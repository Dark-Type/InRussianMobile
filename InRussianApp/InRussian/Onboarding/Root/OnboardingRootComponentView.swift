//
//  OnboardingRootComponentView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared

struct OnboardingRootComponentView: View {
    @StateValue
    var stack: ChildStack<AnyObject, OnboardingRootComponentChild>

    init(component: OnboardingRootComponent) {
        _stack = StateValue(component.stack)
    }

    var body: some View {
        StackView(
            stackValue: _stack,
            getTitle: { child in
                switch child {
                case is OnboardingRootComponentChild.InteractiveOnboardingChild: return "Интерактив"
                default: return "Onboarding"
                }
            },
            onBack: { _ in },
            childContent: { child in
                if let interactive = child as? OnboardingRootComponentChild.InteractiveOnboardingChild {
                    InteractiveOnboardingComponentView(component: interactive.component)
                } else {
                    Text("Unknown onboarding step")
                }
            }
        )
    }
}
