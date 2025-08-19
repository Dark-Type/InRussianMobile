//
//  EditProfileView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI
import Shared

struct EditProfileView: View {
    let component: EditProfileComponent
    @StateValue private var state: EditProfileState

    init(component: EditProfileComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading || state.workingCopy == nil {
            ProgressCentered()
                .padding(.top, 60)
        } else {
            let working = state.workingCopy!
            ScrollView {
                VStack(alignment: .leading, spacing: 14) {
                    Text("Редактирование профиля")
                        .font(.title2).bold()

                    Group {
                        LabeledField("Фамилия", text: working.surname) { component.updateSurname(value: $0) }
                        LabeledField("Имя", text: working.name) { component.updateName(value: $0) }
                        LabeledField("Отчество", text: working.patronymic ?? "") { component.updatePatronymic(value: $0) }
                        LabeledField("Дата рождения (YYYY-MM-DD)", text: working.dob) { component.updateDob(value: $0) }
                        LabeledField("Гражданство", text: working.citizenship ?? "") { component.updateCitizenship(value: $0) }
                        LabeledField("Национальность", text: working.nationality ?? "") { component.updateNationality(value: $0) }
                        LabeledField("Страна проживания", text: working.countryOfResidence ?? "") { component.updateCountryOfResidence(value: $0) }
                        LabeledField("Город проживания", text: working.cityOfResidence ?? "") { component.updateCityOfResidence(value: $0) }
                        LabeledField("Образование", text: working.education ?? "") { component.updateEducation(value: $0) }
                        LabeledField("Цель регистрации", text: working.purposeOfRegister ?? "") { component.updatePurpose(value: $0) }
                    }

                    GenderPicker(selected: working.gender) {
                        component.updateGender(value: $0)
                    }

                    HStack(spacing: 12) {
                        Button {
                            component.onBack()
                        } label: {
                            Text("Назад")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.bordered)

                        Button {
                            component.save()
                        } label: {
                            if state.isSaving {
                                ProgressView().progressViewStyle(.circular)
                                    .frame(maxWidth: .infinity)
                            } else {
                                Text("Сохранить")
                                    .frame(maxWidth: .infinity)
                            }
                        }
                        .buttonStyle(.borderedProminent)
                        .disabled(!state.canSave || state.isSaving)
                    }
                    .padding(.top, 4)
                }
                .padding(16)
            }
        }
    }
}
