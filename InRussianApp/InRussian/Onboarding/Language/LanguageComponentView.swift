//
//  LanguageComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct LanguageComponentView: View {
    let component: LanguageComponent

    @ObservedObject private var observedState: ObservableValue<LanguageComponentState>

    @State private var showLanguageCover: Bool = false
    @State private var showPrivacyCover: Bool = false

    private let languages: [(value: String, display: String)] = [
        ("RUSSIAN", "Русский"),
        ("ENGLISH", "English"),
        ("CHINESE", "中文"),
        ("UZBEK", "O‘zbek"),
        ("TAJIK", "Тоҷикӣ"),
        ("HINDI", "हिन्दी")
    ]

    init(component: LanguageComponent) {
        self.component = component
        self.observedState = ObservableValue(component.state)
    }

    private func displayName(for value: String) -> String {
        languages.first(where: { $0.value == value })?.display ?? value
    }

    var body: some View {
        let state = observedState.value

        ZStack {
            Color(.secondarySystemBackground).ignoresSafeArea()

            VStack(spacing: 24) {
                Image(systemName: "person.crop.circle")
                    .resizable()
                    .foregroundColor(AppColors.Palette.accent.color)
                    .frame(width: 200, height: 200)
                    .padding(.top, 22)

                Text("Для продолжения расскажите немного о себе")
                    .font(.system(size: 24))
                    .fontWeight(.medium)
                    .fontWidth(.standard)
                    .multilineTextAlignment(.center)
                    .foregroundColor(AppColors.Palette.fontCaptive.color)
                    .padding(.bottom, 8)
                    .padding(.horizontal, 16)
                Spacer()

                VStack(spacing: 24) {
                    HStack(alignment: .center, spacing: 16) {
                        VStack(alignment: .leading, spacing: 2) {
                            Button(action: {
                                withAnimation { showPrivacyCover = true }
                            }) {
                                HStack(spacing: 6) {
                                    Text("Я даю согласие")
                                        .foregroundColor(.blue)
                                        .font(.system(size: 17, weight: .medium))
                                    Image(systemName: "info.circle")
                                        .foregroundColor(.blue)
                                        .font(.system(size: 20, weight: .medium))
                                }
                                .buttonStyle(.plain)
                            }
                            Text("на обработку персональных данных")
                                .font(.footnote)
                                .foregroundColor(AppColors.Palette.footnote.color)
                        }
                        Spacer()
                        CustomCheckbox(isChecked: Binding(
                            get: { state.hasGivenPermission },
                            set: { component.clickOnToggleButton(isSelected: $0) }
                        ))
                    }
                    .padding(.horizontal, 10)
                    .frame(height: 64)
                    .background(AppColors.Palette.componentBackground.color)
                    .cornerRadius(16)

                    HStack {
                        Text("Язык приложения:")
                            .foregroundColor(.primary)
                            .font(.system(size: 18, weight: .medium))
                        Spacer()
                        HStack(spacing: 8) {
                            Text(displayName(for: state.selectedLanguage))
                                .foregroundColor(.primary)
                                .font(.system(size: 18, weight: .medium))
                            Image(systemName: "globe")
                                .renderingMode(.template)
                                .foregroundColor(.blue)
                                .font(.system(size: 30, weight: .medium))
                        }
                    }
                    .contentShape(Rectangle())
                    .onTapGesture {
                        withAnimation { showLanguageCover = true }
                    }
                    .frame(height: 64)
                    .padding(.horizontal, 10)
                    .background(AppColors.Palette.componentBackground.color)
                    .cornerRadius(16)
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 32)
            }
            if showLanguageCover {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .onTapGesture {
                        withAnimation { showLanguageCover = false }
                    }
                    .zIndex(2)

                VStack(spacing: 0) {
                    Text("Выберите язык")
                        .font(.headline)
                        .padding(.top, 28)
                        .padding(.bottom, 12)

                    ForEach(Array(languages.enumerated()), id: \.element.value) { index, lang in
                        Button(action: {
                            component.selectLanguage(language: lang.value)
                            withAnimation { showLanguageCover = false }
                        }) {
                            HStack {
                                Text(lang.display)
                                    .foregroundColor(.primary)
                                    .font(.system(size: 20, weight: .medium))
                                Spacer()
                                if state.selectedLanguage == lang.value {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(AppColors.Palette.accent.color)
                                        .font(.system(size: 18, weight: .bold))
                                }
                            }
                            .padding(.vertical, 18)
                            .padding(.horizontal, 32)
                            .background(Color.clear)
                        }
                        if index < languages.count - 1 {
                            Divider()
                                .padding(.leading, 32)
                        }
                    }
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: 400)
                .background(
                    RoundedRectangle(cornerRadius: 20)
                        .fill(AppColors.Palette.componentBackground.color)
                        .shadow(radius: 10)
                )
                .padding(.horizontal, 32)
                .padding(.vertical, 100)
                .transition(.scale)
                .zIndex(3)
            }

            if showPrivacyCover {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .onTapGesture {
                        withAnimation { showPrivacyCover = false }
                    }
                    .zIndex(4)

                VStack(spacing: 20) {
                    ScrollView {
                        Text("Политика конфиденциальности")
                            .font(.title3.bold())
                            .padding(.top, 24)

                        Text("""
                        Ваши персональные данные используются только с целью предоставления функциональности приложения и не передаются третьим лицам. Честно-честно, PS, не забыть заменить эти данные на реальную политику конфидценциальности TODO TODO() CHANGEME
                        """)
                        .font(.body)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 24)
                    }
                }
                .background(
                    RoundedRectangle(cornerRadius: 18)
                        .fill(AppColors.Palette.componentBackground.color)
                        .shadow(radius: 10)
                )
                .padding(.horizontal, 32)
                .padding(.vertical, 100)
                .transition(.scale)
                .zIndex(5)
            }
        }
        .navigationTitle("Язык приложения")
        .navigationBarTitleDisplayMode(.large)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {
                    if state.isActiveContinueButton {
                        component.onNext()
                    }
                }) {
                    HStack {
                        Text("Далее")
                        Image(systemName: "chevron.right")
                    }
                    .foregroundColor(state.isActiveContinueButton ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!state.isActiveContinueButton)
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}

struct CustomCheckbox: View {
    @Binding var isChecked: Bool

    var body: some View {
        Button(action: { isChecked.toggle() }) {
            ZStack {
                RoundedRectangle(cornerRadius: 6)
                    .stroke(Color(.systemBlue), lineWidth: 2)
                    .background(
                        RoundedRectangle(cornerRadius: 6)
                            .fill(isChecked ? Color(.systemBlue) : Color.clear)
                    )
                if isChecked {
                    Image(systemName: "checkmark")
                        .foregroundColor(.white)
                        .font(.system(size: 18, weight: .bold))
                }
            }
            .frame(width: 32, height: 32)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}
