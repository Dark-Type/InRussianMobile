package com.example.inrussian.root.main.train

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailState
import com.example.inrussian.components.main.train.TasksOption

@Composable
fun SectionDetailHost(component: SectionDetailComponent) {
    val default = component as? DefaultSectionDetailComponent
    if (default == null) {
        Text("Unsupported SectionDetailComponent type")
        return
    }
    val state by component.state.subscribeAsState()
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

class SectionDetailHost : SectionDetailComponent {
    override val sectionId: String
        get() = ""
    override val state = MutableValue(SectionDetailState(isLoading = true, section = null))

    override fun openTasks(option: TasksOption) {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    @Preview
    @Composable
    fun Preview() {
        SectionDetailHost(this)
    }
}