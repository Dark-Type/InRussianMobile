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

    @State private var name: String
    @State private var surname: String
    @State private var patronymic: String
    @State private var gender: String
    @State private var birthDate: Date?
    @State private var phoneNumber: String
    @State private var email: String

    @State private var showGenderPicker = false
    @State private var showDatePicker = false

    private let genderOptions = ["Мужской", "Женский"]

    init(component: PersonalDataComponent) {
        self.component = component
        _name = State(initialValue: component.state.name)
        _surname = State(initialValue: component.state.surname)
        _patronymic = State(initialValue: component.state.patronymic)
        _gender = State(initialValue: component.state.gender)
        _phoneNumber = State(initialValue: component.state.phoneNumber)
        _email = State(initialValue: component.state.email)
        if let isoDate = ISO8601DateFormatter().date(from: component.state.birthDate) {
            _birthDate = State(initialValue: isoDate)
        } else {
            _birthDate = State(initialValue: nil)
        }
    }

    private var isFormFilled: Bool {
        !name.isEmpty &&
            !surname.isEmpty &&
            !patronymic.isEmpty &&
            !gender.isEmpty &&
            birthDate != nil &&
            !phoneNumber.isEmpty &&
            !email.isEmpty
    }

    private func formattedBirthDate() -> String {
        guard let birthDate else { return "" }
        let df = DateFormatter()
        df.dateStyle = .medium
        return df.string(from: birthDate)
    }

    private var genderLabel: Text {
        if gender.isEmpty {
            return Text("Пол").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(gender).foregroundColor(.primary)
        }
    }

    private var birthDateLabel: Text {
        if birthDate == nil {
            return Text("Дата рождения").foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(formattedBirthDate()).foregroundColor(.primary)
        }
    }

    var body: some View {
        ZStack {
            Color(.secondarySystemBackground).ignoresSafeArea()

            VStack(spacing: 24) {
                
                Image(systemName: "person.crop.circle")
                    .resizable()
                    .foregroundColor(AppColors.Palette.accent.color)
                    .frame(width: 120, height: 120)
                    .padding(.top, 64)
                Spacer()

                Form {
                    Section {
                        CustomAsteriskTextField(placeholder: "Имя", text: $name)
                            .autocapitalization(.words)
                            .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(placeholder: "Фамилия", text: $surname)
                            .autocapitalization(.words)
                            .listRowBackground(AppColors.Palette.componentBackground.color)

                        TextField("Отчество", text: $patronymic)
                            .autocapitalization(.words)
                            .listRowBackground(AppColors.Palette.componentBackground.color)

                        Button(action: { withAnimation { showGenderPicker = true } }) {
                            HStack {
                                genderLabel
                                Spacer()
                                Image(systemName: "chevron.down")
                                    .foregroundColor(gender.isEmpty ? Color(.systemBlue) : AppColors.Palette.inactive.color)
                            }
                        }
                        .buttonStyle(.plain)
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        Button(action: { withAnimation { showDatePicker = true } }) {
                            HStack {
                                birthDateLabel
                                Spacer()
                                Image(systemName: "calendar")
                                    .foregroundColor(birthDate == nil ? Color(.systemBlue) : AppColors.Palette.inactive.color)
                            }
                        }
                        .buttonStyle(.plain)
                        .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(placeholder: "Телефон", text: $phoneNumber, keyboardType: .phonePad)
                            .listRowBackground(AppColors.Palette.componentBackground.color)

                        CustomAsteriskTextField(placeholder: "Email", text: $email, keyboardType: .emailAddress)
                            .listRowBackground(AppColors.Palette.componentBackground.color)
                    }
                }
                .scrollContentBackground(.hidden)
                .background(Color.clear)
                .cornerRadius(0)
                .padding(.horizontal, 0)
                .padding(.bottom, 36)

            }

            if showGenderPicker {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .onTapGesture {
                        withAnimation { showGenderPicker = false }
                    }
                    .zIndex(2)
                VStack(spacing: 0) {
                    Text("Выберите пол")
                        .font(.headline)
                        .padding(.top, 28)
                        .padding(.bottom, 12)
                    ForEach(genderOptions, id: \.self) { option in
                        Button(action: {
                            gender = option
                            withAnimation { showGenderPicker = false }
                        }) {
                            HStack {
                                Text(option)
                                    .foregroundColor(.primary)
                                    .font(.system(size: 20, weight: .medium))
                                Spacer()
                                if gender == option {
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

            if showDatePicker {
                Color.black.opacity(0.45)
                    .ignoresSafeArea()
                    .onTapGesture {
                        withAnimation { showDatePicker = false }
                    }
                    .zIndex(2)
                VStack(spacing: 0) {
                    Text("Выберите дату рождения")
                        .font(.headline)
                        .padding(.top, 28)
                        .padding(.bottom, 12)
                    DatePicker(
                        "",
                        selection: Binding<Date>(
                            get: { birthDate ?? Date(timeIntervalSince1970: 0) },
                            set: { newDate in birthDate = newDate }
                        ),
                        displayedComponents: .date
                    )
                    .datePickerStyle(.wheel)
                    .labelsHidden()
                    .padding(.vertical, 0)
                    .padding(.horizontal, 32)
                    Spacer()
                    Button("Готово") {
                        withAnimation { showDatePicker = false }
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
        .navigationTitle("Персональные данные")
        .navigationBarTitleDisplayMode(.large)
        .toolbar {
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
                    .foregroundColor(isFormFilled ? AppColors.Palette.accent.color : AppColors.Palette.inactive.color)
                }
                .disabled(!isFormFilled)
            }
        }
    }
}

struct CustomAsteriskTextField: View {
    let placeholder: String
    @Binding var text: String
    var keyboardType: UIKeyboardType = .default

    var body: some View {
        ZStack(alignment: .leading) {
            if text.isEmpty {
                HStack(spacing: 0) {
                    Text(placeholder)
                        .foregroundColor(.secondary)
                    Text("*")
                        .foregroundColor(.red)
                }
                .padding(.leading, 4)
            }
            TextField("", text: $text)
                .keyboardType(keyboardType)
                .autocapitalization(.none)
        }
    }
}
