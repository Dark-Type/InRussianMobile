//
//  ProgressCircleView.swift
//  InRussian
//
//  Created by dark type on 20.08.2025.
//


import SwiftUI

func progressColor(_ percent: Int) -> Color {
    if percent >= 80 {
        return .green
    } else if percent < 50 {
        return .red
    } else {
        return .yellow
    }
}

struct ProgressCircleView: View {
    let progressPercent: Int
    let lineWidth: CGFloat

    private var progress: Double { Double(progressPercent) / 100.0 }

    init(progressPercent: Int, lineWidth: CGFloat = 6) {
        self.progressPercent = max(0, min(100, progressPercent))
        self.lineWidth = lineWidth
    }

    var body: some View {
        ZStack {
            Circle()
                .stroke(Color.secondary.opacity(0.2), lineWidth: lineWidth)

            Circle()
                .trim(from: 0, to: progress)
                .stroke(
                    progressColor(progressPercent),
                    style: StrokeStyle(lineWidth: lineWidth, lineCap: .round)
                )
                .rotationEffect(.degrees(-90))

            Text("\(progressPercent)%")
                .font(.system(size: 11, weight: .semibold))
                .foregroundColor(progressColor(progressPercent))
        }
        .frame(width: 44, height: 44)
    }
}

#Preview {
    ProgressCircleView(progressPercent: 12, lineWidth: 2)
}
