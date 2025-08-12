//
// Created by dark type on 12.08.2025.
//

import SwiftUI
import Shared

class RootHolder: ObservableObject {
    let lifecycle: LifecycleRegistry
    let root: RootComponent

    init() {
        
        DependencyInjectionKt.doInitKoin()
        
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()

        
        let componentFactory = KoinKt.koin(DependencyInjectionKt.koinApplication).get() as (ComponentContext) -> RootComponent
        
        root = componentFactory(DefaultComponentContext(lifecycle: lifecycle))

        lifecycle.onCreate()
    }

    deinit {
        lifecycle.onDestroy()
    }
}
