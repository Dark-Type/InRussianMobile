//
//  BaseAuthView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct BaseAuthView: View {
    let component: BaseAuthComponent

    var body: some View {
        ZStack {
            AppColors.Palette.baseBackground.color
                .ignoresSafeArea()

            VStack(spacing: 32) {
                Spacer()

                AppImages.Logo.appLogo
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: 400, maxHeight: 250)
                    .padding(.top, 48)

                Spacer()

                VStack(spacing: 12) {
                    CustomButton(
                        text: "Зарегистрироваться",
                        color: AppColors.Palette.secondary.color,
                        trailingLogo: nil
                    ) {
                        component.onRegisterClicked()
                    }

                    CustomButton(
                        text: "Войти",
                        color: AppColors.Palette.accent.color,
                        trailingLogo: nil
                    ) {
                        component.onLoginClicked()
                    }

                    SocialDivider()

                    CustomButton(
                        text: "Войти с VK ID",
                        color: AppColors.Palette.vk.color,
                        trailingLogo: .vk
                    ) {
                        component.onVkClicked()
                    }

                    CustomButton(
                        text: "Войти с Яндекс ID",
                        color: AppColors.Palette.yandex.color,
                        trailingLogo: .yandex
                    ) {
                        component.onYandexClicked()
                    }
                }
                .padding(.horizontal, 28)
                .padding(.bottom, 48)
            }
        }
    }
}

private struct SocialDivider: View {
    var body: some View {
        HStack(alignment: .center) {
            Rectangle()
                .fill(AppColors.Palette.stroke.color)
                .frame(height: 1)

            Text("вход через\nсоциальные сети")
                .font(.footnote)
                .foregroundColor(AppColors.Palette.footnote.color)
                .multilineTextAlignment(.center)
                .lineLimit(2, reservesSpace: true)
                .frame(width: UIScreen.main.bounds.width * 0.3)

            Rectangle()
                .fill(AppColors.Palette.stroke.color)
                .frame(height: 1)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 4)
    }
}
