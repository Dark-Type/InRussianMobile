//
//  FlowLayout.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

struct FlowLayout: Layout {
    
    typealias Cache = Void

    let spacing: CGFloat
    let runSpacing: CGFloat
    let alignment: HorizontalAlignment

    init(spacing: CGFloat = 8, runSpacing: CGFloat = 8, alignment: HorizontalAlignment = .leading) {
        self.spacing = spacing
        self.runSpacing = runSpacing
        self.alignment = alignment
    }

    // MARK: - Cache management (required by Layout)
    func makeCache(subviews: Subviews) -> Cache {
        
        return ()
    }

    func updateCache(_ cache: inout Cache, subviews: Subviews) {
        // No-op
    }

    // MARK: - Sizing (exact signature required by Layout)
    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout Cache) -> CGSize {
        
        let maxWidth = proposal.width ?? .greatestFiniteMagnitude

        var currentX: CGFloat = 0
        var currentRowHeight: CGFloat = 0
        var totalHeight: CGFloat = 0
        var maxRowWidth: CGFloat = 0

        for subview in subviews {
            let subviewSize = subview.sizeThatFits(ProposedViewSize(width: maxWidth, height: .greatestFiniteMagnitude))

            if currentX > 0 && currentX + subviewSize.width > maxWidth {

                totalHeight += currentRowHeight
                maxRowWidth = max(maxRowWidth, currentX - spacing)
                
                currentX = 0
                currentRowHeight = 0
            }

            currentX += subviewSize.width + spacing
            currentRowHeight = max(currentRowHeight, subviewSize.height)
        }

        
        if currentRowHeight > 0 || currentX > 0 {
            totalHeight += currentRowHeight
            maxRowWidth = max(maxRowWidth, currentX - spacing)
        }

        let finalWidth = proposal.width ?? maxRowWidth
        return CGSize(width: finalWidth, height: totalHeight)
    }

    // MARK: - Placement (exact signature required by Layout)
    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout Cache) {
        let maxWidth = bounds.width

        var x = bounds.minX
        var y = bounds.minY
        var currentRowHeight: CGFloat = 0

        for subview in subviews {
            let subviewSize = subview.sizeThatFits(ProposedViewSize(width: maxWidth, height: .greatestFiniteMagnitude))

            
            if x > bounds.minX && x + subviewSize.width > bounds.maxX {
                x = bounds.minX
                y += currentRowHeight + runSpacing
                currentRowHeight = 0
            }

            
            subview.place(
                at: CGPoint(x: x, y: y),
                proposal: ProposedViewSize(width: subviewSize.width, height: subviewSize.height)
            )

            x += subviewSize.width + spacing
            currentRowHeight = max(currentRowHeight, subviewSize.height)
        }
    }
}

// MARK: Convenience initializer
extension FlowLayout {
    init(_ spacing: CGFloat = 8, _ runSpacing: CGFloat = 8) {
        self.init(spacing: spacing, runSpacing: runSpacing, alignment: .leading)
    }
}
