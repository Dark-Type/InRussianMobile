//
//  LanguageComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct LanguageComponentView: View {
    let component: LanguageComponent

    @State private var expanded: Bool = false
    @State private var selectedLanguage: String
    @State private var hasPermission: Bool

    private let languages = ["RUSSIAN", "ENGLISH", "CHINESE", "UZBEK", "TAJIK", "HINDI"]

    init(component: LanguageComponent) {
        self.component = component
        _selectedLanguage = State(initialValue: component.state.selectedLanguage)
        _hasPermission = State(initialValue: component.state.hasGivenPermission)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Выберите язык")
            
            Menu {
                ForEach(languages, id: \.self) { lang in
                    Button(action: {
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
            
            HStack {
                CheckboxView(isChecked: $hasPermission, label: "Я даю согасие на обработку персональных данных")
            }

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
