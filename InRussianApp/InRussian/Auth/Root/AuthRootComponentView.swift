//
//  AuthRootComponentView.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//

import SwiftUI
import Shared


struct AuthRootComponentView: View {
    @StateValue
    var stack: ChildStack<AnyObject, AuthRootComponentChild>
    
    init(component: AuthRootComponent) {
        _stack = StateValue(component.stack)
    }

    var body: some View {
        StackView(
            stackValue: _stack,
            getTitle: { child in
                switch child {
                case is AuthRootComponentChild.BaseAuthChild: return "Auth"
                case is AuthRootComponentChild.LoginChild: return "Login"
                case is AuthRootComponentChild.RegisterChild: return "Register"
                case is AuthRootComponentChild.SsoPopoverChild: return "SSO"
                case is AuthRootComponentChild.EnterEmailChild: return "Email"
                case is AuthRootComponentChild.EnterRecoveryCodeChild: return "Recovery Code"
                case is AuthRootComponentChild.UpdatePasswordChild: return "Update Password"
                default: return "Unknown"
                }
            },
            onBack: { _ in },
            childContent: { child in
                if let base = child as? AuthRootComponentChild.BaseAuthChild {
                    BaseAuthView(component: base.component)
                } else if let login = child as? AuthRootComponentChild.LoginChild {
                    LoginView(component: login.component)
                } else if let register = child as? AuthRootComponentChild.RegisterChild {
                    RegisterView(component: register.component)
                } else if let sso = child as? AuthRootComponentChild.SsoPopoverChild {
                    SsoPopoverView(component: sso.component)
                } else if let enterEmail = child as? AuthRootComponentChild.EnterEmailChild {
                    EnterEmailView(component: enterEmail.component)
                } else if let enterRecovery = child as? AuthRootComponentChild.EnterRecoveryCodeChild {
                    EnterRecoveryCodeView(component: enterRecovery.component)
                } else if let updatePassword = child as? AuthRootComponentChild.UpdatePasswordChild {
                    UpdatePasswordView(component: updatePassword.component)
                } else {
                    Text("Unknown Auth Flow")
                }
            }
        )
    }
}
