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

    var isRegisterActive: Bool {
        !email.isEmpty && !password.isEmpty && password == confirmPassword
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

                    OutlinedTextfield(
                        text: $confirmPassword,
                        placeholder: "Подтвердите пароль",
                        isSecure: true
                    )

                    CustomButton(
                        text: "Зарегистрироваться",
                        color: AppColors.Palette.accent.color,
                        isActive: isRegisterActive
                    ) {
                        component.onRegister(email: email, password: password)
                    }
                    .padding(.top, 36)
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
