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

    private let languageOptions = [
        "Русский", "Английский", "Китайский", "Узбекский", "Таджикский", "Хинди", "Казахский", "Французский", "Немецкий", "Испанский",
        "Португальский", "Итальянский", "Японский", "Корейский", "Турецкий", "Польский", "Украинский", "Белорусский", "Литовский", "Латышский",
        "Эстонский", "Грузинский", "Армянский", "Азербайджанский", "Монгольский", "Вьетнамский", "Тайский", "Бирманский", "Лаосский", "Кхмерский",
        "Индонезийский", "Малайский", "Филиппинский (Тагальский)", "Суахили", "Арабский", "Персидский", "Пушту", "Курдский", "Туркменский", "Киргизский",
        "Башкирский", "Татарский", "Чувашский", "Марийский", "Мордовский", "Удмуртский", "Коми", "Карельский", "Финский", "Шведский",
        "Норвежский", "Датский", "Исландский", "Венгерский", "Чешский", "Словацкий", "Словенский", "Хорватский", "Сербский", "Болгарский",
        "Македонский", "Румынский", "Албанский", "Греческий", "Еврейский (Иврит)", "Йидиш", "Бенгальский", "Панджаби", "Гуджарати", "Тамильский",
        "Телугу", "Каннада", "Малаялам", "Маратхи", "Сингальский", "Непальский", "Синдхи", "Урду", "Пушту", "Сингальский",
        "Тайский", "Лаосский", "Бирманский", "Кхмерский", "Болгарский", "Македонский", "Венгерский", "Чешский", "Словацкий", "Польский",
        "Нидерландский", "Фламандский", "Африкаанс", "Каталанский", "Галисийский", "Баскский", "Фризский", "Ирландский", "Шотландский (Гэльский)", "Уэльский",
        "Корнский", "Бретонский", "Люксембургский", "Албанский", "Ретороманский", "Сардский", "Сицилийский", "Неаполитанский", "Пьемонтский", "Лигурийский",
        "Ломбардский", "Венетский", "Фриульский", "Сардский", "Мальтийский", "Креольский (Гаитянский)", "Сомалийский", "Амхарский", "Тигринья", "Оромо",
        "Суахили", "Лингала", "Кикуйю", "Шона", "Зулу", "Коса", "Сото", "Тсвана", "Тсонга", "Венда",
        "Чичева", "Ломве", "Яо", "Ганда", "Луганда", "Руанда", "Кирунди", "Масаи", "Сукума", "Ньянджа",
        "Мандинка", "Волоф", "Фула", "Хауса", "Йоруба", "Игбо", "Эве", "Га", "Акан", "Тви",
        "Бамбара", "Сонгаи", "Туарегский", "Берберский", "Маори", "Тонганский", "Самоанский", "Гавайский", "Фиджийский", "Тахитянский",
        "Микронезийский", "Маршалльский", "Палауанский", "Тувалуанский", "Науруанский", "Бислама", "Пиджин-Инглиш", "Креольский (Сейшельский)", "Креольский (Реюньонский)", "Креольский (Маврикийский)",
        "Эсперанто", "Интерлингва", "Идо", "Волапюк"
    ]

    @ObservedObject private var observedState: ObservableValue<EducationComponentState>

    @State private var understandsRussian: Bool
    @State private var speaksRussian: Bool
    @State private var readsRussian: Bool
    @State private var writesRussian: Bool

    init(component: EducationComponent) {
        self.component = component
        self.observedState = ObservableValue(component.state)

        let s = component.state.value
        _understandsRussian = State(initialValue: s.understandsRussian)
        _speaksRussian = State(initialValue: s.speaksRussian)
        _readsRussian = State(initialValue: s.readsRussian)
        _writesRussian = State(initialValue: s.writesRussian)
    }

    // MARK: - Labels

    private func languagesPlaceholder(for languages: [String]) -> Text {
        if languages.isEmpty {
            return Text("Языки").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text("")
        }
    }

    var body: some View {
        let state = observedState.value

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
                            placeholder: languagesPlaceholder(for: state.languages),
                            selections: Binding(
                                get: { state.languages },
                                set: { newList in
                                    let old = state.languages

                                    old.filter { !newList.contains($0) }
                                        .forEach { component.deleteLanguage(string: $0) }

                                    newList.filter { !old.contains($0) }
                                        .forEach { component.selectLanguage(language: $0) }
                                }
                            ),
                            showPicker: Binding(
                                get: { state.isOpenLanguages },
                                set: { component.onChangeExpanded(boolean: $0) }
                            ),
                            accent: AppColors.Palette.accent.color,
                            backgroundColor: AppColors.Palette.componentBackground.color
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CheckboxView(
                            isChecked: $understandsRussian,
                            label: "Понимает русский"
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CheckboxView(
                            isChecked: $speaksRussian,
                            label: "Говорит по-русски"
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CheckboxView(
                            isChecked: $readsRussian,
                            label: "Читает по-русски"
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CheckboxView(
                            isChecked: $writesRussian,
                            label: "Пишет по-русски"
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(
                            placeholder: "Вид деятельности",
                            text: Binding(
                                get: { state.kindOfActivity },
                                set: { component.changeActivity(activity: $0) }
                            )
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(
                            placeholder: "Образование",
                            text: Binding(
                                get: { state.education },
                                set: { component.changeEducation(education: $0) }
                            )
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(
                            placeholder: "Цель регистрации",
                            text: Binding(
                                get: { state.purposeOfRegistration },
                                set: { component.changePurpose(purpose: $0) }
                            )
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.clear)
                .padding(.bottom, 36)
            }

            if state.isOpenLanguages {
                DimmedModalBackground { withAnimation { component.onChangeExpanded(boolean: false) } }
                OptionListModal(
                    title: "Добавить язык",
                    options: languageOptions,
                    selected: Set(state.languages),
                    allowsMultiple: true,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { lang in component.selectLanguage(language: lang) },
                    onRemove: { lang in component.deleteLanguage(string: lang) },
                    onDone: { withAnimation { component.onChangeExpanded(boolean: false) } }
                )
            }
        }
        .navigationTitle("Образование и язык")
        .navigationBarTitleDisplayMode(.large)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button { component.onBack() } label: {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("Назад")
                    }
                    .foregroundColor(AppColors.Palette.accent.color)
                }
            }
            ToolbarItem(placement: .navigationBarTrailing) {
                Button { component.onNext() } label: {
                    HStack {
                        Text("Далее")
                        Image(systemName: "chevron.right")
                    }
                    .foregroundColor(state.continueEnable ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!state.continueEnable)
            }
        }
        // Keep local checkbox UI in sync if framework state changes externally
        .onChange(of: state.understandsRussian) { understandsRussian = $0 }
        .onChange(of: state.speaksRussian) { speaksRussian = $0 }
        .onChange(of: state.readsRussian) { readsRussian = $0 }
        .onChange(of: state.writesRussian) { writesRussian = $0 }
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
                    FlowLayout(spacing: 8, runSpacing: 8, alignment: .leading) {
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
