//
//  SectionProgressRow.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI
import Shared



struct SectionRow: View {
    let section: Shared.CourseSection
    let showProgress: Bool
    let background: Color

    var body: some View {
        HStack(alignment: .center, spacing: 14) {
            IconBlock()
            TextBlock(section: section, showProgress: showProgress)
            Spacer()
            if showProgress {
                Chevron()
            }
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 18, style: .continuous)
                .fill(background)
                .shadow(color: .black.opacity(0.04), radius: 4, y: 2)
        )
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(accessibilityLabel)
    }

    private var accessibilityLabel: String {
        if showProgress {
            let p = TextBlock.percent(for: section)
            return "\(section.title). Прогресс \(p) процентов."
        } else {
            return "\(section.title). \(section.totalLessons) уроков."
        }
    }
}

// MARK: - Subviews

private struct IconBlock: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 12, style: .continuous)
            .fill(Color.accentColor.opacity(0.15))
            .frame(width: 54, height: 54)
            .overlay(
                Image(systemName: "book.fill")
                    .font(.system(size: 20))
                    .foregroundColor(.accentColor)
            )
            .accessibilityHidden(true)
    }
}

private struct Chevron: View {
    var body: some View {
        Image(systemName: "chevron.right")
            .font(.caption.weight(.bold))
            .foregroundColor(.secondary)
    }
}

private struct TextBlock: View {
    let section: Shared.CourseSection
    let showProgress: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Title()
            if showProgress {
                Progress()
            }
            Footnote()
        }
    }

    private func Title() -> some View {
        Text(section.title)
            .font(.subheadline.weight(.semibold))
            .lineLimit(2)
    }

    @ViewBuilder
    private func Progress() -> some View {
        let p = Self.percent(for: section)
        ProgressView(value: Double(p), total: 100)
            .tint(.accentColor)
            .frame(maxWidth: 160)
            .accessibilityHidden(true)
    }

    private func Footnote() -> some View {
        let p = Self.percent(for: section)
        let line: String
        if showProgress {
            line = "\(section.completedLessons)/\(section.totalLessons) уроков • \(p)%"
        } else {
            line = "\(section.totalLessons) уроков"
        }
        return Text(line)
            .font(.caption2)
            .foregroundColor(.secondary)
    }

    static func percent(for section: Shared.CourseSection) -> Int {
        guard section.totalLessons > 0 else { return 0 }
        return Int((Double(section.completedLessons) / Double(section.totalLessons)) * 100)
    }
}
