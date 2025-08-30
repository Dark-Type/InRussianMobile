//
//  StaticTextScreen.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI
import Shared

struct StaticTextScreenView: View {
    @StateValue private var state: StaticTextState
    let onBack: () -> Void

    init(component: AboutComponent, onBack: @escaping () -> Void) {
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    init(component: PrivacyPolicyComponent, onBack: @escaping () -> Void) {
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Text(state.title)
                        .font(.title2).bold()
                    Text(state.text)
                        .font(.body)
                        .foregroundColor(.secondary)
                        .padding(16)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .fill(Color(uiColor: .systemBackground))
                                .shadow(color: .black.opacity(0.05), radius: 2, y: 1)
                        )
                }
                .padding(16)
                .background(Color(uiColor: .systemGroupedBackground))
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button(action: onBack) {
                        HStack(spacing: 6) {
                            Image(systemName: "chevron.left")
                            Text("Назад")
                        }
                    }
                }
            }
        }
    }
}
