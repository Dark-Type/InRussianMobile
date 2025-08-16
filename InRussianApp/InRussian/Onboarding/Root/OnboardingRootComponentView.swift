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
                case is OnboardingRootComponentChild.LanguageChild: return "Язык"
                case is OnboardingRootComponentChild.PersonalDataChild: return "Данные"
                case is OnboardingRootComponentChild.CitizenshipChild: return "Гражданство"
                case is OnboardingRootComponentChild.EducationChild: return "Образование"
                case is OnboardingRootComponentChild.ConfirmationChild: return "Подтверждение"
                case is OnboardingRootComponentChild.InteractiveOnboardingChild: return "Интерактив"
                default: return "Onboarding"
                }
            },
            onBack: { _ in },
            childContent: { child in
                if let language = child as? OnboardingRootComponentChild.LanguageChild {
                    LanguageComponentView(component: language.component)
                } else if let personalData = child as? OnboardingRootComponentChild.PersonalDataChild {
                    PersonalDataComponentView(component: personalData.component)
                } else if let citizenship = child as? OnboardingRootComponentChild.CitizenshipChild {
                    CitizenshipComponentView(component: citizenship.component)
                } else if let education = child as? OnboardingRootComponentChild.EducationChild {
                    EducationComponentView(component: education.component)
                } else if let confirmation = child as? OnboardingRootComponentChild.ConfirmationChild {
                    ConfirmationComponentView(component: confirmation.component)
                } else if let interactive = child as? OnboardingRootComponentChild.InteractiveOnboardingChild {
                    InteractiveOnboardingComponentView(component: interactive.component)
                } else {
                    Text("Unknown onboarding step")
                }
            }
        )
    }
}
