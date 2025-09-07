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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.profile.EditProfileComponent
import com.example.inrussian.components.main.profile.EditProfileState
import com.example.inrussian.components.main.profile.Gender
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.root.auth.register.ClipsContainer
import com.example.inrussian.root.auth.register.DataPickerSimple
import com.example.inrussian.root.auth.register.InputFormField
import com.example.inrussian.root.auth.register.TextWithToggle
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.activity_type
import inrussian.composeapp.generated.resources.app_language
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.calendar
import inrussian.composeapp.generated.resources.can_write_rus_speech
import inrussian.composeapp.generated.resources.citizenship
import inrussian.composeapp.generated.resources.citizenship_data
import inrussian.composeapp.generated.resources.dob
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.edit_profile
import inrussian.composeapp.generated.resources.education
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.first_name
import inrussian.composeapp.generated.resources.gender
import inrussian.composeapp.generated.resources.language
import inrussian.composeapp.generated.resources.language_proficiency
import inrussian.composeapp.generated.resources.nationality
import inrussian.composeapp.generated.resources.personal_data_
import inrussian.composeapp.generated.resources.phone
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.purpose
import inrussian.composeapp.generated.resources.residence_city
import inrussian.composeapp.generated.resources.residence_country
import inrussian.composeapp.generated.resources.save
import inrussian.composeapp.generated.resources.second_name
import inrussian.composeapp.generated.resources.study_country
import inrussian.composeapp.generated.resources.third_name
import inrussian.composeapp.generated.resources.time_of_stay
import inrussian.composeapp.generated.resources.understand_rus_language
import inrussian.composeapp.generated.resources.understand_rus_speech
import inrussian.composeapp.generated.resources.understand_rus_text
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


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

    Box(Modifier.background(LightGrey)) {
        LazyColumn(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(38.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = component::onBack) {
                            Icon(
                                vectorResource(Res.drawable.back_arrow),
                                contentDescription = null,
                                tint = Orange
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Icon(
                        vectorResource(Res.drawable.profile),
                        "",
                        Modifier.size(120.dp),
                        tint = Orange
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(Res.string.edit_profile),
                        Modifier.padding(horizontal = 30.dp),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W600
                    )
                }
            }
            item {
                Spacer(Modifier.height(60.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        stringResource(Res.string.app_language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400
                    )
                    Spacer(Modifier.weight(1f))
                    state.workingCopy?.systemLanguage?.let {
                        Text(
                            it.name, fontSize = 16.sp, fontWeight = FontWeight.W400
                        )
                    }

                    IconButton(component::openLanguage) {
                        Icon(
                            vectorResource(Res.drawable.language),
                            "",
                            tint = LightBlue
                        )
                    }
                }

            }
            item {
                Text(stringResource(Res.string.personal_data_), color = DarkGrey.copy(0.8f))
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                ) {
                    InputFormField(
                        state.workingCopy?.surname ?: "",
                        component::updateSurname,
                        stringResource(Res.string.second_name),
                        isRequired = true
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.name ?: "",
                        component::updateName,
                        stringResource(Res.string.first_name),
                        isRequired = true
                    )

                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.patronymic ?: "",
                        component::updatePatronymic,
                        stringResource(Res.string.third_name)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.gender?.name ?: "",
                        {},
                        stringResource(Res.string.gender),
                        Modifier,
                        vectorResource(Res.drawable.down_arrow),
                        { component.changeGenderChoose(!state.isGenderOpen) },
                        true,
                        isRequired = true
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.dob ?: "",
                        {},
                        stringResource(Res.string.dob),
                        Modifier,
                        vectorResource(Res.drawable.calendar),
                        component::openDate,
                        true,
                        isRequired = true
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.dor ?: "",
                        component::updateDor,
                        stringResource(Res.string.phone),
                        isRequired = true
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.email ?: "",
                        component::updateEmail,
                        stringResource(Res.string.email),
                        isRequired = true
                    )
                }
            }
            item {
                Text(stringResource(Res.string.citizenship_data), color = DarkGrey.copy(0.8f))
            }


            item() {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                ) {
                    ClipsContainer(
                        isOpen = state.isCitizenshipOpen,
                        variants = listOf(
                            "Indonezia",
                            "amsterdam",
                            "Filipin",
                            "Russia",
                            "Kyrgiztan"
                        ),
                        active = (listOf(state.workingCopy?.citizenship) ?: listOf()) as List<String>,
                        onClick = component::deleteCountry,
                        onChangeExpanded = component::openCitizenship,
                        onAddClick = component::selectCountry,
                        placeholder = stringResource(Res.string.citizenship)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ClipsContainer(
                        isOpen = state.isNationalityOpen,
                        variants = listOf("Evrye", "Russian", "Kazah"),
                        active = if (state.workingCopy?.nationality == "" || state.workingCopy?.nationality == null) listOf() else listOf(
                            state.workingCopy?.nationality ?: ""
                        ),
                        onClick = component::deleteNationality,
                        onChangeExpanded = component::openNationality,
                        onAddClick = component::selectNationality,
                        placeholder = stringResource(Res.string.nationality)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.countryOfResidence ?: "",
                        component::countryLiveUpdate,
                        stringResource(Res.string.residence_country)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.cityOfResidence ?: "",
                        component::cityLiveUpdate, stringResource(Res.string.residence_city)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.countryDuringEducation ?: "",
                        component::studyCountryUpdate,
                        stringResource(Res.string.study_country)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    ClipsContainer(
                        isOpen = state.isTimeOpen,
                        variants = listOf(
                            "До 5 лет",
                            "Меньше года",
                            "Больше года",
                            "Около трех лет"
                        ),
                        active = if (state.workingCopy?.nationality == "" || state.workingCopy?.nationality == null) listOf() else listOf(
                            state.workingCopy?.nationality ?: ""
                        ),
                        onClick = component::deleteTime,
                        onChangeExpanded = component::openNationality,
                        onAddClick = component::selectNationality,
                        placeholder = stringResource(Res.string.time_of_stay)
                    )
                }
            }
            item {
                Text(stringResource(Res.string.language_proficiency), color = DarkGrey.copy(0.8f))
            }


            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                ) {
                    ClipsContainer(
                        isOpen = state.isLanguageOpen,
                        variants = listOf("Russ", "NeRuss", "Japan"),
                        active = state.workingCopy?.languageSkills?.map { it.language } ?: listOf(),
                        onClick = component::deleteLanguage,
                        onChangeExpanded = component::onChangeExpanded,
                        onAddClick = component::selectLanguage,
                        placeholder = stringResource(Res.string.citizenship)
                    )

                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    TextWithToggle(stringResource(Res.string.understand_rus_speech), true, {})
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    TextWithToggle(stringResource(Res.string.understand_rus_language), true, {})
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    TextWithToggle(stringResource(Res.string.understand_rus_text), true, {})
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    TextWithToggle(stringResource(Res.string.can_write_rus_speech), true, {})
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))

                }
            }
            item {
                Text(stringResource(Res.string.education), color = DarkGrey.copy(0.8f))
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                ) {
                    InputFormField(
                        state.workingCopy?.kindOfActivity ?: "",
                        component::changeActivity,
                        stringResource(Res.string.activity_type),
                        Modifier
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.education ?: "",
                        component::changeEducation,
                        stringResource(Res.string.education)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    InputFormField(
                        state.workingCopy?.purposeOfRegister ?: "",
                        component::changePurpose,
                        stringResource(Res.string.purpose)
                    )
                }
            }
            item {
                CommonButton(stringResource(Res.string.save), true, component::save)

            }
            item { Spacer(Modifier.height(86.dp)) }
        }

        DataPickerSimple(state.isDateOpen, component::updateDob)
        if (state.isGenderOpen)
            Column(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .align(Alignment.Center)
                    .shadow(1.dp)
            ) {
                TextButton({
                    component.changeGender("Мужской")
                    component.changeGenderChoose(false)
                }) { Text("Мужской") }
                TextButton({
                    component.changeGender("Женский")
                    component.changeGenderChoose(false)
                }
                ) { Text("Женский") }
            }

    }


}


