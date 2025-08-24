//
//  RegisterView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct RegisterView: View {
    let component: RegisterComponent

    @ObservedObject private var observedState: ObservableValue<RegisterStoreState>

    init(component: RegisterComponent) {
        self.component = component
        self.observedState = ObservableValue(component.state)
    }

    private var state: RegisterStoreState { observedState.value }

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
                    VStack(alignment: .leading, spacing: 4) {
                        OutlinedTextfield(
                            text: Binding(
                                get: { state.email },
                                set: { component.changeEmail(email: $0) }
                            ),
                            placeholder: "Электронная почта",
                            isSecure: false,
                            onToggleSecure: nil,
                            submitLabel: .next,
                            onSubmit: nil,
                            textContentType: .emailAddress,
                            keyboardType: .emailAddress
                        )
                        .disabled(state.loading)

                        if let emailError = state.emailError {
                            Text(emailError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    VStack(alignment: .leading, spacing: 4) {
                        OutlinedTextfield(
                            text: Binding(
                                get: { state.password },
                                set: { component.changePassword(password: $0) }
                            ),
                            placeholder: "Пароль",
                            isSecure: !state.showPassword,
                            onToggleSecure: { component.onShowPasswordClick() },
                            submitLabel: .next,
                            onSubmit: nil,
                            textContentType: .password,
                            keyboardType: .default
                        )
                        .disabled(state.loading)

                        if let passwordError = state.passwordError {
                            Text(passwordError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    VStack(alignment: .leading, spacing: 4) {
                        OutlinedTextfield(
                            text: Binding(
                                get: { state.confirmPassword },
                                set: { component.changeConfirmPassword(confirmPassword: $0) }
                            ),
                            placeholder: "Подтвердите пароль",
                            isSecure: !state.showConfirmPassword,
                            onToggleSecure: { component.onShowConfirmPasswordClick() },
                            submitLabel: .done,
                            onSubmit: {
                                if state.isButtonActive && !state.loading {
                                    component.onRegister(email: state.email, password: state.password)
                                }
                            },
                            textContentType: .password,
                            keyboardType: .default
                        )
                        .disabled(state.loading)

                        if let confirmPasswordError = state.confirmPasswordError {
                            Text(confirmPasswordError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    CustomButton(
                        text: "Зарегистрироваться",
                        color: AppColors.Palette.accent.color,
                        isActive: state.isButtonActive && !state.loading
                    ) {
                        component.onRegister(
                            email: state.email,
                            password: state.password
                        )
                    }
                    .disabled(!state.isButtonActive || state.loading)
                    .padding(.top, 36)

                    if state.loading {
                        ProgressView()
                            .scaleEffect(1.2)
                            .padding(.top, 16)
                    }
                }
                .padding(.horizontal, 28)
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: { component.onBackClicked() }) {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("Назад")
                    }
                    .foregroundColor(AppColors.Palette.accent.color)
                }
                .disabled(state.loading)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
