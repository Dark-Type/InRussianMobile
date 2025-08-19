//
//  CitizenshipComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared

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

    // Options
    private let countryOptions = [
        "Россия", "Казахстан", "Узбекистан", "Таджикистан", "Киргизия",
        "Беларусь", "Армения", "Грузия", "Китай", "Индия", "Турция", "Германия"
    ]

    @State private var citizenships: [String]
    @State private var nationality: String
    @State private var countryOfResidence: String
    @State private var cityOfResidence: String
    @State private var countryDuringEducation: String
    @State private var timeSpentInRussia: TimeSpentInRussia?

    
    @State private var showCitizenshipPicker = false
    @State private var showNationalityPicker = false
    @State private var showTimeSpentPicker = false

    init(component: CitizenshipComponent) {
        self.component = component
        _citizenships = State(initialValue: component.state.citizenship.compactMap { $0 as? String })
        _nationality = State(initialValue: component.state.nationality)
        _countryOfResidence = State(initialValue: component.state.countryOfResidence)
        _cityOfResidence = State(initialValue: component.state.cityOfResidence)
        _countryDuringEducation = State(initialValue: component.state.countryDuringEducation)
        _timeSpentInRussia = State(initialValue:
            TimeSpentInRussia.allCases.first(where: { $0.rawValue == component.state.timeSpentInRussia })
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
                        // Citizenship multi-select chips
                        CitizenshipChipField(
                            placeholder: citizenshipPlaceholder,
                            selections: $citizenships,
                            showPicker: $showCitizenshipPicker,
                            accent: AppColors.Palette.accent.color,
                            backgroundColor: AppColors.Palette.componentBackground.color
                        )
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        // Nationality single-select
                        PickerLikeRow(
                            label: nationalityLabel,
                            icon: "chevron.down",
                            isEmpty: nationality.isEmpty,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showNationalityPicker = true }
                        }
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        // Time spent single-select (enum)
                        PickerLikeRow(
                            label: timeSpentLabel,
                            icon: "chevron.down",
                            isEmpty: timeSpentInRussia == nil,
                            inactiveColor: AppColors.Palette.inactive.color
                        ) {
                            withAnimation { showTimeSpentPicker = true }
                        }
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        // Text fields
                        CustomAsteriskTextField(placeholder: "Страна проживания", text: $countryOfResidence)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CustomAsteriskTextField(placeholder: "Город проживания", text: $cityOfResidence)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                        CustomAsteriskTextField(placeholder: "Страна во время обучения", text: $countryDuringEducation)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.clear)
                .padding(.bottom, 36)
            }

            // Modals
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
                    options: countryOptions,
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
