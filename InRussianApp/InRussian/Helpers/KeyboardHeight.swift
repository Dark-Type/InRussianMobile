//
//  KeyboardHeight.swift
//  InRussian
//
//  Created by dark type on 24.08.2025.
//
import SwiftUI
import Combine

final class KeyboardHeight: ObservableObject {
    @Published var height: CGFloat = 0

    private var willChangeToken: NSObjectProtocol?
    private var willHideToken: NSObjectProtocol?

    init() {
        let center = NotificationCenter.default

        willChangeToken = center.addObserver(
            forName: UIResponder.keyboardWillChangeFrameNotification,
            object: nil,
            queue: .main
        ) { [weak self] note in
            self?.handle(note: note, forceHide: false)
        }

        willHideToken = center.addObserver(
            forName: UIResponder.keyboardWillHideNotification,
            object: nil,
            queue: .main
        ) { [weak self] note in
            self?.handle(note: note, forceHide: true)
        }
    }

    deinit {
        let center = NotificationCenter.default
        if let t = willChangeToken { center.removeObserver(t) }
        if let t = willHideToken { center.removeObserver(t) }
    }

    private func handle(note: Notification, forceHide: Bool) {
        guard
            let userInfo = note.userInfo,
            let duration = (userInfo[UIResponder.keyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue
        else { return }

        let targetHeight: CGFloat
        if forceHide {
            targetHeight = 0
        } else if let endFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            let screen = UIScreen.main.bounds
            targetHeight = max(0, screen.maxY - endFrame.minY)
        } else {
            targetHeight = 0
        }

        withAnimation(.easeOut(duration: max(0.05, duration))) {
            self.height = targetHeight.isFinite ? targetHeight : 0
        }
    }
}
