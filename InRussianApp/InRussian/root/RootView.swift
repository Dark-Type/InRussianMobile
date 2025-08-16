//
//  RootView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct RootView: View {
    @StateValue
    var stack: ChildStack<AnyObject, RootComponentChild>

    init(_ component: RootComponent) {
        
        _stack = StateValue(component.stack)
    }

    var body: some View {
        
        StackView(
            stackValue: _stack,
            getTitle: { child in
                
                switch child {
                case is RootComponentChild.AuthChild: return "Auth"
                case is RootComponentChild.OnboardingChild: return "Onboarding"
                case is RootComponentChild.MainChild: return "Main"
                default: return "Unknown"
                }
            },
            onBack: { toIndex in
                
            },
            childContent: { child in

                if let authChild = child as? RootComponentChild.AuthChild {
                    AuthRootComponentView(component: authChild.component)
                } else if let onboardingChild = child as? RootComponentChild.OnboardingChild {
                    OnboardingRootComponentView(component: onboardingChild.component)
                } else if let mainChild = child as? RootComponentChild.MainChild {
                    MainRootComponentView(component: mainChild.component)
                } else {
                    Text("Unknown child")
                }
            }
        )
    }
}
