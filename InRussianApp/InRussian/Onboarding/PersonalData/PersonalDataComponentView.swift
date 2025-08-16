//
//  PersonalDataComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//
import SwiftUI
import Shared

struct PersonalDataComponentView: View {
    let component: PersonalDataComponent

    @State private var name: String
    @State private var surname: String
    @State private var patronymic: String
    @State private var gender: String
    @State private var birthDate: String
    @State private var phoneNumber: String
    @State private var email: String

    init(component: PersonalDataComponent) {
        self.component = component
        
        _name = State(initialValue: component.state.name)
        _surname = State(initialValue: component.state.surname)
        _patronymic = State(initialValue: component.state.patronymic)
        _gender = State(initialValue: component.state.gender)
        _birthDate = State(initialValue: component.state.birthDate)
        _phoneNumber = State(initialValue: component.state.phoneNumber)
        _email = State(initialValue: component.state.email)
    }

    var body: some View {
        VStack(alignment: .leading) {
            Text("Введите персональные данные")
                .font(.headline)
            Spacer().frame(height: 8)

            Group {
                TextField("Имя", text: $name)
                Spacer().frame(height: 8)
                TextField("Фамилия", text: $surname)
                Spacer().frame(height: 8)
                TextField("Отчество", text: $patronymic)
                Spacer().frame(height: 8)
                TextField("Пол", text: $gender)
                Spacer().frame(height: 8)
                TextField("Дата рождения", text: $birthDate)
                Spacer().frame(height: 8)
                TextField("Телефон", text: $phoneNumber)
                Spacer().frame(height: 8)
                TextField("Email", text: $email)
            }
            .textFieldStyle(.roundedBorder)
            .frame(maxWidth: .infinity)

            Spacer().frame(height: 16)

            Button(action: {
                component.onNext()
            }) {
                Text("Далее")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
        }
        .padding(16)
    }
}
