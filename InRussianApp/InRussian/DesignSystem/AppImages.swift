//
//  CheckboxAsset.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI

protocol RawImageAsset: RawRepresentable where RawValue == String {}

enum AppImages {
    enum Checkbox: String, RawImageAsset {
        case active = "CheckboxActive"
        case inactive = "CheckboxInactive"

        static func toggle(_ checked: Bool) -> Image {
            
            AppImages.image(for: checked ? Checkbox.active : Checkbox.inactive)
        }
    }
    enum Eye: String, RawImageAsset {
        case open = "EyeOpen"
        case close = "EyeClosed"
        
        static func toggle(_ checked: Bool) -> Image {
            
            AppImages.image(for: checked ? Eye.open : Eye.close)
        }
    }
    enum Recovery: String, RawImageAsset{
        case recovery = "Recovery"
    }

    enum Logo: String, RawImageAsset {
        case app = "Logo"
        case vk = "VKLogo"
        case yandex = "YandexLogo"

        static var appLogo: Image { AppImages.image(for: Logo.app) }
        static var vkLogo: Image { AppImages.image(for: Logo.vk) }
        static var yandexLogo: Image { AppImages.image(for: Logo.yandex) }
    }
}

extension AppImages {
    static func image<T: RawImageAsset>(for asset: T) -> Image {
        Image(asset.rawValue)
    }
}
