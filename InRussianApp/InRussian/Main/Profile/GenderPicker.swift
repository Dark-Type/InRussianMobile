//
//  GenderPicker.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI
import Shared

 struct GenderPicker: View {
    let selected: Gender
    let onSelect: (Gender) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("Пол")
                .font(.caption)
                .foregroundColor(.secondary)
            FlowLayout(spacing: 8, runSpacing: 8) {
                ForEach(Gender.entries, id: \.self) { g in
                    let isSel = g == selected
                    Button {
                        onSelect(g)
                    } label: {
                        Text(g.name)
                            .font(.footnote)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(
                                Capsule().fill(isSel ? Color.accentColor : Color.gray.opacity(0.2))
                            )
                            .foregroundColor(isSel ? .white : .primary)
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}
