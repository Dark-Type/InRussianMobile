package com.example.inrussian.root.main.train

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailComponent

@Composable
fun SectionDetailHost(component: SectionDetailComponent) {
    val state by component.state.subscribeAsState()

    val default = component as? DefaultSectionDetailComponent
    if (default == null) {
        Text("Unsupported SectionDetailComponent type")
        return
    }

    val innerStack by default.childStack.subscribeAsState()

    when (val inner = innerStack.active.instance) {
        is DefaultSectionDetailComponent.InnerChild.DetailsChild ->
            SectionDetailsScreen(component, state)

        is DefaultSectionDetailComponent.InnerChild.TasksChild ->
            TasksScreen(inner.component)
    }

    if (state.showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { component.onBack() },
            title = { Text("Секция завершена") },
            text = { Text("Вы завершили все задачи секции!") },
            confirmButton = {
                TextButton(onClick = { component.onBack() }) {
                    Text("OK")
                }
            }
        )
    }
}