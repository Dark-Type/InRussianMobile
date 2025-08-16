//
//  LoginView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct LoginView: View {
    let component: LoginComponent

    @State private var email: String = ""
    @State private var password: String = ""

    var body: some View {
        VStack {
            Spacer()
                .frame(maxHeight: .infinity)
                .layoutPriority(1)

            TextField("Email", text: $email)
                .textContentType(.emailAddress)
                .autocapitalization(.none)
                .keyboardType(.emailAddress)
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)

            SecureField("Пароль", text: $password)
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .background(Color(.secondarySystemBackground))
                .cornerRadius(8)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 16)

            Button(action: {
                component.onLogin(email: email, password: password)
            }) {
                Text("Войти")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)

            Spacer().frame(height: 8)

            Button(action: {
                component.onForgotPasswordClicked()
            }) {
                Text("Забыли пароль?")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.plain)

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
