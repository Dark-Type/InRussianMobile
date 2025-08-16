//
//  TasksComponentView.swift
//  InRussian
//
//  Created by dark type on 16.08.2025.
//


import SwiftUI
import Shared

struct TasksComponentView: View {
    let component: TasksComponent

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Задачи секции: \(component.sectionId)")
                .font(.headline)
            Text("Фильтр: \(String(describing: component.option))")
                .font(.subheadline)
            Button(action: {
                component.onBack()
            }) {
                Text("Назад")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
        .padding()
    }
}
