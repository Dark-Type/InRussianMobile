//
//  EnterRecoveryCodeView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct EnterRecoveryCodeView: View {
    let component: EnterRecoveryCodeComponent

    @State private var code: String = ""

    var body: some View {
        VStack {
            Spacer()
                .frame(maxHeight: .infinity)
                .layoutPriority(1)

            Text("Введите код восстановления")
                .font(.body)
                .foregroundColor(.secondary)

            Spacer().frame(height: 16)

            TextField("Код", text: $code)
                .keyboardType(.default)
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 16)

            Button(action: {
                component.onCodeEntered(code: code)
            }) {
                Text("Далее")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)

            Spacer().frame(height: 8)

            Button(action: {
                component.onBackClicked()
            }) {
                Text("Назад")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
        .padding(16)
    }
}
