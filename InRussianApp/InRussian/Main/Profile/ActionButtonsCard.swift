//
//  ActionButtonsCard.swift
//  InRussian
//
//  Created by dark type on 26.08.2025.
//

import SwiftUI
import Shared
struct ActionButtonsCard: View {
    let state: ProfileMainState
    let component: ProfileMainComponent

    @State private var showThemePicker = false

    private var themeOptions: [AppTheme] { [.system, .light, .dark] }

    var body: some View {
        VStack(spacing: 0) {
            RowContainer {
                Text("Уведомления")
                Spacer()
                Toggle("", isOn: Binding(
                    get: { state.notificationsEnabled },
                    set: { newValue in component.onNotificationSwitchClick(enable: newValue) }
                ))
                .labelsHidden()
                .tint(.accent)
            }

            DividerRow()

    
            RowButton(title: "Тема: \(state.theme.readable())", systemImage: "paintbrush") {
                showThemePicker = true
            }

            DividerRow()

            RowButton(title: "Редактировать профиль") {
                component.onEditClick()
            }

            DividerRow()

            RowButton(title: "Показать онбординг") {
                component.onShowOnboarding()
            }

            DividerRow()

            RowButton(title: "О приложении") {
                component.onAboutClick()
            }

            DividerRow()

            RowButton(title: "Политика конфиденциальности") {
                component.onPrivacyPolicyClick()
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 8)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(Color(uiColor: .componentBackground))
                .shadow(color: .black.opacity(0.05), radius: 3, y: 1)
        )
        .sheet(isPresented: $showThemePicker) {
            ThemePickerSheet(
                current: state.theme,
                options: themeOptions,
                onSelect: { theme in
                    component.onSelectTheme(theme: theme)
                    showThemePicker = false
                },
                onCancel: { showThemePicker = false }
            )
            .presentationDetents([.medium])
        }
    }
}
private struct RowContainer<Content: View>: View {
    @ViewBuilder let content: Content
    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            content
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.vertical, 6)
    }
}

private struct RowButton: View {
    let title: String
    var systemImage: String? = nil
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 10) {
                if let systemImage {
                    Image(systemName: systemImage)
                        .foregroundColor(.accent)
                }
                Text(title)
                    .foregroundColor(.primary)
                Spacer()
                Image(systemName: "chevron.right")
                    .font(.footnote)
                    .foregroundColor(.secondary)
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .padding(.vertical, 12)
    }
}

private struct DividerRow: View {
    var body: some View {
        Divider()
            .padding(.horizontal, 0)
            .overlay(AppColors.Palette.stroke.color)
    }
}
private struct ThemePickerSheet: View {
    let current: AppTheme
    let options: [AppTheme]
    let onSelect: (AppTheme) -> Void
    let onCancel: () -> Void

    @State private var selected: AppTheme

    init(current: AppTheme, options: [AppTheme], onSelect: @escaping (AppTheme) -> Void, onCancel: @escaping () -> Void) {
        self.current = current
        self.options = options
        self.onSelect = onSelect
        self.onCancel = onCancel
        _selected = State(initialValue: current)
    }

    var body: some View {
        NavigationView {
            List {
                ForEach(options, id: \.self) { theme in
                    HStack {
                        Text(theme.readable())
                        Spacer()
                        if theme == selected {
                            Image(systemName: "checkmark")
                                .foregroundColor(.accentColor)
                        }
                    }
                    .contentShape(Rectangle())
                    .onTapGesture { selected = theme }
                }
            }
            .navigationTitle("Тема")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Отмена", action: onCancel)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Готово") { onSelect(selected) }
                }
            }
        }
    }
}
