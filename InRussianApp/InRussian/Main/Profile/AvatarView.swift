//
//  AvatarView.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

 struct AvatarView: View {
    let avatarId: String?
    var body: some View {
        ZStack {
            Circle()
                .fill(Color(uiColor: .secondarySystemBackground))
            Text("A")
                .font(.title)
                .bold()
        }
        .frame(width: 96, height: 96)
    }
}
