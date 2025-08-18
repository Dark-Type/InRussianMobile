package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.EditProfileComponent
import com.example.inrussian.components.main.profile.Gender
import com.example.inrussian.components.main.profile.ProfileComponent
import com.example.inrussian.components.main.profile.ProfileMainComponent
import com.example.inrussian.components.main.profile.ProfileMainState
import com.example.inrussian.components.main.profile.StaticTextState

@Composable
fun ProfileComponentUi(component: ProfileComponent) {
    val stack by component.stack.subscribeAsState()
    val current = stack.active.instance

    Surface(modifier = Modifier.fillMaxSize()) {
        when (current) {
            is ProfileComponent.Child.MainChild ->
                ProfileMainScreen(component = current.component)

            is ProfileComponent.Child.EditProfileChild ->
                EditProfileScreen(component = current.component)

            is ProfileComponent.Child.AboutChild ->
                StaticTextScreen(
                    stateValue = current.component.state,
                    onBack = current.component::onBack
                )

            is ProfileComponent.Child.PrivacyPolicyChild ->
                StaticTextScreen(
                    stateValue = current.component.state,
                    onBack = current.component::onBack
                )
        }
    }
}

/* -----------------------------------------------------------
 * Main Profile Screen
 * ----------------------------------------------------------- */
@Composable
private fun ProfileMainScreen(component: ProfileMainComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HeaderSection(state = state, component = component)
        BadgesSection(state = state)
        ActionButtons(component = component)
    }
}

@Composable
private fun HeaderSection(
    state: ProfileMainState,
    component: ProfileMainComponent
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Avatar(avatarId = state.user?.avatarId)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = state.displayName.ifBlank { "—" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Дата регистрации: ${state.registrationDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Уведомления", modifier = Modifier.weight(1f))
                Switch(
                    checked = state.notificationsEnabled,
                    onCheckedChange = { component.toggleNotifications() }
                )
            }

            OutlinedButton(onClick = { component.cycleTheme() }, modifier = Modifier.fillMaxWidth()) {
                Text("Тема: ${state.theme.readable()}")
            }
        }
    }
}

@Composable
private fun Avatar(avatarId: String?) {
    // If you later add an image loader (Coil / Kamel), plug it here.
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text("A", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun BadgesSection(state: ProfileMainState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Значки", style = MaterialTheme.typography.titleMedium)
        if (state.badges.isEmpty()) {
            Text("Пока нет значков", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.badges) { badge ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.widthIn(min = 72.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                badge.name.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            badge.name,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(component: ProfileMainComponent) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = component::onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Редактировать профиль") }

        OutlinedButton(
            onClick = component::onAboutClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("О приложении") }

        OutlinedButton(
            onClick = component::onPrivacyPolicyClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Политика конфиденциальности") }
    }
}

/* -----------------------------------------------------------
 * Edit Profile Screen
 * ----------------------------------------------------------- */
@Composable
private fun EditProfileScreen(component: EditProfileComponent) {
    val state by component.state.subscribeAsState()
    val working = state.workingCopy

    if (state.isLoading || working == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
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
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.name,
            onValueChange = component::updateName,
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.patronymic.orEmpty(),
            onValueChange = component::updatePatronymic,
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.dob,
            onValueChange = component::updateDob,
            label = { Text("Дата рождения (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.citizenship.orEmpty(),
            onValueChange = component::updateCitizenship,
            label = { Text("Гражданство") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.nationality.orEmpty(),
            onValueChange = component::updateNationality,
            label = { Text("Национальность") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.countryOfResidence.orEmpty(),
            onValueChange = component::updateCountryOfResidence,
            label = { Text("Страна проживания") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.cityOfResidence.orEmpty(),
            onValueChange = component::updateCityOfResidence,
            label = { Text("Город проживания") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.education.orEmpty(),
            onValueChange = component::updateEducation,
            label = { Text("Образование") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = working.purposeOfRegister.orEmpty(),
            onValueChange = component::updatePurpose,
            label = { Text("Цель регистрации") },
            modifier = Modifier.fillMaxWidth()
        )

        GenderSelector(
            selected = working.gender,
            onSelect = component::updateGender
        )

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = component::onBack,
                modifier = Modifier.weight(1f)
            ) { Text("Назад") }

            Button(
                onClick = component::save,
                enabled = state.canSave && !state.isSaving,
                modifier = Modifier.weight(1f)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
}

@Composable
private fun GenderSelector(selected: Gender, onSelect: (Gender) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Пол", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Gender.entries.forEach { g ->
                FilterChip(
                    selected = selected == g,
                    onClick = { onSelect(g) },
                    label = { Text(g.name) }
                )
            }
        }
    }
}

/* -----------------------------------------------------------
 * Static Text Screens (About / Privacy)
 * ----------------------------------------------------------- */
@Composable
private fun StaticTextScreen(
    stateValue: com.arkivanov.decompose.value.Value<StaticTextState>,
    onBack: () -> Unit
) {
    val state by stateValue.subscribeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(state.title, style = MaterialTheme.typography.titleLarge)
        Text(state.text, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
    }
}

/* -----------------------------------------------------------
 * Utilities
 * ----------------------------------------------------------- */
private fun AppTheme.readable(): String = when (this) {
    AppTheme.SYSTEM -> "СИСТЕМА"
    AppTheme.LIGHT -> "СВЕТЛАЯ"
    AppTheme.DARK -> "ТЕМНАЯ"
}