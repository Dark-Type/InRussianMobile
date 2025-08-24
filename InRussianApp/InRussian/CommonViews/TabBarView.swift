//
//  TabBarView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared

struct TabBarView: View {
    let activeTab: MainRootComponentTab
    let onTabSelected: (MainRootComponentTab) -> Void

    var body: some View {
        HStack {
            Spacer()
            TabBarItem(
                isSelected: activeTab == .home,
                icon: "house",
                label: "Главная",
                action: { onTabSelected(.home) }
            )
            Spacer()
            TabBarItem(
                isSelected: activeTab == .train,
                icon: "book.pages",
                label: "Тренировка",
                action: { onTabSelected(.train) }
            )
            Spacer()
            TabBarItem(
                isSelected: activeTab == .profile,
                icon: "person.crop.circle",
                label: "Профиль",
                action: { onTabSelected(.profile) }
            )
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .frame(height: 56)
        .padding(.vertical, 8)
        .background(.ultraThinMaterial)
        
    }
}
