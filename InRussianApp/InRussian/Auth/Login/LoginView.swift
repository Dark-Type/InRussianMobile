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
        ZStack {
            AppColors.Palette.baseBackground.color
                .ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer(minLength: 0)

                AppImages.Logo.appLogo
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: 400, maxHeight: 250)
                    .padding(.bottom, 36)

                Spacer(minLength: 0)

                VStack(spacing: 16) {
                    OutlinedTextfield(
                        text: $email,
                        placeholder: "Электронная почта",
                        isSecure: false
                    )

                    OutlinedTextfield(
                        text: $password,
                        placeholder: "Пароль",
                        isSecure: true
                    )

                    CustomButton(
                        text: "Войти",
                        color: AppColors.Palette.accent.color
                    ) {
                        component.onLogin(email: email, password: password)
                    }
                    .padding(.top, 36)

                    Button(action: {
                        component.onForgotPasswordClicked()
                    }) {
                        Text("Забыли пароль?")
                            .font(.footnote)
                            .foregroundColor(AppColors.Palette.footnote.color)
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.plain)
                    .padding(.top, 12)
                }
                .padding(.horizontal, 28)
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    component.onBackClicked()
                }) {
                    Image(systemName: "chevron.left")
                        .foregroundColor(AppColors.Palette.accent.color)
                    Text("Назад")
                        .foregroundColor(AppColors.Palette.accent.color)
                }
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
