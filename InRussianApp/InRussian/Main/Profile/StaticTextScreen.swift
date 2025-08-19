//
//  StaticTextScreen.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI
import Shared

struct StaticTextScreen: View {
    let component: AnyObject
    let onBack: () -> Void
    @StateValue private var state: StaticTextState

    init(component: AboutComponent, onBack: @escaping () -> Void) {
        self.component = component as AnyObject
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    init(component: PrivacyPolicyComponent, onBack: @escaping () -> Void) {
        self.component = component as AnyObject
        _state = StateValue(component.state)
        self.onBack = onBack
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                Text(state.title)
                    .font(.title2).bold()
                Text(state.text)
                    .font(.body)
                Button {
                    onBack()
                } label: {
                    Text("Назад")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
            .padding(16)
        }
    }
}
