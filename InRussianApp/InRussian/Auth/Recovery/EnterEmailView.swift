//
//  EnterEmailView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI

import Shared

struct EnterEmailView: View {
    let component: EnterEmailComponent

    @State private var email: String = ""
    @State private var isEmailValid: Bool = false

    private func validate(email: String) -> Bool {
        let pattern = #"^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"#
        return email.range(of: pattern, options: .regularExpression) != nil
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

                Text("Укажите почту, на которую придет код подтверждения")
                    .font(.footnote)
                    .foregroundColor(AppColors.Palette.footnote.color)
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 16)
                    .padding(.trailing, 24)
                    
                    

                OutlinedTextfield(
                    text: $email,
                    placeholder: "Электронная почта",
                    isSecure: false
                )
                .padding(.bottom, 120)

                CustomButton(
                    text: "Далее",
                    color: AppColors.Palette.accent.color,
                    isActive: isEmailValid
                ) {
                    component.onEmailEntered(email: email)
                }
                .padding(.vertical, 24)

            }
            .padding(.horizontal, 28)
        }
        .onChange(of: email) { newValue in
            isEmailValid = validate(email: newValue)
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
