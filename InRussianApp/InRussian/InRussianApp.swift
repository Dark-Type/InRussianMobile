import Shared
import SwiftUI

@main
struct InRussianApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    @Environment(\.scenePhase)
    var scenePhase: ScenePhase

    var rootHolder: RootHolder {
        appDelegate.rootHolder
    }

    var body: some Scene {
        WindowGroup {
            RootView(rootHolder.root)
                .onChange(of: scenePhase) { newPhase in

                    switch newPhase {
                    case .background:
                        print("App ScenePhase: Background - Stopping Decompose lifecycle")
                        LifecycleRegistryExtKt.stop(rootHolder.lifecycle)
                    case .inactive:
                        print("App ScenePhase: Inactive - Pausing Decompose lifecycle")
                        LifecycleRegistryExtKt.pause(rootHolder.lifecycle)
                    case .active:
                        print("App ScenePhase: Active - Resuming Decompose lifecycle")
                        LifecycleRegistryExtKt.resume(rootHolder.lifecycle)
                    @unknown default:
                        print("App ScenePhase: Unknown state")
                    }
                }
        }
    }
}
