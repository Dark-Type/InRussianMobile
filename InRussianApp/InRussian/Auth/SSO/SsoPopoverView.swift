//
//  SsoPopoverView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct SsoPopoverView: View {
    let component: SsoPopoverComponent

    var body: some View {
        VStack {
            Spacer()
                .frame(maxHeight: .infinity)
                .layoutPriority(1)

            Text("Войти через SSO")
                .font(.body)
                .foregroundColor(.secondary)

            Spacer().frame(height: 16)

            Button(action: {
                component.onAuthenticationSuccess()
            }) {
                Text("Войти")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)

            Spacer().frame(height: 8)

            Button(action: {
                component.onBackClicked()
            }) {
                Text("Назад")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
        .padding(16)
    }
}
