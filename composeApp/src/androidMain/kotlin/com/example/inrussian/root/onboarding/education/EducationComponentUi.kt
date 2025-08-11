package com.example.inrussian.root.onboarding.education

import androidx.compose.material3.Text
import com.example.inrussian.components.onboarding.education.EducationComponent

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationComponentUi(component: EducationComponent) {
    val languageOptions = listOf("Русский", "Английский", "Китайский", "Узбекский", "Таджикский", "Хинди", "Казахский", "Французский", "Немецкий", "Испанский")
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("") }
    var languages by remember { mutableStateOf(component.state.languages) }
    var understandsRussian by remember { mutableStateOf(component.state.understandsRussian) }
    var speaksRussian by remember { mutableStateOf(component.state.speaksRussian) }
    var readsRussian by remember { mutableStateOf(component.state.readsRussian) }
    var writesRussian by remember { mutableStateOf(component.state.writesRussian) }
    var kindOfActivity by remember { mutableStateOf(component.state.kindOfActivity) }
    var education by remember { mutableStateOf(component.state.education) }
    var purposeOfRegistration by remember { mutableStateOf(component.state.purposeOfRegistration) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Выберите языки, которыми владеете")
        Spacer(Modifier.height(8.dp))

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
                languageOptions.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(lang) },
                        onClick = {
                            if (!languages.contains(lang)) {
                                languages.add(lang)
                            }
                            selectedLanguage = lang
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Выбранные языки: ${languages.joinToString()}")

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = understandsRussian, onCheckedChange = { understandsRussian = it })
            Spacer(Modifier.width(8.dp))
            Text("Понимает русский")
        }
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = speaksRussian, onCheckedChange = { speaksRussian = it })
            Spacer(Modifier.width(8.dp))
            Text("Говорит по-русски")
        }
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = readsRussian, onCheckedChange = { readsRussian = it })
            Spacer(Modifier.width(8.dp))
            Text("Читает по-русски")
        }
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = writesRussian, onCheckedChange = { writesRussian = it })
            Spacer(Modifier.width(8.dp))
            Text("Пишет по-русски")
        }

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = kindOfActivity,
            onValueChange = { kindOfActivity = it },
            label = { Text("Вид деятельности") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = education,
            onValueChange = { education = it },
            label = { Text("Образование") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = purposeOfRegistration,
            onValueChange = { purposeOfRegistration = it },
            label = { Text("Цель регистрации") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = { component.onBack() }) {
                Text("Назад")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { component.onNext() }) {
                Text("Далее")
            }
        }
    }
}