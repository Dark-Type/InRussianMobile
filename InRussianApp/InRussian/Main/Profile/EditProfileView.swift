//
//  EditProfileView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import Shared
import SwiftUI

struct EditProfileView: View {
    let component: EditProfileComponent
    @StateValue private var state: EditProfileState

    @State private var showGenderPicker = false
    @State private var showDatePicker = false
    @State private var showCitizenshipPicker = false
    @State private var showNationalityPicker = false
    @State private var showLanguagesPicker = false

    private let nationalityOptions = [
        "Русский", "Казах", "Узбек", "Таджик", "Китаец", "Киргиз", "Гражданин мира"
    ].sorted()

    private let countryOptions = [
        "Россия", "Казахстан", "Китай", "Киргизия", "Таджикистан", "Узбекистан"
    ].sorted()

    init(component: EditProfileComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading || state.workingCopy == nil {
            ProgressCentered().padding(.top, 60)
        } else {
            let working = state.workingCopy!

            ZStack {
                ScrollView {
                    VStack(spacing: 12) {
                        header

                        // Language row
                        CardRow {
                            HStack {
                                Text("Язык приложения")
                                    .font(.system(size: 16))
                                Spacer()
                                let lang = working.language
                                Text(lang.name)
                                    .font(.system(size: 16))

                                Button {
                                    component.openLanguage()
                                } label: {
                                    Image(systemName: "globe")
                                        .foregroundColor(Color(.systemBlue))
                                }
                                .buttonStyle(.plain)
                            }
                            .frame(minHeight: 48)
                            .padding(.horizontal, 8)
                        }

                        sectionTitle("Персональные данные")

                        VStack(spacing: 0) {
                            CustomAsteriskTextField(
                                placeholder: "Фамилия",
                                text: Binding(
                                    get: { working.surname },
                                    set: { component.updateSurname(value: $0) }
                                )
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Имя",
                                text: Binding(
                                    get: { working.name },
                                    set: { component.updateName(value: $0) }
                                )
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Отчество",
                                text: Binding(
                                    get: { working.patronymic ?? "" },
                                    set: { component.updatePatronymic(value: $0) }
                                ),
                                showAsterisk: false
                            )
                            Divider().padding(.horizontal, 8)

                            // Gender (picker-like)
                            PickerLikeRow(
                                label: labelForPicker(current: working.gender.name, placeholder: "Пол"),
                                icon: "chevron.down",
                                isEmpty: (working.gender.name).isEmpty,
                                inactiveColor: AppColors.Palette.inactive.color
                            ) { withAnimation { showGenderPicker = true } }
                                .padding(.horizontal, 8)
                            Divider().padding(.horizontal, 8)

                            // Date of birth (picker-like)
                            PickerLikeRow(
                                label: labelForPicker(current: working.dob, placeholder: "Дата рождения"),
                                icon: "calendar",
                                isEmpty: (working.dob).isEmpty,
                                inactiveColor: AppColors.Palette.inactive.color
                            ) { withAnimation { showDatePicker = true } }
                                .padding(.horizontal, 8)
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Телефон",
                                text: Binding(
                                    get: { working.dor }, // phone (dor)
                                    set: { component.updateDor(value: $0) }
                                ),
                                keyboardType: .phonePad
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Email",
                                text: Binding(
                                    get: { working.email },
                                    set: { component.updateEmail(value: $0) }
                                ),
                                keyboardType: .emailAddress
                            )
                        }
                        .padding(.vertical, 8)
                        .background(CardBackground())

                        sectionTitle("Гражданство и проживание")

                        // Citizenship + residence card
                        VStack(spacing: 0) {
                            // Citizenship chips (multi-select)
                            CitizenshipChipField(
                                placeholder: Text("Гражданство").foregroundColor(.secondary) + Text("*").foregroundColor(.red),
                                selections: Binding(
                                    get: { working.citizenship ?? [] },
                                    set: { newList in
                                        let old = working.citizenship ?? []
                                        // removals
                                        old.filter { !newList.contains($0) }
                                            .forEach { component.deleteCountry(country: $0) }
                                        // additions
                                        newList.filter { !old.contains($0) }
                                            .forEach { component.selectCountry(country: $0) }
                                    }
                                ),
                                showPicker: $showCitizenshipPicker,
                                accent: AppColors.Palette.accent.color,
                                backgroundColor: AppColors.Palette.componentBackground.color
                            )
                            .padding(.horizontal, 8)
                            Divider().padding(.horizontal, 8)

                            PickerLikeRow(
                                label: labelForPicker(current: working.nationality ?? "", placeholder: "Национальность"),
                                icon: "chevron.down",
                                isEmpty: (working.nationality ?? "").isEmpty,
                                inactiveColor: AppColors.Palette.inactive.color
                            ) { withAnimation { showNationalityPicker = true } }
                                .padding(.horizontal, 8)
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Страна проживания",
                                text: Binding(
                                    get: { working.countryOfResidence ?? "" },
                                    set: { component.countryLiveUpdate(county: $0) }
                                )
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Город проживания",
                                text: Binding(
                                    get: { working.cityOfResidence ?? "" },
                                    set: { component.cityLiveUpdate(county: $0) }
                                )
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Страна во время обучения",
                                text: Binding(
                                    get: { working.countryDuringEducation ?? "" },
                                    set: { component.studyCountryUpdate(county: $0) }
                                )
                            )
                        }
                        .padding(.vertical, 8)
                        .background(CardBackground())

                        sectionTitle("Владение языками")

                        // Languages + skills (simple)
                        VStack(spacing: 0) {
                            CitizenshipChipField(
                                placeholder: Text("Языки").foregroundColor(.secondary) + Text("*").foregroundColor(.red),
                                selections: Binding(
                                    get: { working.languages },
                                    set: { newList in
                                        let old = working.languages
                                        old.filter { !newList.contains($0) }
                                            .forEach { component.deleteLanguage(language: $0) }
                                        newList.filter { !old.contains($0) }
                                            .forEach { component.selectLanguage(language: $0) }
                                    }
                                ),
                                showPicker: $showLanguagesPicker,
                                accent: AppColors.Palette.accent.color,
                                backgroundColor: AppColors.Palette.componentBackground.color
                            )
                            .padding(.horizontal, 8)

                            // If you want additional toggles here, add below with Dividers
                        }
                        .padding(.vertical, 8)
                        .background(CardBackground())

                        sectionTitle("Образование")

                        VStack(spacing: 0) {
                            CustomAsteriskTextField(
                                placeholder: "Вид деятельности",
                                text: Binding(
                                    get: { working.kindOfActivity ?? "" },
                                    set: { component.changeActivity(activity: $0) }
                                ),
                                showAsterisk: false
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Образование",
                                text: Binding(
                                    get: { working.education ?? "" },
                                    set: { component.changeEducation(education: $0) }
                                ),
                                showAsterisk: false
                            )
                            Divider().padding(.horizontal, 8)

                            CustomAsteriskTextField(
                                placeholder: "Цель регистрации",
                                text: Binding(
                                    get: { working.purposeOfRegister ?? "" },
                                    set: { component.changePurpose(purpose: $0) }
                                ),
                                showAsterisk: false
                            )
                        }
                        .padding(.vertical, 8)
                        .background(CardBackground())

                        Button {
                            component.save()
                        } label: {
                            Text("Сохранить")
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)
                        .padding(.top, 8)

                        Spacer().frame(height: 86)
                    }
                    .padding(16)
                    .background(Color(uiColor: .secondarySystemBackground))
                    .ignoresSafeArea(.all)
                }

                // Overlays

                if showGenderPicker {
                    DimmedModalBackground { withAnimation { showGenderPicker = false } }
                    GenderPickerOverlay(
                        genderOptions: ["МУЖСКОЙ", "ЖЕНСКИЙ"], // show localized options
                        selectedGender: working.gender.name, // String (guaranteed)
                        onSelect: { selectedName in // String -> map to Gender
                            let genderEnum = mapGender(from: selectedName)
                            component.updateGender(value: genderEnum) // pass Gender to component
                            withAnimation { showGenderPicker = false }
                        },
                        onCancel: { withAnimation { showGenderPicker = false } }
                    )
                }
                if showDatePicker {
                    DimmedModalBackground { withAnimation { showDatePicker = false } }
                    DatePickerOverlay(
                        birthDate: working.dob,
                        onDateChange: { iso in component.updateDob(value: iso) },
                        onDone: { withAnimation { showDatePicker = false } }
                    )
                }

                if showCitizenshipPicker {
                    DimmedModalBackground { withAnimation { showCitizenshipPicker = false } }
                    OptionListModal(
                        title: "Добавить гражданство",
                        options: countryOptions,
                        selected: Set((working.citizenship) ?? []),
                        allowsMultiple: true,
                        accent: AppColors.Palette.accent.color,
                        onSelect: { country in component.selectCountry(country: country) },
                        onRemove: { country in component.deleteCountry(country: country) },
                        onDone: { withAnimation { showCitizenshipPicker = false } }
                    )
                }

                if showNationalityPicker {
                    DimmedModalBackground { withAnimation { showNationalityPicker = false } }
                    OptionListModal(
                        title: "Национальность",
                        options: nationalityOptions,
                        selected: Set((working.nationality ?? "").isEmpty ? [] : [working.nationality!]),
                        allowsMultiple: false,
                        accent: AppColors.Palette.accent.color,
                        onSelect: { choice in
                            component.selectNationality(nationality: choice)
                            withAnimation { showNationalityPicker = false }
                        },
                        onRemove: { _ in },
                        onDone: { withAnimation { showNationalityPicker = false } }
                    )
                }

                if showLanguagesPicker {
                    DimmedModalBackground { withAnimation { showLanguagesPicker = false } }
                    OptionListModal(
                        title: "Добавить язык",
                        options: [
                            "Русский", "Английский", "Китайский", "Узбекский", "Таджикский", "Казахский"
                        ],
                        selected: Set((working.languages as [String])),
                        allowsMultiple: true,
                        accent: AppColors.Palette.accent.color,
                        onSelect: { lang in component.selectLanguage(language: lang) },
                        onRemove: { lang in component.deleteLanguage(language: lang) },
                        onDone: { withAnimation { showLanguagesPicker = false } }
                    )
                }
            }
        }
    }

    // MARK: - Subviews

    private var header: some View {
        VStack(spacing: 16) {
            HStack {
                Button(action: { component.onBack() }) {
                    Image(systemName: "chevron.left")
                    Text("Назад")
                }
                .foregroundColor(.accent)
                Spacer()
            }
            Image(systemName: "person.crop.circle.fill")
                .resizable()
                .scaledToFit()
                .frame(width: 120, height: 120)
                .foregroundColor(.accent)

            Text("Редактировать профиль")
                .font(.title2).bold()
                .multilineTextAlignment(.center)
                .padding(.horizontal, 30)

            Spacer(minLength: 24)
        }
        .padding(.top, 38)
    }

    private func sectionTitle(_ title: String) -> some View {
        HStack {
            Text(title)
                .foregroundColor(.secondary)
            Spacer()
        }
        .padding(.top, 8)
    }

    private func labelForPicker(current: String, placeholder: String) -> Text {
        if current.isEmpty {
            return Text(placeholder).foregroundColor(.secondary) + Text("*").foregroundColor(.red)
        } else {
            return Text(current).foregroundColor(.primary)
        }
    }
}

// MARK: - Small helpers

private struct CardBackground: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 12)
            .fill(Color(uiColor: .componentBackground))
            .shadow(color: .black.opacity(0.05), radius: 1, y: 1)
    }
}

private struct CardRow<Content: View>: View {
    @ViewBuilder let content: Content
    var body: some View {
        HStack { content }
            .padding(.vertical, 8)
            .padding(.horizontal, 12)
            .background(CardBackground())
            .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}

func mapGender(from label: String) -> Gender {
    let upper = label.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
    if upper == "МУЖСКОЙ" || upper == "MALE" {
        return Gender.male
    } else {
        return Gender.female
    }
}
