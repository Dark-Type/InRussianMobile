//
//  MainRootComponentView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import Shared
import SwiftUI

struct MainRootComponentView: View {
    let component: MainRootComponent

    @StateValue private var childStack: ChildStack<AnyObject, any MainRootComponentChild>
    @StateValue private var activeTab: MainRootComponentTab

    init(component: MainRootComponent) {
        self.component = component
        _childStack = StateValue(component.childStack)
        _activeTab = StateValue(component.activeTab)
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            Group {
                let child = childStack.active.instance
                if let home = child as? MainRootComponentChildHomeChild {
                    HomeComponentView(component: home.component)
                } else if let train = child as? MainRootComponentChildTrainChild {
                    TrainComponentView(component: train.component)
                } else if let profile = child as? MainRootComponentChildProfileChild {
                    ProfileComponentView(component: profile.component)
                } else {
                    EmptyView()
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            
            TabBarView(
                activeTab: activeTab,
                onTabSelected: { tab in component.openTab(tab: tab) }
            )
            .frame(maxWidth: .infinity)
            .ignoresSafeArea(.container, edges: .horizontal)
            .padding(.bottom, 8)
        }
        .edgesIgnoringSafeArea(.bottom)
    }
}
