//
//  StateValue.swift
//  iosApp
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

@propertyWrapper struct StateValue<T : AnyObject>: DynamicProperty {
    @ObservedObject
    private var obj: ObservableValue<T>

    var wrappedValue: T { obj.value }

    init(_ value: Value<T>) {
        obj = ObservableValue(value)
    }
}