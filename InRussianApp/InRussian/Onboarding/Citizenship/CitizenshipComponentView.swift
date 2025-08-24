//
//  CitizenshipComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

enum TimeSpentInRussia: String, CaseIterable, Identifiable {
    case lessThanMonth = "менее месяца"
    case lessThanHalfYear = "менее полугода"
    case lessThanYear = "менее года"
    case moreThanYear = "более года"
    case moreThanFiveYears = "более пяти лет"
    case never = "никогда не был"

    var id: String { rawValue }
}

struct CitizenshipComponentView: View {
    let component: CitizenshipComponent

    private let countryOptions = [
        "Австралия", "Австрия", "Азербайджан", "Албания", "Алжир", "Ангола", "Андорра", "Антигуа и Барбуда", "Аргентина", "Армения",
        "Афганистан", "Багамские Острова", "Бангладеш", "Барбадос", "Бахрейн", "Беларусь", "Белиз", "Бельгия", "Бенин", "Болгария",
        "Боливия", "Босния и Герцеговина", "Ботсвана", "Бразилия", "Бруней", "Буркина-Фасо", "Бурунди", "Бутан", "Вануату", "Ватикан",
        "Великобритания", "Венгрия", "Венесуэла", "Восточный Тимор", "Вьетнам", "Габон", "Гаити", "Гайана", "Гамбия", "Гана",
        "Гватемала", "Гвинея", "Гвинея-Бисау", "Германия", "Гондурас", "Гренада", "Греция", "Грузия", "Дания", "Джибути", "Доминика",
        "Доминиканская Республика", "Египет", "Замбия", "Зимбабве", "Израиль", "Индия", "Индонезия", "Иордания", "Ирак", "Иран",
        "Ирландия", "Исландия", "Испания", "Италия", "Йемен", "Кабо-Верде", "Казахстан", "Камбоджа", "Камерун", "Канада", "Катар",
        "Кения", "Кипр", "Киргизия", "Кирибати", "Китай", "Колумбия", "Коморы", "Конго", "Коста-Рика", "Кот-д’Ивуар", "Куба",
        "Кувейт", "Лаос", "Латвия", "Лесото", "Либерия", "Ливан", "Ливия", "Литва", "Лихтенштейн", "Люксембург", "Маврикий", "Мавритания",
        "Мадагаскар", "Макао", "Македония", "Малави", "Малайзия", "Мали", "Мальдивы", "Мальта", "Марокко", "Маршалловы Острова",
        "Мексика", "Мозамбик", "Молдова", "Монако", "Монголия", "Мьянма", "Намибия", "Науру", "Непал", "Нигер", "Нигерия", "Нидерланды",
        "Никарагуа", "Новая Зеландия", "Норвегия", "ОАЭ", "Оман", "Пакистан", "Палау", "Палестина", "Панама", "Папуа — Новая Гвинея",
        "Парагвай", "Перу", "Польша", "Португалия", "Россия", "Руанда", "Румыния", "Сальвадор", "Самоа", "Сан-Марино", "Сан-Томе и Принсипи",
        "Саудовская Аравия", "Свазиленд", "Северная Корея", "Северная Македония", "Сейшельские Острова", "Сенегал", "Сент-Винсент и Гренадины",
        "Сент-Китс и Невис", "Сент-Люсия", "Сербия", "Сингапур", "Сирия", "Словакия", "Словения", "Соломоновы Острова", "Сомали",
        "Судан", "Суринам", "США", "Сьерра-Леоне", "Таджикистан", "Таиланд", "Танзания", "Того", "Тонга", "Тринидад и Тобаго", "Тувалу",
        "Тунис", "Туркмения", "Турция", "Уганда", "Узбекистан", "Украина", "Уругвай", "Фиджи", "Филиппины", "Финляндия", "Франция",
        "Хорватия", "ЦАР", "Чад", "Черногория", "Чехия", "Чили", "Швейцария", "Швеция", "Шри-Ланка", "Эквадор", "Экваториальная Гвинея",
        "Эритрея", "Эстония", "Эфиопия", "ЮАР", "Южная Корея", "Южный Судан", "Ямайка", "Япония"
    ].sorted()
    let nationalityOptions = [
        "Русский", "Татарин", "Башкир", "Чеченец", "Удмурт", "Мордва", "Мариец", "Чуваш", "Бурят", "Якут", "Осетин", "Калмык",
        "Кабардинец", "Аварец", "Даргинец", "Кумык", "Лезгин", "Ингуш", "Карачаевец", "Балкарец", "Армянин", "Азербайджанец",
        "Грузин", "Еврей", "Белорус", "Украинец", "Молдаванин", "Казах", "Узбек", "Киргиз", "Туркмен", "Таджик", "Латыш",
        "Литовец", "Эстонец", "Поляк", "Немец", "Грек", "Болгарин", "Румын", "Чех", "Словац", "Венгр", "Финн", "Швед", "Норвежец",
        "Датчанин", "Англичанин", "Шотландец", "Ирландец", "Француз", "Итальянец", "Испанец", "Португалец", "Серб", "Хорват",
        "Словенец", "Македонец", "Черногорец", "Босниец", "Албанец", "Турок", "Араб", "Иранец", "Афганец", "Пакистанец", "Индус",
        "Китайц", "Кореец", "Японец", "Монгол", "Вьетнамец", "Лаосец", "Кхмер", "Таец", "Бирманец", "Малайзиец", "Индонезиец",
        "Филиппинец", "Сингапурец", "Канадец", "Американец", "Мексиканец", "Кубинец", "Бразилец", "Аргентинец", "Чилиец",
        "Перуанец", "Колумбиец", "Венесуэлец", "Эквадорец", "Боливиец", "Парагваец", "Уругваец", "Гватемалец", "Коста-риканец",
        "Гондурасец", "Сальвадорец", "Панамец", "Ямайец", "Гаитянин", "Доминиканец", "Камерунец", "Ганец", "Нигериец",
        "Суданец", "Эфиоп", "Сомалиец", "Кениец", "Танзаниец", "Угандиец", "Замбиец", "Зимбабвиец", "Мозамбикец", "Малагасиец",
        "Мадагаскарец", "Сенегалец", "Марокканец", "Алжирец", "Тунисец", "Ливиец", "Египтянин", "Южноафриканец", "Австралиец",
        "Новозеландец", "Папуас", "Фиджиец", "Тонганец", "Самоанец", "Маори", "Гренландец", "Исландиец", "Еврей", "Цыган",
        "Ассириец", "Осетин", "Караим", "Татарин-крымский", "Ахалтекинец", "Абхаз", "Адыгеец", "Абазин", "Шорец", "Тувинец",
        "Хакас", "Саха", "Эвенк", "Ненец", "Чукча", "Коряк", "Ительмен", "Коми", "Карел", "Вепс", "Саам", "Удмурт", "Мари",
        "Мордва", "Эрзя", "Мокша", "Чуваш", "Башкир", "Калмык", "Бурят", "Тувинец", "Якут", "Хант", "Манси", "Селькуп", "Нганасан",
        "Эскимос", "Алеут", "Кет", "Юкагир", "Эвен", "Долган", "Нивх", "Уильта", "Ороч", "Удэгеец", "Тазы", "Орок", "Чулымец",
        "Селькуп", "Сойот", "Тофалар", "Айну", "Нагаец", "Башкирин", "Кумыкец", "Лезгин", "Табасаранец", "Рутулец", "Цахурец",
        "Агулец", "Тат", "Джухур", "Лакец", "Дидой", "Тиндиец", "Чамалинец", "Ботлихец", "Гинухец", "Годоберинец", "Хваршине",
        "Багулалец", "Цез", "Гунзибец", "Хиналуг", "Будуг", "Крымчак", "Карачаевец", "Балкарец", "Абазин", "Черкес", "Ногай", "Кумыкец"
    ].sorted()

