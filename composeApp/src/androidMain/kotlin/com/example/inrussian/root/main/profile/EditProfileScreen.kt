package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.EditProfileComponent


@Composable
 fun EditProfileScreen(component: EditProfileComponent) {
    val state by component.state.subscribeAsState()
    val working = state.workingCopy

    if (state.isLoading || working == null) {
        Box(Modifier.Companion.fillMaxSize(), contentAlignment = Alignment.Companion.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Редактирование профиля", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = working.surname,
            onValueChange = component::updateSurname,
            label = { Text("Фамилия") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.name,
            onValueChange = component::updateName,
            label = { Text("Имя") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.patronymic.orEmpty(),
            onValueChange = component::updatePatronymic,
            label = { Text("Отчество") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.dob,
            onValueChange = component::updateDob,
            label = { Text("Дата рождения (YYYY-MM-DD)") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.citizenship.orEmpty(),
            onValueChange = component::updateCitizenship,
            label = { Text("Гражданство") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.nationality.orEmpty(),
            onValueChange = component::updateNationality,
            label = { Text("Национальность") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.countryOfResidence.orEmpty(),
            onValueChange = component::updateCountryOfResidence,
            label = { Text("Страна проживания") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.cityOfResidence.orEmpty(),
            onValueChange = component::updateCityOfResidence,
            label = { Text("Город проживания") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.education.orEmpty(),
            onValueChange = component::updateEducation,
            label = { Text("Образование") },
            modifier = Modifier.Companion.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.purposeOfRegister.orEmpty(),
            onValueChange = component::updatePurpose,
            label = { Text("Цель регистрации") },
            modifier = Modifier.Companion.fillMaxWidth()
        )

        GenderSelector(
            selected = working.gender,
            onSelect = component::updateGender
        )

        Spacer(Modifier.Companion.height(8.dp))
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = component::onBack,
                modifier = Modifier.Companion.weight(1f)
            ) { Text("Назад") }

            Button(
                onClick = component::save,
                enabled = state.canSave && !state.isSaving,
                modifier = Modifier.Companion.weight(1f)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(Modifier.Companion.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
}