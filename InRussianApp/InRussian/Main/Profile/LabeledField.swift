//
//  LabeledField.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

 struct LabeledField: View {
    let label: String
    @State private var textValue: String
    let onChange: (String) -> Void

    init(_ label: String, text: String, onChange: @escaping (String) -> Void) {
        self.label = label
        _textValue = State(initialValue: text)
        self.onChange = onChange
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)
            TextField("", text: Binding(
                get: { textValue },
                set: { newValue in
                    textValue = newValue
                    onChange(newValue)
                }
            ))
            .textFieldStyle(.roundedBorder)
            .font(.body)
        }
    }
}
