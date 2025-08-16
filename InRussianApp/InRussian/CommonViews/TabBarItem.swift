//
//  TabBarItem.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//
import SwiftUI

struct TabBarItem: View {
    let isSelected: Bool
    let icon: String
    let label: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(spacing: 2) {
                Image(systemName: icon)
                    .font(.system(size: 22, weight: isSelected ? .bold : .regular))
                    .foregroundColor(isSelected ? .accentColor : .secondary)
                Text(label)
                    .font(.caption)
                    .foregroundColor(isSelected ? .accentColor : .secondary)
            }
            .padding(.horizontal, 16)
        }
    }
}
