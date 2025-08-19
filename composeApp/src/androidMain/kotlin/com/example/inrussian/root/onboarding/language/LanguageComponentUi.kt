package com.example.inrussian.root.onboarding.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.onboarding.language.LanguageComponent

/*
@Composable
fun LanguageComponentUi(component: LanguageComponent) {
    val state = component.state
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("RUSSIAN", "ENGLISH", "CHINESE", "UZBEK", "TAJIK", "HINDI")
    var selectedLanguage by remember { mutableStateOf(state.selectedLanguage) }
    var hasPermission by remember { mutableStateOf(state.hasGivenPermission) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Выберите язык")
        Spacer(modifier = Modifier.height(8.dp))
        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                label = { Text("Язык") },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang) },
                        onClick = {
                            selectedLanguage = lang
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(
                checked = hasPermission,
                onCheckedChange = { hasPermission = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Я даю разрешение")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { component.onNext() }) {
            Text(text = "Далее")
        }
    }
}*/
