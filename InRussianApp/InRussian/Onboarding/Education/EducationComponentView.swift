//
//  EducationComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct EducationComponentView: View {
    let component: EducationComponent

    // Options
    private let languageOptions = [
        "Русский", "Английский", "Китайский", "Узбекский", "Таджикский",
        "Хинди", "Казахский", "Французский", "Немецкий", "Испанский"
    ]

    // State
    @State private var languages: [String]
    @State private var understandsRussian: Bool
    @State private var speaksRussian: Bool
    @State private var readsRussian: Bool
    @State private var writesRussian: Bool
    @State private var kindOfActivity: String
    @State private var education: String
    @State private var purposeOfRegistration: String

    // Pickers / modals
    @State private var showLanguagesPicker = false

    init(component: EducationComponent) {
        self.component = component
        _languages = State(initialValue: component.state.value.languages)
        _understandsRussian = State(initialValue: component.state.value.understandsRussian)
        _speaksRussian = State(initialValue: component.state.value.speaksRussian)
        _readsRussian = State(initialValue: component.state.value.readsRussian)
        _writesRussian = State(initialValue: component.state.value.writesRussian)
        _kindOfActivity = State(initialValue: component.state.value.kindOfActivity)
        _education = State(initialValue: component.state.value.education)
        _purposeOfRegistration = State(initialValue: component.state.value.purposeOfRegistration)
    }

    // MARK: - Labels

    private var languagesPlaceholder: Text {
        if languages.isEmpty {
            return Text("Языки").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text("") 
        }
    }

    private var isFormFilled: Bool {
        !languages.isEmpty &&
            !kindOfActivity.isEmpty &&
            !education.isEmpty &&
            !purposeOfRegistration.isEmpty
    }

    var body: some View {
        ZStack {
            Color(.secondarySystemBackground).ignoresSafeArea()

            VStack(spacing: 24) {
                Image(systemName: "graduationcap.circle.fill")
                    .resizable()
                    .scaledToFit()
                    .foregroundColor(AppColors.Palette.accent.color)
                    .frame(width: 120, height: 120)
                    .padding(.top, 64)
                Spacer()

                Form {
                    Section {

                        LanguageChipField(
                            placeholder: languagesPlaceholder,
                            selections: $languages,
                            showPicker: $showLanguagesPicker,
                            accent: AppColors.Palette.accent.color,
                            backgroundColor: AppColors.Palette.componentBackground.color
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CheckboxView(isChecked: $understandsRussian, label: "Понимает русский")
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CheckboxView(isChecked: $speaksRussian, label: "Говорит по-русски")
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CheckboxView(isChecked: $readsRussian, label: "Читает по-русски")
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CheckboxView(isChecked: $writesRussian, label: "Пишет по-русски")
                            .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(placeholder: "Вид деятельности", text: $kindOfActivity)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CustomAsteriskTextField(placeholder: "Образование", text: $education)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CustomAsteriskTextField(placeholder: "Цель регистрации", text: $purposeOfRegistration)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.clear)
                .padding(.bottom, 36)
            }

            
            if showLanguagesPicker {
                DimmedModalBackground { withAnimation { showLanguagesPicker = false } }
                OptionListModal(
                    title: "Добавить язык",
                    options: languageOptions,
                    selected: Set(languages),
                    allowsMultiple: true,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { lang in
                        if !languages.contains(lang) {
                            languages.append(lang)
                        }
                    },
                    onRemove: { lang in
                        languages.removeAll { $0 == lang }
                    },
                    onDone: { withAnimation { showLanguagesPicker = false } }
                )
            }
        }
        .navigationTitle("Образование и язык")
        .navigationBarTitleDisplayMode(.large)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button {
                    component.onBack()
                } label: {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("Назад")
                    }
                    .foregroundColor(AppColors.Palette.accent.color)
                }
            }
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    component.onNext()
                } label: {
                    HStack(spacing: 0) {
                        Text("Далее")
                            .font(.system(size: 17, weight: .semibold))
                        Image(systemName: "chevron.right")
                            .font(.system(size: 17, weight: .semibold))
                    }
                    .foregroundColor(isFormFilled ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!isFormFilled)
            }
        }
    }
}

// MARK: - Language Chips Field (multi-select)

struct LanguageChipField: View {
    let placeholder: Text
    @Binding var selections: [String]
    @Binding var showPicker: Bool
    let accent: Color
    let backgroundColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            if selections.isEmpty {
                HStack {
                    placeholder
                    Spacer()
                    Image(systemName: "chevron.down")
                        .foregroundColor(Color(.systemBlue))
                }
                .frame(minHeight: 44)
                .contentShape(Rectangle())
                .onTapGesture { withAnimation { showPicker = true } }
            } else {
                HStack(alignment: .top, spacing: 8) {
                    FlowLayout(spacing: 8, runSpacing: 8,  alignment: .leading) {
                        ForEach(selections, id: \.self) { item in
                            ChipView(
                                text: item,
                                accent: accent,
                                onRemove: {
                                    withAnimation {
                                        selections.removeAll { $0 == item }
                                    }
                                }
                            )
                        }
                    }
                    Spacer(minLength: 4)
                    Button {
                        withAnimation { showPicker = true }
                    } label: {
                        Image(systemName: "chevron.down")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(accent)
                            .padding(10)
                            .background(
                                Circle()
                                    .fill(backgroundColor.opacity(0.9))
                                    .overlay(
                                        Circle()
                                            .stroke(accent.opacity(0.4), lineWidth: 1)
                                    )
                            )
                    }
                    .buttonStyle(.plain)
                }
                .frame(minHeight: 44)
            }
        }
    }
}

