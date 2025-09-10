//
//  TaskDescriptionView.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//
import Shared
import SwiftUI

struct TaskDescriptionView: View {
    var onInfo: (() -> Void)? = nil
    let text: String
    let taskTypes: [Any]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(spacing: 8) {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        ForEach(taskTypes.indices, id: \.self) { _ in
                            TypePill()
                        }
                    }
                    .padding(.horizontal, 8)
                }

                Spacer(minLength: 8)

                Button {
                    onInfo?()
                } label: {
                    Image(systemName: "info.circle")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(.orange)
                        .frame(width: 33, height: 33)
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Task info")
            }

            Text(text)
                .font(.body)
                .fixedSize(horizontal: false, vertical: true)
        }
        .accessibilityElement(children: .contain)
    }
}

private struct TypePill: View {
    var body: some View {
        HStack(spacing: 6) {
            Image(systemName: "square.grid.2x2")
                .font(.system(size: 12, weight: .semibold))
            Text("Type")
                .font(.caption)
                .lineLimit(1)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 6)
        .background(
            Capsule().fill(AppColors.Palette.componentBackground.color)
        )
        .overlay(
            Capsule().stroke(AppColors.Palette.stroke.color, lineWidth: 1)
        )
        .accessibilityElement(children: .combine)
    }
}
