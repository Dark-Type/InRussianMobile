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
            image(for: checked ? Checkbox.active : Checkbox.inactive)
        }
    }
}

extension AppImages {
    static func image<T: RawImageAsset>(for asset: T) -> Image {
        Image(asset.rawValue)
    }
}