    @State private var citizenships: [String]
    @State private var nationality: String
    @State private var countryOfResidence: String
    @State private var cityOfResidence: String
    @State private var countryDuringEducation: String
    @State private var timeSpentInRussia: TimeSpentInRussia?

    @State private var showCitizenshipPicker = false
    @State private var showNationalityPicker = false
    @State private var showTimeSpentPicker = false
    @State private var showCountryOfResidencePicker = false
    @State private var showCountryDuringEducationPicker = false

    init(component: CitizenshipComponent) {
        self.component = component
        _citizenships = State(initialValue: component.state.value.citizenship)
        _nationality = State(initialValue: component.state.value.nationality)
        _countryOfResidence = State(initialValue: component.state.value.countryOfResidence)
        _cityOfResidence = State(initialValue: component.state.value.cityOfResidence)
        _countryDuringEducation = State(initialValue: component.state.value.countryDuringEducation)
        _timeSpentInRussia = State(initialValue:
            TimeSpentInRussia.allCases.first(where: { $0.rawValue == component.state.value.timeSpentInRussia })
        )
    }

    // MARK: - Labels

    private var citizenshipPlaceholder: Text {
        Text("Гражданство").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
    }

    private var nationalityLabel: Text {
        if nationality.isEmpty {
            return Text("Национальность").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(nationality).foregroundColor(.primary)
        }
    }

