//
//  ObservableValue.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

public class ObservableValue<T: AnyObject>: ObservableObject {
    @Published
    var value: T

    private var cancellation: Cancellation?

    public init(_ value: Value<T>) {
        self.value = value.value
        self.cancellation = value.subscribe { [weak self] v in
            guard let self = self else { return }
            if Thread.isMainThread {
                self.value = v
            } else {
                DispatchQueue.main.async {
                    self.value = v
                }
            }
        }
    }

    deinit {
        cancellation?.cancel()
    }
}
