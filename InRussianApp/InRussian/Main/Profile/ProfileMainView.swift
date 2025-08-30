//
//  ProfileMainView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import Shared
import SwiftUI

struct ProfileMainView: View {
    let component: ProfileMainComponent
    @StateValue private var state: ProfileMainState

    init(component: ProfileMainComponent) {
        self.component = component
        _state = StateValue(component.state)
    }

    var body: some View {
        Group {
            if state.isLoading {
                ProgressCentered().padding(.top, 60)
            } else {
                ScrollView {
                    VStack(alignment: .leading, spacing: 24) {
                        ProfileHeaderSectionView(
                            state: state,
                            onLogout: { component.onShowAuth() }
                        )

                        BadgesSectionView(badges: state.badges)

                        ActionButtonsCard(state: state, component: component)

                        Spacer().frame(height: 82)
                    }
                    .padding(16)
                }
                .background(Color(uiColor: .secondarySystemBackground))
            }
        }
    }
}
