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
    @State private var showSupportCover: Bool = false

    private var canUpdate: Bool {
        !password.isEmpty && password == confirmPassword
    }

    var body: some View {
        ZStack {
            AppColors.Palette.baseBackground.color
                .ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer(minLength: 0)

                AppImages.image(for: AppImages.Recovery.recovery)
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: 200, maxHeight: 200)
                    .padding(.bottom, 24)

                Text("Восстановление пароля")
                    .font(.title2.bold())
                    .foregroundColor(AppColors.Palette.fontCaptive.color)
                    .padding(.bottom, 8)

                Spacer()

                Text("Пароль успешно сброшен, выберете новый пароль")
                    .font(.footnote)
                    .foregroundColor(AppColors.Palette.footnote.color)
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 16)
                    .padding(.horizontal, 10)

                OutlinedTextfield(
                    text: $password,
                    placeholder: "Новый пароль",
                    isSecure: true
                )
                .padding(.bottom, 12)

                OutlinedTextfield(
                    text: $confirmPassword,
                    placeholder: "Подтвердите пароль",
                    isSecure: true
                )
                Spacer()

                CustomButton(
                    text: "Обновить пароль",
                    color: AppColors.Palette.accent.color,
                    isActive: canUpdate
                ) {
                    component.onPasswordUpdated()
                }
                .padding(.bottom, 24)
            }
            .padding(.horizontal, 28)

        }
        .navigationBarBackButtonHidden(true)
    }
}
