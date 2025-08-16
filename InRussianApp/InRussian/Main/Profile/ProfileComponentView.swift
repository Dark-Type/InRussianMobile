//
//  ProfileComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct ProfileComponentView: View {
    let component: ProfileComponent

    @StateValue private var stack: ChildStack<AnyObject, any ProfileComponentChild>

    init(component: ProfileComponent) {
        self.component = component
        _stack = StateValue(component.stack)
    }

    var body: some View {
        let current = stack.active.instance

        Group {
            if let main = current as? ProfileComponentChildMainChild {
                VStack {
                    Spacer()
                        .frame(maxHeight: .infinity)
                    Button(action: { main.component.onEditClick() }) {
                        Text("Редактировать профиль")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.borderedProminent)
                    Button(action: { main.component.onAboutClick() }) {
                        Text("О приложении")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                    Button(action: { main.component.onPrivacyPolicyClick() }) {
                        Text("Политика конфиденциальности")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                    Spacer()
                }
                .padding()
            } else if let edit = current as? ProfileComponentChildEditProfileChild {
                VStack(spacing: 16) {
                    Text("Редактирование профиля")
                        .font(.headline)
                    Button(action: { edit.component.onBack() }) {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
                .padding()
            } else if let about = current as? ProfileComponentChildAboutChild {
                VStack(spacing: 16) {
                    Text("О приложении")
                        .font(.headline)
                    Button(action: { about.component.onBack() }) {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
                .padding()
            } else if let privacy = current as? ProfileComponentChildPrivacyPolicyChild {
                VStack(spacing: 16) {
                    Text("Политика конфиденциальности")
                        .font(.headline)
                    Button(action: { privacy.component.onBack() }) {
                        Text("Назад")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
                .padding()
            } else {
                EmptyView()
            }
        }
    }
}
