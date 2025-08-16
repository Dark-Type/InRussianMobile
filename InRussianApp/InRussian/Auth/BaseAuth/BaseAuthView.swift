//
//  BaseAuthView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared

struct BaseAuthView: View {
    let component: BaseAuthComponent

    var body: some View {
        VStack {
            Spacer()
                .frame(maxHeight: .infinity)
                .layoutPriority(1)

            Button(action: {
                component.onLoginClicked()
            }) {
                Text("Войти")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .padding(.bottom, 8)

            Button(action: {
                component.onRegisterClicked()
            }) {
                Text("Регистрация")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
            .padding(.bottom, 8)

            Button(action: {
                component.onSsoClicked()
            }) {
                Text("Войти через SSO")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
        .padding(16)
    }
}
