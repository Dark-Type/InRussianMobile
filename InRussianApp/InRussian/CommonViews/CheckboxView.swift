//
//  CheckboxView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI

struct CheckboxView: View {
    @Binding var isChecked: Bool
    var label: String

    var body: some View {
        Button(action: { isChecked.toggle() }) {
            HStack {
                Text(label)
                    .foregroundColor(.primary)
                
                AppImages.Checkbox.toggle(isChecked)
                    .resizable()
                    .frame(width: 24, height: 24)
            }
        }
        .buttonStyle(.plain)
    }
}
#Preview {
    CheckboxView(isChecked: Binding.constant(true), label: "String")
}
