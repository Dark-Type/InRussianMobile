//
//  AvatarView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

struct AvatarView: View {
    let avatarId: String?
    let displayName: String?

    private var initial: String {
        let name = (displayName ?? "").trimmingCharacters(in: .whitespacesAndNewlines)
        if let first = name.first { return String(first).uppercased() }
        return "A"
    }

    var body: some View {
        ZStack {
            Circle()
                .fill(Color(.accent))
            Text(initial)
                .font(.title)
                .bold()
                .foregroundColor(.white)
        }
        .frame(width: 116, height: 116)
    }
}
