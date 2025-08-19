//
//  ProgressCentered.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI

func ProgressCentered() -> some View {
    AnyView(
        VStack {
            ProgressView()
            Text("Загрузка...")
                .font(.footnote)
                .foregroundColor(.secondary)
                .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    )
}
