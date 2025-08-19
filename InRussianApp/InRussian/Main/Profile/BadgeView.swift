//
//  BadgeView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI
import Shared

 struct BadgeView: View {
    let badge: Badge
    var body: some View {
        VStack(spacing: 6) {
            ZStack {
                Circle()
                    .fill(Color.accentColor.opacity(0.15))
                Text(badge.name.first.map { String($0).uppercased() } ?? "?")
                    .font(.title3).bold()
            }
            .frame(width: 64, height: 64)
            Text(badge.name)
                .font(.caption)
                .lineLimit(1)
        }
        .frame(minWidth: 72)
    }
}
