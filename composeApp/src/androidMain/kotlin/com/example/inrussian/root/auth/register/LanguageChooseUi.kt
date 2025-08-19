package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.onboarding.language.LanguageComponent
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.reallyLightGrey
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.app_language
import inrussian.composeapp.generated.resources.app_n_language
import inrussian.composeapp.generated.resources.give_permission
import inrussian.composeapp.generated.resources.language
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.tell_about_u
import inrussian.composeapp.generated.resources.to_update_data
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LanguageChooseUi(component: LanguageComponent) {
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
            ContinueButton(state.hasGivenPermission, component::onNext)
        }
        Text(
            stringResource(Res.string.app_n_language),
            fontSize = 40.sp,
            fontWeight = FontWeight.W600
        )
        Column(
            Modifier.weight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                vectorResource(Res.drawable.profile),
                "",
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 22.dp),
                tint = Orange
            )
            Text(
                stringResource(Res.string.tell_about_u),
                fontSize = 20.sp,
                color = DarkGrey.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W600,
                lineHeight = 30.sp
            )
        }
        Spacer(Modifier.weight(0.1f))
        PermissionRow(state.hasGivenPermission, component::clickOnToggleButton)
        Spacer(Modifier.height(18.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 74.dp)
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
            Text(
                state.selectedLanguage, fontSize = 16.sp, fontWeight = FontWeight.W400
            )
            IconButton(component::openMenu) {
                Icon(
                    vectorResource(Res.drawable.language),
                    "",
                    tint = LightBlue
                )
            }
        }
    }
}

@Composable
fun PermissionRow(isSelected: Boolean, onClick: (Boolean) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 10.dp)
    ) {
        IconButton({}, Modifier.size(16.dp)) {
            Icon(
                vectorResource(Res.drawable.profile),
                "",
                modifier = Modifier.size(16.dp),
                tint = LightBlue
            )
        }
        Text(
            stringResource(Res.string.give_permission),
            Modifier.padding(start = 20.dp),
            color = LightBlue,
            fontSize = 15.sp
        )
        Text(
            stringResource(Res.string.to_update_data),
            Modifier.padding(top = 20.dp),
            fontSize = 15.sp,
            color = DarkGrey.copy(alpha = 0.8f)
        )
        Checkbox(
            isSelected,
            onClick,
            Modifier.align(Alignment.TopEnd),
            colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = LightBlue,
                checkedCheckmarkColor = White,
                checkedBorderColor = LightBlue,
                uncheckedBoxColor = White,
                uncheckedCheckmarkColor = LightBlue,
                uncheckedBorderColor = LightBlue,
            )
        )
    }
}



class LanguageChooseUi : LanguageComponent {
    override fun onNext() {
        TODO("Not yet implemented")
    }

    override fun openMenu() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun clickOnToggleButton(isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override val state = LanguageComponent.State()

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        LanguageChooseUi(this)
    }
}