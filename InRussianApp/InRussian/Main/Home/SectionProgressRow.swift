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

    private var progressPercent: Int {
        guard section.totalLessons > 0 else { return 0 }
        return Int((section.completedLessons * 100) / section.totalLessons)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(section.title)
                .font(.headline)
            ProgressView(value: Double(progressPercent), total: 100)
            Text("\(progressPercent)% (\(section.completedLessons)/\(section.totalLessons))")
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .padding(12)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(Color(uiColor: .secondarySystemBackground))
        )
    }
}
