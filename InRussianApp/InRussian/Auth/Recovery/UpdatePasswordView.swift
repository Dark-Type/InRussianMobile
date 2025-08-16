//
//  UpdatePasswordView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct UpdatePasswordView: View {
    let component: UpdatePasswordComponent

    @State private var password: String = ""
    @State private var confirmPassword: String = ""

    var body: some View {
        VStack {
            Text("Введите новый пароль")
                .font(.body)
                .foregroundColor(.secondary)

            Spacer().frame(height: 16)

            SecureField("Новый пароль", text: $password)
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)

            SecureField("Подтвердите пароль", text: $confirmPassword)
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 16)

            Button(action: {
                component.onPasswordUpdated(newPassword: confirmPassword)
            }) {
                Text("Сохранить")
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
