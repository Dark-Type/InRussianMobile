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
        content
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    @ViewBuilder
    private var content: some View {
        if let c = child as? TaskBodyChildTextConnect {
            TextConnectTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildAudioConnect {
            AudioConnectTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildImageConnect {
            ImageConnectTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildTextInput {
            TextInputTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildTextInputWithVariant {
            TextInputWithVariantTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildListenAndSelect {
            ListenAndSelectTaskView(component: c.component) { onSetOnEvent($0) }

        } else if let c = child as? TaskBodyChildImageAndSelect {
            ImageAndSelectTaskView(component: c.component) { onSetOnEvent($0) }

        } else {
            Text("No specific body renderer")
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
    }
}
