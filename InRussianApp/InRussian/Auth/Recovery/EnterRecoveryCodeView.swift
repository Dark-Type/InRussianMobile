//
//  EnterRecoveryCodeView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct EnterRecoveryCodeView: View {
    let component: EnterRecoveryCodeComponent

    @State private var code: String = ""
    @State private var showSupportCover: Bool = false

    private let targetCode = "code"

    init(component: EnterRecoveryCodeComponent) {
        self.component = component
        _code = State(initialValue: component.state.value.code)
        _showSupportCover = State(initialValue: component.state.value.questionShow)
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
                .disabled(component.state.value.loading)

                if let error = component.state.value.emailError {
                    Text(error.localized)
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.bottom, 16)
                }

                Spacer()

                TimerButton(
                    initialSeconds: 300,
                    inactiveColor: AppColors.Palette.inactive.color,
                    activeColor: AppColors.Palette.accent.color
                ) {}
                    .padding(.bottom, 24)

                if component.state.value.loading {
                    ProgressView()
                        .scaleEffect(1.2)
                        .padding(.top, 16)
                }
            }
            .padding(.horizontal, 28)

            if showSupportCover {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .transition(.opacity)
                    .onTapGesture {
                        withAnimation {
                            showSupportCover = false
                            component.onMissClick()
                        }
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
                            component.onSupportContactClick()
                            withAnimation { showSupportCover = false }
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
            component.codeChange(code: newValue)
            if newValue == targetCode {
                component.onCodeEntered(code: newValue)
            }
        }
        .onChange(of: component.state.value.questionShow) { newValue in
            withAnimation {
                showSupportCover = newValue
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
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {
                    withAnimation {
                        showSupportCover = true
                        component.onQuestionClick()
                    }
                }) {
                    Image(systemName: "questionmark.circle")
                        .foregroundColor(AppColors.Palette.accent.color)
                        .imageScale(.large)
                }
                .accessibilityLabel("Помощь")
                .disabled(component.state.value.loading)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
