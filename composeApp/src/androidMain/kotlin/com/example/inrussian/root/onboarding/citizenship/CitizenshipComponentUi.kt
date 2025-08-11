package com.example.inrussian.root.onboarding.citizenship

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenshipComponentUi(component: CitizenshipComponent) {
    val countries = listOf(
        "Россия", "Казахстан", "Узбекистан", "Таджикистан", "Киргизия",
        "Беларусь", "Армения", "Грузия", "Китай", "Индия"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("") }
    var citizenships by remember { mutableStateOf(component.state.citizenship) }
    var nationality by remember { mutableStateOf(component.state.nationality) }
    var countryOfResidence by remember { mutableStateOf(component.state.countryOfResidence) }
    var cityOfResidence by remember { mutableStateOf(component.state.cityOfResidence) }
    var countryDuringEducation by remember { mutableStateOf(component.state.countryDuringEducation) }
    var timeSpentInRussia by remember { mutableStateOf(component.state.timeSpentInRussia) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Выберите гражданство")
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                label = { Text("Страна") },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            if (!citizenships.contains(country)) {
                                citizenships.add(country)
                            }
                            selectedCountry = country
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Ваши гражданства: ${citizenships.joinToString()}")

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = nationality,
            onValueChange = { nationality = it },
            label = { Text("Национальность") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = countryOfResidence,
            onValueChange = { countryOfResidence = it },
            label = { Text("Страна проживания") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = cityOfResidence,
            onValueChange = { cityOfResidence = it },
            label = { Text("Город проживания") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = countryDuringEducation,
            onValueChange = { countryDuringEducation = it },
            label = { Text("Страна во время обучения") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = timeSpentInRussia,
            onValueChange = { timeSpentInRussia = it },
            label = { Text("Время, проведённое в России") },
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