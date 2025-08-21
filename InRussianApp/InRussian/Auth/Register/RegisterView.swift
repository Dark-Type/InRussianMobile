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

    @State private var email: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""

    init(component: RegisterComponent) {
        self.component = component
        _email = State(initialValue: component.state.value.email)
        _password = State(initialValue: component.state.value.password)
        _confirmPassword = State(initialValue: component.state.value.confirmPassword)
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
                    VStack(alignment: .leading, spacing: 4) {
                       
                            OutlinedTextfield(
                                text: $email,
                                placeholder: "Электронная почта",
                                isSecure: false
                            )
                            .disabled(component.state.value.loading)
                            
                        
                        
                        if let emailError = component.state.value.emailError {
                            Text(emailError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    VStack(alignment: .leading, spacing: 4) {
                       
                            OutlinedTextfield(
                                text: $password,
                                placeholder: "Пароль",
                                isSecure: !component.state.value.showPassword
                            )
                            .disabled(component.state.value.loading)
                            
                            
                        
                        
                        if let passwordError = component.state.value.passwordError {
                            Text(passwordError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    VStack(alignment: .leading, spacing: 4) {
                     
                            OutlinedTextfield(
                                text: $confirmPassword,
                                placeholder: "Подтвердите пароль",
                                isSecure: !component.state.value.showConfirmPassword
                            )
                            .disabled(component.state.value.loading)
                            
                        
                        
                        if let confirmPasswordError = component.state.value.confirmPasswordError {
                            Text(confirmPasswordError)
                                .font(.caption)
                                .foregroundColor(.red)
                                .padding(.leading, 16)
                        }
                    }

                    CustomButton(
                        text: "Зарегистрироваться",
                        color: AppColors.Palette.accent.color,
                        isActive: component.state.value.isButtonActive && !component.state.value.loading
                    ) {
                        component.onRegister(email: email, password: password)
                    }
                    .padding(.top, 36)
                    .disabled(component.state.value.loading)

                    if component.state.value.loading {
                        ProgressView()
                            .scaleEffect(1.2)
                            .padding(.top, 16)
                    }
                }
                .padding(.horizontal, 28)
            }
        }
        .onChange(of: email) { newValue in
            component.changeEmail(email: newValue)
        }
        .onChange(of: password) { newValue in
            component.changePassword(password: newValue)
        }
        .onChange(of: confirmPassword) { newValue in
            component.changeConfirmPassword(confirmPassword: newValue)
        }
        .onChange(of: component.state.value.email) { newValue in
            if email != newValue {
                email = newValue
            }
        }
        .onChange(of: component.state.value.password) { newValue in
            if password != newValue {
                password = newValue
            }
        }
        .onChange(of: component.state.value.confirmPassword) { newValue in
            if confirmPassword != newValue {
                confirmPassword = newValue
            }
        }
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    component.onBackClicked()
                }) {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("Назад")
                    }
                    .foregroundColor(AppColors.Palette.accent.color)
                }
                .disabled(component.state.value.loading)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
