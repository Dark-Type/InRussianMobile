package com.example.inrussian.root.auth.register

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.onboarding.personalData.PersonalDataComponent
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.reallyLightGrey
import inrussian.composeapp.generated.resources.Res
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
import java.util.Calendar
import java.util.Date

@Composable
fun PersonaDataUi(component: PersonalDataComponent) {
    val state by component.state.subscribeAsState()
    Box() {

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
                ContinueButton(state.isEnableContinueButton, component::onNext)
            }
            Text(
                stringResource(Res.string.personal_data),
                fontSize = 40.sp,
                fontWeight = FontWeight.W600
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
                InputFormField(
                    state.surname,
                    component::changeSurname,
                    stringResource(Res.string.second_name),
                    isRequired = true
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                InputFormField(
                    state.name,
                    component::changeName,
                    stringResource(Res.string.first_name),
                    isRequired = true
                )

                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                InputFormField(
                    state.patronymic,
                    component::changeThirdName,
                    stringResource(Res.string.third_name)
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                InputFormField(
                    state.gender,
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
                    state.birthDate,
                    {},
                    stringResource(Res.string.dob),
                    Modifier,
                    vectorResource(Res.drawable.calendar),
                    component::openDataPicker,
                    true,
                    isRequired = true
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                InputFormField(
                    state.phoneNumber, component::changePhone, stringResource(Res.string.phone),
                    isRequired = true
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                InputFormField(
                    state.email, component::changeEmail, stringResource(Res.string.email),
                    isRequired = true
                )
            }
        }
        DataPickerSimple(state.showDataPicker, component::onDataChange)
        if (state.isGenderOpen)
            Column(
                Modifier
                    .offset(120.dp, 220.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(White)
                    .align(Alignment.Center)
                    .shadow(1.dp, shape = RoundedCornerShape(10.dp))
                    .width(IntrinsicSize.Min)
            ) {
                TextButton({
                    component.changeGender("Мужской")
                    component.changeGenderChoose(false)
                }) { Text("Мужской", color = DarkGrey.copy(0.8f)) }
                HorizontalDivider()
                TextButton({
                    component.changeGender("Женский")
                    component.changeGenderChoose(false)
                }
                ) { Text("Женский", color = DarkGrey.copy(0.8f)) }
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
    onIconClick: () -> Unit = {},
    onlyRead: Boolean = false,
    isRequired: Boolean = false
) {
    OutlinedTextField(
        value,
        onTextChange,
        modifier.fillMaxWidth(),
        readOnly = onlyRead,
        placeholder = {
            Text(buildAnnotatedString {
                append(placeholder)
                if (isRequired) withStyle(style = SpanStyle(color = Orange)) {
                    append("*")
                }
            })
        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPickerSimple(showDataPicker: Boolean, onSelect: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    var date by remember { mutableStateOf("") }

    val datePicker = remember {
        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                date = "$mDayOfMonth-${mMonth + 1}-$mYear"
            },
            year,
            month,
            day
        )
    }

    LaunchedEffect(showDataPicker) {
        if (showDataPicker)
            datePicker.show()
    }

    DisposableEffect(datePicker) {
        onDispose { datePicker.dismiss() }
    }

    LaunchedEffect(date) {
        if (date.isNotBlank()) onSelect(date)
    }
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

    override val state = MutableValue(PersonalDataComponent.State(isGenderOpen = true))
    override fun changeSurname(surname: String) {
        TODO("Not yet implemented")
    }

    override fun changeName(name: String) {
        TODO("Not yet implemented")
    }

    override fun changeThirdName(thirdName: String) {
        TODO("Not yet implemented")
    }

    override fun changeGender(gender: String) {
        TODO("Not yet implemented")
    }

    override fun changeDob(dob: String) {
        TODO("Not yet implemented")
    }

    override fun changePhone(phone: String) {
        TODO("Not yet implemented")
    }

    override fun changeEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun changeGenderChoose(isOpen: Boolean) {
        TODO("Not yet implemented")
    }

    override fun openDataPicker() {
        TODO("Not yet implemented")
    }

    override fun onDataChange(date: String) {
        TODO("Not yet implemented")
    }

    override fun dataPickerMissClick() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun onContinue() {
        TODO("Not yet implemented")
    }


}