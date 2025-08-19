//
//  RawColorAsset.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//
import SwiftUI


protocol RawColorAsset: RawRepresentable where RawValue == String {}

enum AppColors {
    enum Palette: String, RawColorAsset {
        case accent = "AccentColor"
        case baseBackground = "BaseBackground"
        case secondary = "AccentSecondary"
        case vk = "VKColor"
        case yandex = "YandexColor"
        case fontCaptive = "FontCaptive"
        case fontInactive = "FontInactive"
        case footnote = "Footnote"
        case inactive = "Inactive"
        case secondaryBackground = "SecondaryBackground"
        case stroke = "Stroke"
        case componentBackground = "ComponentBackground"

        var color: Color { AppColors.color(for: self) }
    }
}

extension AppColors {
    static func color<T: RawColorAsset>(for asset: T) -> Color {
        Color(asset.rawValue)
    }
}
