//
//  TaskBodyChildRenderer.swift
//  InRussian
//
//  Created by dark type on 10.09.2025.
//
import Shared
import SwiftUI

struct TaskBodyChildRenderer: View {
    let child: (any TaskBodyChild)?
    let onSetOnEvent: ((() -> Void)?) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
//            content
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.componentBackground)
        .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
    }

//    @ViewBuilder
//    private var content: some View {
//        if let c = child as? TaskBodyChildTextConnect {
//            TextConnectView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildAudioConnect {
//            AudioConnectView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildImageConnect {
//            ImageConnectTaskView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildTextInput {
//            TextInputTaskView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildTextInputWithVariant {
//            TextInputWithVariantTaskView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildListenAndSelect {
//            ListenAndSelectTaskView(component: c.component) { onSetOnEvent($0) }
//        } else if let c = child as? TaskBodyChildImageAndSelect {
//            ImageAndSelectTaskView(component: c.component) { onSetOnEvent($0) }
//        } else {
//            Text("No specific body renderer")
//                .font(.subheadline)
//                .foregroundStyle(.secondary)
//        }
//    }
}

private struct MockTaskBodyView: View {
    let title: String
    let onSetOnEvent: ((() -> Void)?) -> Void

    @State private var storedEvent: (() -> Void)?

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.headline)
            Text("This is a mocked task body view.")
                .font(.subheadline)
                .foregroundStyle(.secondary)

            HStack(spacing: 8) {
                Button("Provide Event") {
                    let event: () -> Void = { /* mocked event */ }
                    storedEvent = event
                    onSetOnEvent(event)
                }
                .buttonStyle(.bordered)

                Button("Trigger Event") { storedEvent?() }
                    .buttonStyle(.bordered)
                    .disabled(storedEvent == nil)

                Button("Clear Event") {
                    storedEvent = nil
                    onSetOnEvent(nil)
                }
                .buttonStyle(.bordered)
            }
        }
        .onDisappear {
            onSetOnEvent(nil)
        }
    }
}

