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

    @ObservedObject private var observedState: ObservableValue<UpdatePasswordState>

    init(component: UpdatePasswordComponent) {
        self.component = component
        self.observedState = ObservableValue(component.state)
    }

    private var state: UpdatePasswordState { observedState.value }

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
                                get: { state.password },
                                set: { component.onPasswordChange(password: $0) }
                            ),
                            placeholder: "Пароль",
                            isSecure: !state.showPassword,
                            onToggleSecure: { component.onShowPasswordClick() },
                            submitLabel: .next,
                            onSubmit: nil,
                            textContentType: .password,
                            keyboardType: .default
                        )
                        .disabled(state.isLoading)

                        if let passwordError = state.confirmPasswordError {
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
                                set: { component.onConfirmPasswordChange(confirmPassword: $0) }
                            ),
                            placeholder: "Подтвердите пароль",
                            isSecure: !state.showConfirmPassword,
                            onToggleSecure: { component.onShowConfirmPasswordClick() },
                            submitLabel: .done,
                            onSubmit: {
                                if state.updateButtonEnable && !state.isLoading {
                                    component.onPasswordUpdated()
                                }
                            },
                            textContentType: .password,
                            keyboardType: .default
                        )
                        .disabled(state.isLoading)

                        if let confirmPasswordError = state.confirmPasswordError {
                            Text(confirmPasswordError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    CustomButton(
                        text: "Обновить пароль",
                        color: AppColors.Palette.accent.color,
                        isActive: state.updateButtonEnable && !state.isLoading
                    ) {
                        component.onPasswordUpdated()
                    }
                    .disabled(!state.updateButtonEnable || state.isLoading)
                    .padding(.top, 36)

                    if state.isLoading {
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
                .disabled(state.isLoading)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
