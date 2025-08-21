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
    @StateValue private var state: LoginStoreState

    init(component: LoginComponent) {
        self.component = component

        self._state = StateValue(component.state)
    }

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
                    HStack {
                        OutlinedTextfield(
                            text: Binding(
                                get: { state.email },
                                set: { component.onEmailChange(email: $0) }
                            ),
                            placeholder: "Электронная почта",
                            isSecure: false
                        )

                        if !state.email.isEmpty {
                            Button(action: {
                                component.onDeleteEmailClick()
                            }) {
                                Image(systemName: "xmark.circle.fill")
                                    .foregroundColor(AppColors.Palette.footnote.color)
                            }
                            .buttonStyle(.plain)
                        }
                    }

                    if let emailError = state.emailError {
                        Text(emailError)
                            .font(.caption)
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }

                    HStack {
                        OutlinedTextfield(
                            text: Binding(
                                get: { state.password },
                                set: { component.onPasswordChange(password: $0) }
                            ),
                            placeholder: "Пароль",
                            isSecure: !state.showPassword
                        )

//                        Button(action: {
//                            component.onShowPasswordClick()
//                        }) {
//                            Image(systemName: state.showPassword ? "eye.slash" : "eye")
//                                .foregroundColor(AppColors.Palette.footnote.color)
//                        }
//                        .buttonStyle(.plain)
                    }

                    if let passwordError = state.passwordError {
                        Text(passwordError)
                            .font(.caption)
                            .foregroundColor(.red)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }

                    CustomButton(
                        text: state.loading ? "Вход..." : "Войти",
                        color: AppColors.Palette.accent.color
                    ) {
                        component.onLogin(email: state.email, password: state.password)
                    }
                    .padding(.top, 36)
                    .disabled(!state.isButtonActive || state.loading)

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
                    .disabled(state.loading)
                }
                .padding(.horizontal, 28)
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    component.onBackClicked()
                }) {
                    HStack(spacing: 4) {
                        Image(systemName: "chevron.left")
                            .foregroundColor(AppColors.Palette.accent.color)
                        Text("Назад")
                            .foregroundColor(AppColors.Palette.accent.color)
                    }
                }
                .disabled(state.loading)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
