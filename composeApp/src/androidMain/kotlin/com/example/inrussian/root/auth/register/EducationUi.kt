package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.onboarding.education.EducationComponent
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.reallyLightGrey
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.activity_type
import inrussian.composeapp.generated.resources.can_write_rus_speech
import inrussian.composeapp.generated.resources.citizenship
import inrussian.composeapp.generated.resources.education
import inrussian.composeapp.generated.resources.education_placeholder
import inrussian.composeapp.generated.resources.purpose
import inrussian.composeapp.generated.resources.understand_rus_language
import inrussian.composeapp.generated.resources.understand_rus_speech
import inrussian.composeapp.generated.resources.understand_rus_text
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EducationUi(component: EducationComponent) {
    val state by component.state.subscribeAsState()
    Column(
        Modifier
            .background(reallyLightGrey)
            .padding(horizontal = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(true, component::onBack)
            ContinueButton(state.continueEnable, component::onNext)
        }
        Text(
            stringResource(Res.string.education), fontSize = 40.sp, fontWeight = FontWeight.W600
        )
        Box(
            Modifier
                .weight(0.8f)
                .fillMaxWidth(),

            ) {
            Icon(
                vectorResource(Res.drawable.education_placeholder),
                "",
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 22.dp)
                    .align(Alignment.Center),
                tint = Orange
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White)
        ) {
            ClipsContainer(
                isOpen = state.isOpenLanguages,
                variants = listOf("Russ", "NeRuss", "Japan"),
                active = state.languages,
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
            InputFormField(
                state.kindOfActivity,
                component::changeActivity,
                stringResource(Res.string.activity_type),
                Modifier
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField(
                state.education,
                component::changeEducation,
                stringResource(Res.string.education)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField(
                state.purposeOfRegistration,
                component::changePurpose,
                stringResource(Res.string.purpose)
            )
        }
    }
}


@Composable
fun TextWithToggle(
    text: String, isActive: Boolean, onClick: (Boolean) -> Unit, isRequired: Boolean = true
) {
    Row(
        Modifier.padding(start = 16.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(buildAnnotatedString {
            append(text)
            if (isRequired) withStyle(style = SpanStyle(color = Orange)) {
                append("*")
            }
        }, fontSize = 16.sp)
        Spacer(Modifier.weight(1f))
        Checkbox(
            isActive, onClick, colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = Orange,
                checkedCheckmarkColor = White,
                checkedBorderColor = Orange,
                uncheckedBoxColor = White,
                uncheckedCheckmarkColor = Orange,
                uncheckedBorderColor = Orange,
            )
        )
    }
}

class EducationUi : EducationComponent {
    override val state = MutableValue(
        EducationComponent.State(languages = mutableListOf("Ural", "Oleg"), isOpenLanguages = true)
    )

    override fun onNext() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun deleteLanguage(string: String) {
        TODO("Not yet implemented")
    }


    override fun onChangeExpanded(boolean: Boolean) {
        TODO("Not yet implemented")
    }

    override fun selectLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun changeActivity(activity: String) {
        TODO("Not yet implemented")
    }

    override fun changeEducation(education: String) {
        TODO("Not yet implemented")
    }

    override fun changePurpose(purpose: String) {
        TODO("Not yet implemented")
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        EducationUi(this)
    }
}
