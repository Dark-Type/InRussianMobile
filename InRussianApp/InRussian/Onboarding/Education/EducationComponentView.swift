//
//  EducationComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct EducationComponentView: View {
    let component: EducationComponent

    private let languageOptions = [
        "Русский", "Английский", "Китайский", "Узбекский", "Таджикский",
        "Хинди", "Казахский", "Французский", "Немецкий", "Испанский"
    ]

    @State private var expanded: Bool = false
    @State private var selectedLanguage: String = ""
    @State private var languages: [String]
    @State private var understandsRussian: Bool
    @State private var speaksRussian: Bool
    @State private var readsRussian: Bool
    @State private var writesRussian: Bool
    @State private var kindOfActivity: String
    @State private var education: String
    @State private var purposeOfRegistration: String

    init(component: EducationComponent) {
        self.component = component
        _languages = State(initialValue: component.state.languages.compactMap { $0 as? String })
        _understandsRussian = State(initialValue: component.state.understandsRussian)
        _speaksRussian = State(initialValue: component.state.speaksRussian)
        _readsRussian = State(initialValue: component.state.readsRussian)
        _writesRussian = State(initialValue: component.state.writesRussian)
        _kindOfActivity = State(initialValue: component.state.kindOfActivity)
        _education = State(initialValue: component.state.education)
        _purposeOfRegistration = State(initialValue: component.state.purposeOfRegistration)
    }

    var body: some View {
        VStack(alignment: .leading) {
            Text("Выберите языки, которыми владеете")
                .font(.headline)
            Spacer().frame(height: 8)

            Menu {
                ForEach(languageOptions, id: \.self) { lang in
                    Button(action: {
                        if !languages.contains(lang) {
                            languages.append(lang)
                        }
                        selectedLanguage = lang
                    }) {
                        Text(lang)
                    }
                }
            } label: {
                HStack {
                    Text(selectedLanguage.isEmpty ? "Язык" : selectedLanguage)
                        .foregroundColor(selectedLanguage.isEmpty ? .gray : .primary)
                    Spacer()
                    Image(systemName: "chevron.down")
                        .foregroundColor(.gray)
                }
                .padding(8)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.gray.opacity(0.5))
                )
            }

            Spacer().frame(height: 8)
            Text("Выбранные языки: \(languages.joined(separator: ", "))")
                .font(.subheadline)

            Spacer().frame(height: 16)

            
            HStack {
                CheckboxView(isChecked: $understandsRussian, label: "Понимает русский")
            }
            HStack {
                CheckboxView(isChecked: $speaksRussian, label: "Говорит по-русски")
            }
            HStack {
                CheckboxView(isChecked: $readsRussian, label: "Читает по-русски")
            }
            HStack {
                CheckboxView(isChecked: $writesRussian, label: "Пишет по-русски")
            }

            Spacer().frame(height: 16)

            TextField("Вид деятельности", text: $kindOfActivity)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)
            Spacer().frame(height: 8)
            TextField("Образование", text: $education)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)
            Spacer().frame(height: 8)
            TextField("Цель регистрации", text: $purposeOfRegistration)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 16)
            HStack {
                Button(action: {
                    component.onBack()
                }) {
                    Text("Назад")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)

                Spacer().frame(width: 8)

                Button(action: {
                    component.onNext()
                }) {
                    Text("Далее")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
            }
        }
        .padding(16)
    }
}