//class EditProfileScreen : EditProfileComponent {
//    override val state =
//        MutableValue(EditProfileState(isLoading = false, workingCopy = UserProfile()))
//
//    override fun updateSurname(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateName(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updatePatronymic(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateGender(value: Gender) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateDob(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateDor(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateEmail(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateCitizenship(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateNationality(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateCountryOfResidence(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateCityOfResidence(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateEducation(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updatePurpose(value: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openLanguage() {
//        TODO("Not yet implemented")
//    }
//
//    override fun changeGender(gender: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun changeGenderChoose(isOpen: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openDate() {
//        TODO("Not yet implemented")
//    }
//
//    override fun openCitizenship(isOpen: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openTime() {
//        TODO("Not yet implemented")
//    }
//
//    override fun save() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBack() {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteCountry(country: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteLanguage(language: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun selectCountry(country: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteNationality(nationality: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun selectNationality(nationality: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun openNationality(isOpen: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override fun countryLiveUpdate(county: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun cityLiveUpdate(county: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun studyCountryUpdate(county: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteTime(time: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onChangeExpanded(boolean: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override fun selectLanguage(language: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun changeActivity(activity: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun changeEducation(education: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun changePurpose(purpose: String) {
//        TODO("Not yet implemented")
//    }
//
//    @Composable
//    @Preview(showBackground = true, showSystemUi = true)
//    fun Preview() {
//        EditProfileScreen(this)
//    }
//}