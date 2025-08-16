//
// Created by dark type on 16.08.2025.
//

import Foundation
import Shared
import SwiftUI

class RootHolder: ObservableObject {
    let lifecycle: LifecycleRegistry
    let root: RootComponent

    init() {
        self.lifecycle = LifecycleRegistryKt.LifecycleRegistry()
        let iosComponentContext = DefaultComponentContext(lifecycle: self.lifecycle)
        self.root = SharedDI.shared.createRoot(componentContext: iosComponentContext)

        print("RootHolder: Initialized. Root component created via existing SharedDI.createRoot: \(self.root)")

        LifecycleRegistryExtKt.create(self.lifecycle)
        print("RootHolder: Lifecycle Created. State: \(self.lifecycle.state)")
    }

    deinit {
        LifecycleRegistryExtKt.destroy(self.lifecycle)
        print("RootHolder: Lifecycle Destroyed.")
    }
}
