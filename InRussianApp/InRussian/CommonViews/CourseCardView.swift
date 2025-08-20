//
//  CourseCardWithTriangle.swift
//  InRussian
//
//  Created by dark type on 20.08.2025.
//
import SwiftUI

struct CourseCardView: View {
    
    let imageWidth: CGFloat
    let imageHeight: CGFloat
    
    let wrapperWidth: CGFloat
    let wrapperHeight: CGFloat
    
    let wrapperOffsetX: CGFloat
    let wrapperOffsetY: CGFloat

    let courseTitle: String
    let courseSubtitle: String

    init(
        courseTitle: String,
        courseSubtitle: String,
        imageWidth: CGFloat = 250,
        imageHeight: CGFloat = 200,
        wrapperWidth: CGFloat = 240,
        wrapperHeight: CGFloat = 90,
        wrapperOffsetX: CGFloat = -20,
        wrapperOffsetY: CGFloat = 90
    ) {
        self.courseTitle = courseTitle
        self.courseSubtitle = courseSubtitle
        self.imageWidth = imageWidth
        self.imageHeight = imageHeight
        self.wrapperWidth = wrapperWidth
        self.wrapperHeight = wrapperHeight
        self.wrapperOffsetX = wrapperOffsetX
        self.wrapperOffsetY = wrapperOffsetY
    }

    var body: some View {
        ZStack(alignment: .topLeading) {
            AppImages.image(for: AppImages.Mock.mock)
                .resizable()
                .frame(width: imageWidth, height: imageHeight)
                .cornerRadius(24)
                .zIndex(0)

            ZStack(alignment: .leading) {
                AppImages.image(for: AppImages.Wrapper.wrapper)
                    .resizable()
                    .frame(width: wrapperWidth, height: wrapperHeight)
                VStack(alignment: .leading, spacing: 4) {
                    Text(courseTitle)
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)
                        .lineLimit(2)
                        .minimumScaleFactor(0.8)
                    Text(courseSubtitle)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundColor(.white.opacity(0.85))
                        .lineLimit(1)
                }
                .padding(.leading, 14)
                .padding(.top, -20)
            }
            .frame(width: wrapperWidth, height: wrapperHeight)
            .offset(x: wrapperOffsetX, y: wrapperOffsetY)
            .zIndex(1)
        }
        .frame(
            width: imageWidth + abs(min(0, wrapperOffsetX)),
            height: imageHeight
        )
    }
}

struct CourseCardView_Previews: PreviewProvider {
    static var previews: some View {
        CourseCardView(
            courseTitle: "Курс на рабочий патент",
            courseSubtitle: "12 секций • English"
        )
        .padding()
        .background(Color(.systemGroupedBackground))
        .previewLayout(.sizeThatFits)
    }
}
