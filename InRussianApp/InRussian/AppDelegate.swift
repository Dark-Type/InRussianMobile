//
//  AppDelegate.swift
//  iosApp
//
//  Created by dark type on 12.08.2025.
//

import Shared
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate, ObservableObject {
    private let koinStarter: () = {
        let modules = KotlinArray<Koin_coreModule>(size: 0) { _ in nil }
        SharedDI.shared.start(platformModules: modules)

    }()

    lazy var rootHolder: RootHolder = .init()

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool
    {
        print("AppDelegate: didFinishLaunchingWithOptions. SharedDI.start() from Kotlin should have been called.")
        return true
    }
}
