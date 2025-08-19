//
//  EnterRecoveryCodeView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct EnterRecoveryCodeView: View {
    let component: EnterRecoveryCodeComponent

    @State private var code: String = ""
    @State private var showSupportCover: Bool = false

    private let targetCode = "code"

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

                Text("Введите код, который мы отправили вам на почту")
                    .font(.footnote)
                    .foregroundColor(AppColors.Palette.footnote.color)
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 16)
                    .padding(.trailing, 24)

                OutlinedTextfield(
                    text: $code,
                    placeholder: "Код подтверждения",
                    isSecure: false
                )
                .padding(.bottom, 24)
                
                Spacer()
                

                TimerButton(
                    initialSeconds: 300,
                    inactiveColor: AppColors.Palette.inactive.color,
                    activeColor: AppColors.Palette.accent.color
                ) {
                    
                }
                .padding(.bottom, 24)
            }
            .padding(.horizontal, 28)

            if showSupportCover {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .transition(.opacity)
                    .onTapGesture {
                        withAnimation { showSupportCover = false }
                    }
                    .zIndex(2)

                VStack(spacing: 20) {
                    Text("Не пришло письмо?")
                        .font(.title3.bold())
                        .padding(.top, 24)

                    Text("Убедитесь, что сообщение на почте не было отправлено в папку спам")
                        .font(.body)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 24)

                    Divider()
                        .background(AppColors.Palette.accent.color)
                        .frame(height: 1)
                        .padding(.horizontal, 16)

                    Text("Связаться с поддержкой")
                        .font(.body)
                        .foregroundColor(AppColors.Palette.accent.color)
                        .padding(.bottom, 24)
                        .onTapGesture {
                            
                        }
                }
                .background(
                    RoundedRectangle(cornerRadius: 18)
                        .fill(AppColors.Palette.baseBackground.color)
                        .shadow(radius: 10)
                )
                .padding(.horizontal, 32)
                .padding(.vertical, 100)
                .transition(.scale)
                .zIndex(3)
            }
        }
        .onChange(of: code) { newValue in
            if newValue == targetCode {
                component.onCodeEntered(code: newValue)
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
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {
                    withAnimation { showSupportCover = true }
                }) {
                    Image(systemName: "questionmark.circle")
                        .foregroundColor(AppColors.Palette.accent.color)
                        .imageScale(.large)
                }
                .accessibilityLabel("Помощь")
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
