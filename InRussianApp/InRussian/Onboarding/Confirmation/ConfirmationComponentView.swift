//
//  ConfirmationComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared

struct ConfirmationComponentView: View {
    let component: ConfirmationComponent

    var body: some View {
        VStack(alignment: .center, spacing: 24) {
            Text("Данные успешно переданы!")
                .font(.title3)
                .multilineTextAlignment(.center)
            Button(action: {
                component.onConfirm()
            }) {
                Text("Продолжить")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
        }
        .padding(16)
    }
}