    private var timeSpentLabel: Text {
        if timeSpentInRussia == nil {
            return Text("Время в России").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(timeSpentInRussia!.rawValue).foregroundColor(.primary)
        }
    }

    private var countryOfResidenceLabel: Text {
        if countryOfResidence.isEmpty {
            return Text("Страна проживания").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(countryOfResidence).foregroundColor(.primary)
        }
    }

    private var countryDuringEducationLabel: Text {
        if countryDuringEducation.isEmpty {
            return Text("Страна во время обучения").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(countryDuringEducation).foregroundColor(.primary)
        }
    }

    // MARK: - Validation

    private var isFormFilled: Bool {
        !citizenships.isEmpty &&
            !nationality.isEmpty &&
            !countryOfResidence.isEmpty &&
            !cityOfResidence.isEmpty &&
            !countryDuringEducation.isEmpty &&
            timeSpentInRussia != nil
    }

    var body: some View {
        ZStack {
            Color(.secondarySystemBackground).ignoresSafeArea()

            VStack(spacing: 24) {
                Image(systemName: "globe.europe.africa.fill")
                    .resizable()
                    .scaledToFit()
                    .foregroundColor(AppColors.Palette.accent.color)
                    .frame(width: 120, height: 120)
                    .padding(.top, 64)
                Spacer()

                Form {
                    Section {
                        CitizenshipChipField(
                            placeholder: citizenshipPlaceholder,
                            selections: $citizenships,
                            showPicker: $showCitizenshipPicker,
                            accent: AppColors.Palette.accent.color,
                            backgroundColor: AppColors.Palette.componentBackground.color
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        PickerLikeRow(
                            label: nationalityLabel,
                            icon: "chevron.down",
                            isEmpty: nationality.isEmpty,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showNationalityPicker = true }
                        }
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        PickerLikeRow(
                            label: timeSpentLabel,
                            icon: "chevron.down",
                            isEmpty: timeSpentInRussia == nil,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showTimeSpentPicker = true }
                        }
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        PickerLikeRow(
                            label: countryOfResidenceLabel,
                            icon: "chevron.down",
                            isEmpty: countryOfResidence.isEmpty,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showCountryOfResidencePicker = true }
                        }
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(placeholder: "Город проживания", text: $cityOfResidence)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        PickerLikeRow(
                            label: countryDuringEducationLabel,
                            icon: "chevron.down",
                            isEmpty: countryDuringEducation.isEmpty,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showCountryDuringEducationPicker = true }
                        }
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.clear)
                .padding(.bottom, 36)
            }

            if showCitizenshipPicker {
                DimmedModalBackground { withAnimation { showCitizenshipPicker = false } }
                OptionListModal(
                    title: "Добавить гражданство",
                    options: countryOptions,
                    selected: Set(citizenships),
                    allowsMultiple: true,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { country in
                        if !citizenships.contains(country) {
                            citizenships.append(country)
                        }
                    },
                    onRemove: { country in
                        citizenships.removeAll { $0 == country }
                    },
                    onDone: {
                        withAnimation { showCitizenshipPicker = false }
                    }
                )
            }

            if showNationalityPicker {
                DimmedModalBackground { withAnimation { showNationalityPicker = false } }
                OptionListModal(
                    title: "Выберите национальность",
                    options: nationalityOptions,
                    selected: nationality.isEmpty ? [] : [nationality],
                    allowsMultiple: false,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { choice in
                        nationality = choice
                        withAnimation { showNationalityPicker = false }
                    },
                    onRemove: { _ in },
                    onDone: { withAnimation { showNationalityPicker = false } }
                )
            }

            if showTimeSpentPicker {
                DimmedModalBackground { withAnimation { showTimeSpentPicker = false } }
                OptionListModal(
                    title: "Время в России",
                    options: TimeSpentInRussia.allCases.map { $0.rawValue },
                    selected: timeSpentInRussia == nil ? [] : [timeSpentInRussia!.rawValue],
                    allowsMultiple: false,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { value in
                        timeSpentInRussia = TimeSpentInRussia.allCases.first { $0.rawValue == value }
                        withAnimation { showTimeSpentPicker = false }
                    },
                    onRemove: { _ in },
                    onDone: { withAnimation { showTimeSpentPicker = false } }
                )
            }
            if showCountryOfResidencePicker {
                DimmedModalBackground { withAnimation { showCountryOfResidencePicker = false } }
                OptionListModal(
                    title: "Страна проживания",
                    options: countryOptions,
                    selected: countryOfResidence.isEmpty ? [] : [countryOfResidence],
                    allowsMultiple: false,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { choice in
                        countryOfResidence = choice
                        withAnimation { showCountryOfResidencePicker = false }
                    },
                    onRemove: { _ in },
                    onDone: { withAnimation { showCountryOfResidencePicker = false } }
                )
            }
            if showCountryDuringEducationPicker {
                DimmedModalBackground { withAnimation { showCountryDuringEducationPicker = false } }
                OptionListModal(
                    title: "Страна во время обучения",
                    options: countryOptions,
                    selected: countryDuringEducation.isEmpty ? [] : [countryDuringEducation],
                    allowsMultiple: false,
                    accent: AppColors.Palette.accent.color,
                    onSelect: { choice in
                        countryDuringEducation = choice
                        withAnimation { showCountryDuringEducationPicker = false }
                    },
                    onRemove: { _ in },
                    onDone: { withAnimation { showCountryDuringEducationPicker = false } }
                )
            }
        }
        .navigationTitle("Гражданство и проживание")
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
                    HStack {
                        Text("Далее")
                        Image(systemName: "chevron.right")
                    }
                    .foregroundColor(isFormFilled ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!isFormFilled)
            }
        }
    }
}

