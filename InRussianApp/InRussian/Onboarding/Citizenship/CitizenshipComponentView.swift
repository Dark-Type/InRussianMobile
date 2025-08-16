//
//  CitizenshipComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared
struct CitizenshipComponentView: View {
    let component: CitizenshipComponent

    private let countries = [
        "Россия", "Казахстан", "Узбекистан", "Таджикистан", "Киргизия",
        "Беларусь", "Армения", "Грузия", "Китай", "Индия"
    ]

    @State private var expanded: Bool = false
    @State private var selectedCountry: String = ""
    @State private var citizenships: [String]
    @State private var nationality: String
    @State private var countryOfResidence: String
    @State private var cityOfResidence: String
    @State private var countryDuringEducation: String
    @State private var timeSpentInRussia: String

    init(component: CitizenshipComponent) {
        self.component = component
        _citizenships = State(initialValue: component.state.citizenship.compactMap { $0 as? String })
        _nationality = State(initialValue: component.state.nationality)
        _countryOfResidence = State(initialValue: component.state.countryOfResidence)
        _cityOfResidence = State(initialValue: component.state.cityOfResidence)
        _countryDuringEducation = State(initialValue: component.state.countryDuringEducation)
        _timeSpentInRussia = State(initialValue: component.state.timeSpentInRussia)
    }

    var body: some View {
        VStack(alignment: .leading) {
            Text("Выберите гражданство")
                .font(.headline)
            Spacer().frame(height: 8)

            
            Menu {
                ForEach(countries, id: \.self) { country in
                    Button(action: {
                        if !citizenships.contains(country) {
                            citizenships.append(country)
                        }
                        selectedCountry = country
                    }) {
                        Text(country)
                    }
                }
            } label: {
                HStack {
                    Text(selectedCountry.isEmpty ? "Страна" : selectedCountry)
                        .foregroundColor(selectedCountry.isEmpty ? .gray : .primary)
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
            Text("Ваши гражданства: \(citizenships.joined(separator: ", "))")
                .font(.subheadline)

            Spacer().frame(height: 16)
            TextField("Национальность", text: $nationality)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)
            TextField("Страна проживания", text: $countryOfResidence)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)
            TextField("Город проживания", text: $cityOfResidence)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)
            TextField("Страна во время обучения", text: $countryDuringEducation)
                .textFieldStyle(.roundedBorder)
                .frame(maxWidth: .infinity)

            Spacer().frame(height: 8)
            TextField("Время, проведённое в России", text: $timeSpentInRussia)
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
