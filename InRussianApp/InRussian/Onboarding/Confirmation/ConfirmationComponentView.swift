//
//  ConfirmationComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct ConfirmationComponentView: View {
    let component: ConfirmationComponent

    var body: some View {
        VStack(spacing: 32) {
            Spacer(minLength: 0)
            Image(systemName: "checkmark.circle.fill")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundColor(Color(.systemGreen))
                .frame(width: 200, height: 200)
                .padding(.top, 48)

            Text("Данные успешно переданы!")
                .font(.largeTitle)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 16)

            Spacer()

            CustomButton(
                text: "Все сохранено",
                color: AppColors.Palette.accent.color,
                isActive: true,
                trailingLogo: nil,
                action: {
                    component.onConfirm()
                }
            )
            .padding(.horizontal, 16)
            .padding(.bottom, 36)
        }
        .navigationBarBackButtonHidden(true)
    }
}