// MARK: - Picker-Like Row

struct PickerLikeRow: View {
    let label: Text
    let icon: String
    let isEmpty: Bool
    let inactiveColor: Color
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(alignment: .center) {
                label
                Spacer()
                Image(systemName: icon)
                    .foregroundColor(isEmpty ? Color(.systemBlue) : inactiveColor)
            }
            .frame(minHeight: 44)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Citizenship Chips Field

struct CitizenshipChipField: View {
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

// MARK: - Chip View

struct ChipView: View {
    let text: String
    let accent: Color
    let onRemove: () -> Void

    var body: some View {
        ZStack(alignment: .topTrailing) {
            Text(text)
                .foregroundColor(.primary)
                .padding(.horizontal, 14)
                .padding(.vertical, 10)
                .background(
                    Capsule()
                        .fill(accent.opacity(0.12))
                )
                .overlay(
                    Capsule()
                        .stroke(accent.opacity(0.35), lineWidth: 1)
                )

            Button(action: onRemove) {
                Image(systemName: "xmark")
                    .font(.system(size: 10, weight: .bold))
                    .foregroundColor(.white)
                    .padding(5)
                    .background(Circle().fill(accent))
            }
            .offset(x: 6, y: -6)
            .buttonStyle(.plain)
        }
        .fixedSize()
    }
}

struct OptionListModal: View {
    let title: String
    let options: [String]
    var selected: Set<String>
    let allowsMultiple: Bool
    let accent: Color
    let onSelect: (String) -> Void
    let onRemove: (String) -> Void
    let onDone: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            Text(title)
                .font(.headline)
                .padding(.top, 24)
                .padding(.bottom, 12)

            ScrollView {
                LazyVStack(spacing: 0) {
                    ForEach(options, id: \.self) { option in
                        let isSelected = selected.contains(option)
                        Button {
                            if allowsMultiple {
                                if isSelected {
                                    onRemove(option)
                                } else {
                                    onSelect(option)
                                }
                            } else {
                                onSelect(option)
                            }
                        } label: {
                            HStack {
                                Text(option)
                                    .foregroundColor(.primary)
                                Spacer()
                                if isSelected {
                                    Image(systemName: "checkmark.circle.fill")
                                        .foregroundColor(accent)
                                }
                            }
                            .padding(.vertical, 14)
                            .padding(.horizontal, 24)
                        }
                        .buttonStyle(.plain)
                        .contentShape(Rectangle())

                        if option != options.last {
                            Divider()
                                .padding(.leading, 24)
                        }
                    }
                }
            }
            .frame(maxHeight: 260)

            if allowsMultiple {
                Button(action: onDone) {
                    Text("Готово")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(
                            RoundedRectangle(cornerRadius: 12).fill(accent)
                        )
                        .padding(.horizontal, 24)
                        .padding(.top, 8)
                        .padding(.bottom, 20)
                }
            } else {
                Button(action: onDone) {
                    Text("Отмена")
                        .font(.system(size: 16))
                        .foregroundColor(accent)
                        .padding(.vertical, 10)
                }
                .padding(.bottom, 12)
            }
        }
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(AppColors.Palette.componentBackground.color)
                .shadow(radius: 10)
        )
        .padding(.horizontal, 32)
        .transition(.scale)
        .zIndex(3)
    }
}

// MARK: - Dimmed Background

struct DimmedModalBackground: View {
    let dismiss: () -> Void
    var body: some View {
        Color.black.opacity(0.45)
            .ignoresSafeArea()
            .onTapGesture { dismiss() }
            .transition(.opacity)
            .zIndex(2)
    }
}
