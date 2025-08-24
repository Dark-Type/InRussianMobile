//
//  KeyboardObserver.swift
//  InRussian
//
//  Created by dark type on 24.08.2025.
//


import SwiftUI
import Combine


final class KeyboardObserver: ObservableObject {
    @Published var height: CGFloat = 0
    @Published var isChanging: Bool = false

    private var tokens: [NSObjectProtocol] = []

    init() {
        let center = NotificationCenter.default

        tokens.append(center.addObserver(
            forName: UIResponder.keyboardWillChangeFrameNotification,
            object: nil,
            queue: .main
        ) { [weak self] note in
            self?.handle(note: note)
        })

        tokens.append(center.addObserver(
            forName: UIResponder.keyboardWillHideNotification,
            object: nil,
            queue: .main
        ) { [weak self] note in
            self?.handle(note: note, forceHide: true)
        })
    }

    deinit {
        let center = NotificationCenter.default
        tokens.forEach { center.removeObserver($0) }
        tokens.removeAll()
    }

    private func handle(note: Notification, forceHide: Bool = false) {
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

        isChanging = true
        withAnimation(.easeOut(duration: max(0.1, duration))) {
            self.height = targetHeight.isFinite ? targetHeight : 0
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + duration + 0.02) {
            self.isChanging = false
        }
    }
}
