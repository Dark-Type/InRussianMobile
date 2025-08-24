//
//  PersonalDataComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//
import Shared
import SwiftUI

struct PersonalDataComponentView: View {
    let component: PersonalDataComponent

    @ObservedObject private var observedState: ObservableValue<PersonalDataComponentState>
    @State private var showGenderPicker = false
    @State private var showDatePicker = false

    private let genderOptions = ["Мужской", "Женский"]

    init(component: PersonalDataComponent) {
        self.component = component
        self.observedState = ObservableValue(component.state)
    }

    private func formattedBirthDate(_ iso: String) -> String {
        guard let date = ISO8601DateFormatter().date(from: iso) else { return "" }
        let df = DateFormatter()
        df.dateStyle = .medium
        return df.string(from: date)
    }

    var body: some View {
        let state = observedState.value

        ZStack {
            Color(.secondarySystemBackground).ignoresSafeArea()

            VStack(spacing: 24) {
                Image(systemName: "person.crop.circle")
                    .resizable()
                    .foregroundColor(AppColors.Palette.accent.color)
                    .frame(width: 120, height: 120)
                    .padding(.top, 64)
                Spacer()

                PersonalDataFields(
                    state: state,
                    component: component,
                    showGenderPicker: $showGenderPicker,
                    showDatePicker: $showDatePicker,
                    formattedBirthDate: formattedBirthDate(state.birthDate)
                )
                .background(AppColors.Palette.componentBackground.color)
                .cornerRadius(12)
                .padding(.horizontal, 20)
                .padding(.vertical, 8)
            }
            .padding(.horizontal, 0)
            .padding(.bottom, 36)

            if showGenderPicker {
                GenderPickerOverlay(
                    genderOptions: genderOptions,
                    selectedGender: state.gender,
                    onSelect: { gender in
                        component.changeGender(gender: gender)
                        withAnimation { showGenderPicker = false }
                    },
                    onCancel: { withAnimation { showGenderPicker = false } }
                )
            }

            if showDatePicker {
                DatePickerOverlay(
                    birthDate: state.birthDate,
                    onDateChange: { isoString in component.onDataChange(date: isoString) },
                    onDone: { withAnimation { showDatePicker = false } }
                )
            }
        }
        .navigationTitle("Персональные данные")
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
                Button(action: {
                    component.onNext()
                }) {
                    HStack(spacing: 0) {
                        Text("Далее")
                            .font(.system(size: 17, weight: .semibold))
                        Image(systemName: "chevron.right")
                            .font(.system(size: 17, weight: .semibold))
                    }
                    .foregroundColor(state.isEnableContinueButton ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!state.isEnableContinueButton)
            }
        }
    }
}

private struct PersonalDataFields: View {
    let state: PersonalDataComponentState
    let component: PersonalDataComponent
    @Binding var showGenderPicker: Bool
    @Binding var showDatePicker: Bool
    let formattedBirthDate: String

    var body: some View {
        VStack(spacing: 10) {
            CustomAsteriskTextField(
                placeholder: "Фамилия",
                text: Binding(
                    get: { state.surname },
                    set: { component.changeSurname(surname: $0) }
                )
            ).padding(.top, 10)
            Divider().padding(.horizontal, 8)

            CustomAsteriskTextField(
                placeholder: "Имя",
                text: Binding(
                    get: { state.name },
                    set: { component.changeName(name: $0) }
                )
            )
            Divider().padding(.horizontal, 8)

            CustomAsteriskTextField(
                placeholder: "Отчество",
                text: Binding(
                    get: { state.patronymic },
                    set: { component.changeThirdName(thirdName: $0) }
                ),
                showAsterisk: false
            )
            Divider().padding(.horizontal, 8)

            GenderPickerButton(
                gender: state.gender,
                showGenderPicker: $showGenderPicker
            )
            Divider().padding(.horizontal, 8)

            BirthDatePickerButton(
                birthDate: state.birthDate,
                formattedBirthDate: formattedBirthDate,
                showDatePicker: $showDatePicker
            )
            Divider().padding(.horizontal, 8)

            CustomAsteriskTextField(
                placeholder: "Телефон",
                text: Binding(
                    get: { state.phoneNumber },
                    set: { component.changePhone(phone: $0) }
                ),
                keyboardType: .phonePad
            )
            Divider().padding(.horizontal, 8)

            CustomAsteriskTextField(
                placeholder: "Email",
                text: Binding(
                    get: { state.email },
                    set: { component.changeEmail(email: $0) }
                ),
                keyboardType: .emailAddress
            ).padding(.bottom, 10)
        }
    }
}

private struct GenderPickerButton: View {
    let gender: String
    @Binding var showGenderPicker: Bool

