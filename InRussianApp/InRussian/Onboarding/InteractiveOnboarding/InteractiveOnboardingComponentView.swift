//
//  InteractiveOnboardingComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//
import SwiftUI
import Shared

struct InteractiveOnboardingComponentView: View {
    let component: InteractiveOnboardingComponent

    var body: some View {
        VStack(alignment: .center, spacing: 24) {
            Text("Интерактивный онбординг")
                .font(.title3)
                .multilineTextAlignment(.center)
            Button(action: {
                component.onFinish()
            }) {
                Text("Завершить")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
        }
        .padding(16)
    }
}
