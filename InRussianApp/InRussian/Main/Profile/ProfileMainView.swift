//
//  ProfileMainView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//


import SwiftUI
import Shared

struct ProfileMainView: View {
    let component: ProfileMainComponent
    @StateValue private var state: ProfileMainState

    init(component: ProfileMainComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        if state.isLoading {
            ProgressCentered()
                .padding(.top, 60)
        } else {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    header
                    badges
                    actionButtons
                }
                .padding(16)
            }
        }
    }

    
    private var header: some View {
        HStack(alignment: .top, spacing: 16) {
            AvatarView(avatarId: state.user?.avatarId)
            VStack(alignment: .leading, spacing: 10) {
                Text(state.displayName.isEmpty ? "—" : state.displayName)
                    .font(.title3).bold()
                Text("Дата регистрации: \(state.registrationDate)")
                    .font(.footnote)
                    .foregroundColor(.secondary)

                Toggle(isOn: Binding(
                    get: { state.notificationsEnabled },
                    set: { _ in component.toggleNotifications() }
                )) {
                    Text("Уведомления")
                }
                .toggleStyle(.switch)

                Button {
                    component.cycleTheme()
                } label: {
                    Label("Тема: \(state.theme.readable())", systemImage: "paintbrush")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
        }
    }

    private var badges: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Значки")
                .font(.title3).bold()
            if state.badges.isEmpty {
                Text("Пока нет значков")
                    .foregroundColor(.secondary)
            } else {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 16) {
                        ForEach(state.badges, id: \.id) { badge in
                            BadgeView(badge: badge)
                        }
                    }
                }
            }
        }
    }

    private var actionButtons: some View {
        VStack(spacing: 12) {
            Button {
                component.onEditClick()
            } label: {
                Text("Редактировать профиль")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)

            Button {
                component.onAboutClick()
            } label: {
                Text("О приложении")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)

            Button {
                component.onPrivacyPolicyClick()
            } label: {
                Text("Политика конфиденциальности")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
    }
}