    var body: some View {
        Button(action: { withAnimation { showGenderPicker = true } }) {
            HStack {
                if gender.isEmpty {
                    HStack(spacing: 0) {
                        Text("Пол").foregroundColor(.secondary)
                        Text("*").foregroundColor(.red)
                    }
                } else {
                    Text(gender).foregroundColor(.primary)
                }
                Spacer()
                Image(systemName: "chevron.down")
                    .foregroundColor(gender.isEmpty ? Color(.systemBlue) : AppColors.Palette.inactive.color)
            }
            .contentShape(Rectangle())
            .padding(.vertical, 10)
            .padding(.horizontal, 12)
        }
        .buttonStyle(.plain)
    }
}

private struct BirthDatePickerButton: View {
    let birthDate: String
    let formattedBirthDate: String
    @Binding var showDatePicker: Bool

    var body: some View {
        Button(action: { withAnimation { showDatePicker = true } }) {
            HStack {
                if birthDate.isEmpty {
                    HStack(spacing: 0) {
                        Text("Дата рождения").foregroundColor(.secondary)
                        Text("*").foregroundColor(.red)
                    }
                } else {
                    Text(formattedBirthDate).foregroundColor(.primary)
                }
                Spacer()
                Image(systemName: "calendar")
                    .foregroundColor(birthDate.isEmpty ? Color(.systemBlue) : AppColors.Palette.inactive.color)
            }
            .contentShape(Rectangle())
            .padding(.vertical, 10)
            .padding(.horizontal, 12)
        }
        .buttonStyle(.plain)
    }
}

private struct GenderPickerOverlay: View {
    let genderOptions: [String]
    let selectedGender: String
    let onSelect: (String) -> Void
    let onCancel: () -> Void

    var body: some View {
        Color.black.opacity(0.45)
            .ignoresSafeArea()
            .onTapGesture { onCancel() }
            .zIndex(2)
        VStack(spacing: 0) {
            Text("Выберите пол")
                .font(.headline)
                .padding(.top, 28)
                .padding(.bottom, 12)
            ForEach(genderOptions, id: \.self) { option in
                Button(action: { onSelect(option) }) {
                    HStack {
                        Text(option)
                            .foregroundColor(.primary)
                            .font(.system(size: 20, weight: .medium))
                        Spacer()
                        if selectedGender == option {
                            Image(systemName: "checkmark")
                                .foregroundColor(AppColors.Palette.accent.color)
                                .font(.system(size: 18, weight: .bold))
                        }
                    }
                    .padding(.vertical, 18)
                    .padding(.horizontal, 32)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: 300)
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
}

private struct DatePickerOverlay: View {
    let birthDate: String
    let onDateChange: (String) -> Void
    let onDone: () -> Void

    var body: some View {
        let binding = Binding<Date>(
            get: {
                if let date = ISO8601DateFormatter().date(from: birthDate) {
                    return date
                } else {
                    return Date(timeIntervalSince1970: 0)
                }
            },
            set: { newDate in
                let iso = ISO8601DateFormatter().string(from: newDate)
                onDateChange(iso)
            }
        )

        Color.black.opacity(0.45)
            .ignoresSafeArea()
            .onTapGesture { onDone() }
            .zIndex(2)
        VStack(spacing: 0) {
            Text("Выберите дату рождения")
                .font(.headline)
                .padding(.top, 28)
                .padding(.bottom, 12)
            DatePicker(
                "",
                selection: binding,
                displayedComponents: .date
            )
            .datePickerStyle(.wheel)
            .labelsHidden()
            .padding(.vertical, 0)
            .padding(.horizontal, 32)
            Spacer()
            Button("Готово") {
                onDone()
            }
            .font(.system(size: 20, weight: .bold))
            .padding(.vertical, 10)
            .padding(.horizontal, 48)
        }
        .frame(maxWidth: .infinity, maxHeight: 340)
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(AppColors.Palette.componentBackground.color)
                .shadow(radius: 10)
        )
        .padding(.horizontal, 32)
        .padding(.vertical, 120)
        .transition(.scale)
        .zIndex(3)
    }
}

struct CustomAsteriskTextField: View {
    let placeholder: String
    @Binding var text: String
    var keyboardType: UIKeyboardType = .default
    var showAsterisk: Bool = true

    var body: some View {
        ZStack(alignment: .leading) {
            if text.isEmpty {
                HStack(spacing: 2) {
                    Text("  " + placeholder)
                        .foregroundColor(.secondary)
                    if showAsterisk {
                        Text("*")
                            .foregroundColor(.red)
                    }
                }
                .padding(.leading, 4)
            }

            TextField("", text: $text)
                .keyboardType(keyboardType)
                .autocapitalization(.none)
                .padding(.vertical, 10)
                .padding(.horizontal, 12)
                .background(Color.clear)
        }
    }
}
