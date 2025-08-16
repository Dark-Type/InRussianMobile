//
//  MutableValue.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//

import Shared

func mutableValue<T: AnyObject>(_ initialValue: T) -> MutableValue<T> {
    return MutableValueBuilderKt.MutableValue(initialValue: initialValue) as! MutableValue<T>
}

