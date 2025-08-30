//
//  ProfileHeaderSectionView.swift
//  InRussian
//
//  Created by dark type on 26.08.2025.
//
import Shared
import SwiftUI

struct ProfileHeaderSectionView: View {
    let state: ProfileMainState
    let onLogout: () -> Void

    var body: some View {
        ZStack(alignment: .topTrailing) {
            Button(action: onLogout) {
                Image(systemName: "rectangle.portrait.and.arrow.right")
                    .foregroundColor(.accent)
                    .padding(12)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(Color(.componentBackground))
                            .shadow(color: .black.opacity(0.06), radius: 2, y: 1)
                    )
            }

            VStack(alignment: .leading, spacing: 18) {
                Text("Профиль")
                    .font(.system(size: 34, weight: .semibold))

                VStack(spacing: 12) {
                    AvatarView(avatarId: state.user?.avatarId, displayName: state.displayName)
                        .frame(maxWidth: .infinity)
                    Text(state.displayName.isEmpty ? "—" : state.displayName)
                        .font(.title3).bold()
                        .multilineTextAlignment(.center)
                    Text("Дата регистрации:\n\(state.registrationDate)")
                        .font(.footnote)
                        .multilineTextAlignment(.center)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity)

                Text("Ваши достижения")
                    .font(.headline)
            }
        }
    }
}
