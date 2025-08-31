//
//  KeyboardObserver.swift
//  InRussian
//
//  Created by dark type on 24.08.2025.
//


import SwiftUI
import Combine


final class KeyboardObserver: ObservableObject {
    @Published private(set) var visibleHeight: CGFloat = 0
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

    private func keyWindow() -> UIWindow? {
        // Prefer the active scene's key window
        return UIApplication.shared
            .connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }
    }

    private func handle(note: Notification, forceHide: Bool = false) {
        guard
            let userInfo = note.userInfo,
            let duration = (userInfo[UIResponder.keyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue
        else { return }

        let screenBounds = UIScreen.main.bounds
        let bottomInset = keyWindow()?.safeAreaInsets.bottom ?? 0

        let overlap: CGFloat
        if forceHide {
            overlap = 0
        } else if let endFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            // How much of the screen bottom is covered by the keyboard
            overlap = max(0, screenBounds.maxY - endFrame.minY)
        } else {
            overlap = 0
        }

        // Subtract the bottom safe-area to avoid a "double gap" above the keyboard.
        let targetVisible = max(0, overlap - bottomInset)

        isChanging = true
        withAnimation(.easeOut(duration: max(0.1, duration))) {
            self.visibleHeight = targetVisible.isFinite ? targetVisible : 0
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + duration + 0.02) {
            self.isChanging = false
        }
    }
}
