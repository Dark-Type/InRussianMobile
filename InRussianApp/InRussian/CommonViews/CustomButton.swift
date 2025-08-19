//
//  CustomButton.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//


import SwiftUI

struct CustomButton: View {
    let text: String
    let color: Color
    let isActivatable: Bool
    let trailingLogo: AppImages.Logo?
    let action: () -> Void

    
    @State private var isActive: Bool = true

    init(
        text: String,
        color: Color,
        isActivatable: Bool = false,
        trailingLogo: AppImages.Logo? = nil,
        action: @escaping () -> Void
    ) {
        self.text = text
        self.color = color
        self.isActivatable = isActivatable
        self.trailingLogo = trailingLogo
        self.action = action
        _isActive = State(initialValue: true)
    }

    private var fillColor: Color {
        if isActivatable {
            return isActive ? color : AppColors.Palette.inactive.color
        } else {
            return color
        }
    }

    var body: some View {
        Button(action: {
            if isActivatable {
                withAnimation(.easeInOut(duration: 0.18)) {
                    isActive.toggle()
                }
            }
            action()
        }) {
            
            HStack(spacing: 8) {
                Spacer(minLength: 0)

                Text(text)
                    .font(.system(size: 17, weight: .semibold))
                    .foregroundColor(.white)
                    .lineLimit(1)

                if let logo = trailingLogo {
                    AppImages.image(for: logo)
                        .resizable()
                        .renderingMode(.original)
                        .aspectRatio(contentMode: .fit)
                        .frame(height: 20)
                }

                Spacer(minLength: 0)
            }
            .padding(.horizontal, 12)
            .frame(height: 50)
            .background(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .fill(fillColor)
            )
        }
        .buttonStyle(.plain)
    }
}
