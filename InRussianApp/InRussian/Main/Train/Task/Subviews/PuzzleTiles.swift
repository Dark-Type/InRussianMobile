//
//  PuzzleTiles.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//

import SwiftUI

enum TabSide {
    case left, right, top, bottom
}

enum ConnectorKind {
    case inward
    case outward
    case flat
}

struct PuzzleTileShape: InsettableShape {
    let side: TabSide
    let kind: ConnectorKind
    var knobRatio: CGFloat = 0.22
    var cornerRadius: CGFloat = 12

    var insetAmount: CGFloat = 0

    func path(in rect: CGRect) -> Path {
        let r = rect.insetBy(dx: insetAmount, dy: insetAmount)

        let w = r.width
        let h = r.height
        let leftX = r.minX
        let rightX = r.maxX
        let topY = r.minY
        let botY = r.maxY
        let cy = r.midY

        let baseCR = min(cornerRadius, min(w, h) * 0.25)
        let radius = max(2, min(w, h) * knobRatio)

        var tl = baseCR, tr = baseCR, br = baseCR, bl = baseCR
        if kind != .flat {
            switch side {
            case .right: tr = 0; br = 0
            case .left: tl = 0; bl = 0
            case .top: tl = 0; tr = 0
            case .bottom: bl = 0; br = 0
            }
        }

        var path = Path()

        path.move(to: CGPoint(x: leftX + tl, y: topY))

        path.addLine(to: CGPoint(x: rightX - tr, y: topY))

        if tr > 0 {
            path.addArc(center: CGPoint(x: rightX - tr, y: topY + tr),
                        radius: tr,
                        startAngle: .degrees(-90),
                        endAngle: .degrees(0),
                        clockwise: false)
        } else {
            path.addLine(to: CGPoint(x: rightX, y: topY))
        }

        if side == .right && kind != .flat {
            path.addLine(to: CGPoint(x: rightX, y: cy - radius))
            if kind == .outward {
                path.addArc(center: CGPoint(x: rightX, y: cy),
                            radius: radius,
                            startAngle: .degrees(-90),
                            endAngle: .degrees(90),
                            clockwise: false)
            } else {
                path.addArc(center: CGPoint(x: rightX, y: cy),
                            radius: radius,
                            startAngle: .degrees(-90),
                            endAngle: .degrees(90),
                            clockwise: true)
            }

            path.addLine(to: CGPoint(x: rightX, y: botY - br))
        } else {
            path.addLine(to: CGPoint(x: rightX, y: botY - br))
        }

        if br > 0 {
            path.addArc(center: CGPoint(x: rightX - br, y: botY - br),
                        radius: br,
                        startAngle: .degrees(0),
                        endAngle: .degrees(90),
                        clockwise: false)
        } else {
            path.addLine(to: CGPoint(x: rightX, y: botY))
        }

        path.addLine(to: CGPoint(x: leftX + bl, y: botY))
        if bl > 0 {
            path.addArc(center: CGPoint(x: leftX + bl, y: botY - bl),
                        radius: bl,
                        startAngle: .degrees(90),
                        endAngle: .degrees(180),
                        clockwise: false)
        } else {
            path.addLine(to: CGPoint(x: leftX, y: botY))
        }

        if side == .left && kind != .flat {
            path.addLine(to: CGPoint(x: leftX, y: cy + radius))
            if kind == .outward {
                path.addArc(center: CGPoint(x: leftX, y: cy),
                            radius: radius,
                            startAngle: .degrees(90),
                            endAngle: .degrees(270),
                            clockwise: false)
            } else {
                path.addArc(center: CGPoint(x: leftX, y: cy),
                            radius: radius,
                            startAngle: .degrees(90),
                            endAngle: .degrees(-90),
                            clockwise: true)
            }
            path.addLine(to: CGPoint(x: leftX, y: topY + tl))
        } else {
            path.addLine(to: CGPoint(x: leftX, y: topY + tl))
        }

        if tl > 0 {
            path.addArc(center: CGPoint(x: leftX + tl, y: topY + tl),
                        radius: tl,
                        startAngle: .degrees(180),
                        endAngle: .degrees(270),
                        clockwise: false)
        } else {
            path.addLine(to: CGPoint(x: leftX, y: topY))
        }

        path.closeSubpath()
        return path
    }

    func inset(by amount: CGFloat) -> Self {
        var copy = self
        copy.insetAmount += amount
        return copy
    }
}

struct PuzzleTile<Content: View>: View {
    let side: TabSide
    let kind: ConnectorKind
    var knobRatio: CGFloat = 0.22
    var cornerRadius: CGFloat = 12
    var fill: Color = .white
    var stroke: Color = .secondary.opacity(0.35)
    var lineWidth: CGFloat = 1

    var content: () -> Content

    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        let shape = PuzzleTileShape(side: side, kind: kind, knobRatio: knobRatio, cornerRadius: cornerRadius)
        ZStack {
            shape
                .fill(fill)
                .overlay(
                    shape
                        .inset(by: lineWidth / 2)
                        .stroke(stroke, lineWidth: lineWidth)
                        .drawingGroup()
                )
                .contentShape(shape)
            content()
                .clipShape(shape)
        }
        .shadow(color: Color.black.opacity(colorScheme == .dark ? 0.25 : 0.07), radius: 8, x: 0, y: 4)
        .accessibilityElement(children: .combine)
    }
}

// MARK: - Convenience

extension PuzzleTile where Content == EmptyView {
    init(side: TabSide, kind: ConnectorKind,
         knobRatio: CGFloat = 0.22,
         cornerRadius: CGFloat = 12,
         fill: Color = .white,
         stroke: Color = .secondary.opacity(0.35),
         lineWidth: CGFloat = 1)
    {
        self.side = side
        self.kind = kind
        self.knobRatio = knobRatio
        self.cornerRadius = cornerRadius
        self.fill = fill
        self.stroke = stroke
        self.lineWidth = lineWidth
        self.content = { EmptyView() }
    }
}

// MARK: - Previews

struct PuzzleTileView_Previews: PreviewProvider {
    static let tileSize = CGSize(width: 140, height: 80)

    static var previews: some View {
        Group {
            HStack(spacing: 0) {
                PuzzleTile(side: .right, kind: .outward) {
                    Text("A")
                        .font(.headline)
                        .padding()
                }
                .frame(width: tileSize.width, height: tileSize.height)

                PuzzleTile(side: .left, kind: .inward) {
                    Text("B")
                        .font(.headline)
                        .padding()
                }
                .frame(width: tileSize.width, height: tileSize.height)
            }
            .padding()
            .previewDisplayName("Connected (horizontal)")

            HStack(spacing: 16) {
                PuzzleTile(side: .right, kind: .outward)
                    .frame(width: tileSize.width, height: tileSize.height)
                PuzzleTile(side: .left, kind: .inward)
                    .frame(width: tileSize.width, height: tileSize.height)
            }
            .padding()
            .previewDisplayName("Not connected (horizontal)")

            HStack(spacing: 16) {
                PuzzleTile(side: .right, kind: .outward, fill: .mint, stroke: .mint.opacity(0.5))
                    .frame(width: tileSize.width, height: tileSize.height)
                PuzzleTile(side: .left, kind: .inward, fill: .orange, stroke: .orange.opacity(0.5))
                    .frame(width: tileSize.width, height: tileSize.height)
            }
            .padding()
            .previewDisplayName("Color variants")
        }
        .background(Color(white: 0.96))
        .previewLayout(.sizeThatFits)
    }
}
