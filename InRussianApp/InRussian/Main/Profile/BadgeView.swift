//
//  BadgeView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import Shared
import SwiftUI

struct BadgesSectionView: View {
    let badges: [Badge]

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            if badges.isEmpty {
                Text("Пока нет достижений")
                    .font(.body)
                    .foregroundColor(.secondary)
            } else {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 16) {
                        ForEach(badges, id: \.id) { badge in
                            AchievementItemView(badge: badge)
                        }
                    }
                    .padding(.horizontal, 2)
                }
            }
        }
    }
}
struct AchievementItemView: View {
    let badge: Badge

    var body: some View {
        VStack(spacing: 6) {
            Image(systemName: "rosette")
                .font(.title2)
                .foregroundColor(.accent)
            Text(badge.name)
                .font(.caption)
                .lineLimit(1)
                .foregroundColor(.primary.opacity(0.8))
            if let desc = badge.description_, !desc.isEmpty {
                Text(desc)
                    .font(.caption2)
                    .lineLimit(1)
                    .foregroundColor(.secondary)
            }
        }
        .padding(.vertical, 10)
        .padding(.horizontal, 20)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(Color(uiColor: .componentBackground))
                .shadow(color: .black.opacity(0.05), radius: 2, y: 1)
        )
    }
}
