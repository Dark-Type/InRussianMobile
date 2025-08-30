//
//  ProfileComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct ProfileComponentView: View {
    let component: ProfileComponent
    @StateValue private var stack: ChildStack<AnyObject, any ProfileComponentChild>

    init(component: ProfileComponent) {
        self.component = component
        _stack = StateValue(component.stack)
    }

    var body: some View {
        let current = stack.active.instance
        Group {
            if let main = current as? ProfileComponentChildMainChild {
                ProfileMainView(component: main.component)
            } else if let edit = current as? ProfileComponentChildEditProfileChild {
                EditProfileView(component: edit.component)
            } else if let about = current as? ProfileComponentChildAboutChild {
                StaticTextScreenView(component: about.component, onBack: about.component.onBack)
            } else if let privacy = current as? ProfileComponentChildPrivacyPolicyChild {
                StaticTextScreenView(component: privacy.component, onBack: privacy.component.onBack)
            } else {
                EmptyView()
            }
        }
        .animation(.default, value: stack.active.instance.hashObject())
    }
}



private extension ProfileComponentChild {
    func hashObject() -> Int { (self as AnyObject).hash }
}
