//
//  SectionProgressRow.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI
import Shared

struct SectionProgressRow: View {
    let section: CourseSection
    let showProgress: Bool

    private var progressPercent: Int {
        guard section.totalLessons > 0 else { return 0 }
        let value = Double(section.completedLessons) / Double(section.totalLessons) * 100
        return Int(value.rounded())
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack {
                Text(section.title)
                    .font(.footnote.weight(.semibold))
                    .lineLimit(1)
                Spacer()
                if showProgress {
                    Text("\(progressPercent)%")
                        .font(.caption2.weight(.semibold))
                        .foregroundColor(progressColor(progressPercent))
                }
            }

            if showProgress {
                ProgressView(value: Double(progressPercent), total: 100)
                    .tint(progressColor(progressPercent))
            }
        }
        .padding(.vertical, 15)
        .padding(.horizontal, 12)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(Color(uiColor: .systemBackground))
        )
    }
}
