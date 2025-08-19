package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.onboarding.personalData.PersonalDataComponent
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.reallyLightGrey
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.back
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.calendar
import inrussian.composeapp.generated.resources.dob
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.first_name
import inrussian.composeapp.generated.resources.gender
import inrussian.composeapp.generated.resources.personal_data
import inrussian.composeapp.generated.resources.phone
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.second_name
import inrussian.composeapp.generated.resources.third_name
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PersonaDataUi(component: PersonalDataComponent) {
    val state = component.state
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
            ContinueButton(state.isEnableContinueButton, component::onContinue)
        }
        Text(
            stringResource(Res.string.personal_data), fontSize = 40.sp, fontWeight = FontWeight.W600
        )
        Box(
            Modifier
                .weight(0.8f)
                .fillMaxWidth(),

            ) {
            Icon(
                vectorResource(Res.drawable.profile),
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
                .padding(bottom = 72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White)
        ) {
            InputFormField("", {}, stringResource(Res.string.second_name))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.first_name))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.third_name))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField(
                "",
                {},
                stringResource(Res.string.gender),
                Modifier,
                vectorResource(Res.drawable.down_arrow),
                {})
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField(
                "",
                {},
                stringResource(Res.string.dob),
                Modifier,
                vectorResource(Res.drawable.calendar),
                {})
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.phone))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.email))
        }
    }
}


@Composable
fun InputFormField(
    value: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {}
) {
    OutlinedTextField(
        value,
        onTextChange,
        modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        trailingIcon = if (icon == null) null else {
            @Composable() {
                IconButton(onIconClick) {
                    Icon(icon, "")
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = White.copy(alpha = 0f),
            unfocusedBorderColor = White.copy(alpha = 0f),
        ))
}

class PersonalDataUi() : PersonalDataComponent {
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        PersonaDataUi(this)
    }

    override fun onNext() {
        TODO("Not yet implemented")
    }

    override val state = PersonalDataComponent.State()
    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun onContinue() {
        TODO("Not yet implemented")
    }
}