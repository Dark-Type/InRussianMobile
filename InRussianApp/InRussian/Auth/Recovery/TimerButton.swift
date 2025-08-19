//
//  TimerButton.swift
//  InRussian
//
//  Created by dark type on 19.08.2025.
//

import SwiftUI

struct TimerButton: View {
    let initialSeconds: Int
    let onTap: () -> Void
    let inactiveColor: Color
    let activeColor: Color

    @State private var secondsLeft: Int
    @State private var isCounting: Bool = true

    init(
        initialSeconds: Int = 300,
        inactiveColor: Color,
        activeColor: Color,
        onTap: @escaping () -> Void
    ) {
        self.initialSeconds = initialSeconds
        self.inactiveColor = inactiveColor
        self.activeColor = activeColor
        self.onTap = onTap
        _secondsLeft = State(initialValue: initialSeconds)
    }

    private var timerText: String {
        let minutes = secondsLeft / 60
        let seconds = secondsLeft % 60
        return String(format: "%d:%02d", minutes, seconds)
    }

    var body: some View {
        CustomButton(
            text: "Отправить код подтверждения \(timerText)",
            color: isCounting ? inactiveColor : activeColor,
            isActive: !isCounting,
            action: onTap
        )
        .onAppear {
            startTimer()
        }
        .onDisappear {
            isCounting = false
        }
    }

    private func startTimer() {
        isCounting = true
        secondsLeft = initialSeconds
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { timer in
            if secondsLeft > 0 && isCounting {
                secondsLeft -= 1
            } else {
                timer.invalidate()
                isCounting = false
            }
        }
    }
}
