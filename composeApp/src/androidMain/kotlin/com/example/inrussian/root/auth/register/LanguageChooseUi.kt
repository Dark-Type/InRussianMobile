package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.onboarding.language.LanguageComponent
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.app_language
import inrussian.composeapp.generated.resources.app_n_language
import inrussian.composeapp.generated.resources.checkmark_circle
import inrussian.composeapp.generated.resources.language
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.tell_about_u
import com.example.inrussian.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LanguageChooseUi(component: LanguageComponent) {
    val state by component.state.subscribeAsState()
    val currentColors = LocalExtraColors.current
    val languages = listOf(
        "RUSSIAN" to "Русский",
        "ENGLISH" to "English",
        "CHINESE" to "中文",
        "UZBEK" to "O‘zbek",
        "TAJIK" to "Тоҷикӣ",
        "HINDI" to "हिन्दी"
    )

    var showPrivacyDialog by remember { mutableStateOf(false) }

    Box {
        Column(
            Modifier
                .background(currentColors.secondaryBackground)
                .padding(horizontal = 22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.app_n_language),
                    style = MaterialTheme.typography.titleLarge,
                    color = currentColors.fontCaptive
                )

                Spacer(modifier = Modifier.weight(1f))

                ContinueButton(state.isActiveContinueButton, component::onNext)
            }
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
                    color = currentColors.fontCaptive,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W600,
                    lineHeight = 30.sp
                )
            }
            Spacer(Modifier.weight(0.1f))
            PermissionRow(
                isSelected = state.hasGivenPermission,
                onClick = { component.clickOnToggleButton(!state.hasGivenPermission) },
                onShowPrivacy = { showPrivacyDialog = true }
            )
            Spacer(Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 74.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(currentColors.componentBackground)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    stringResource(Res.string.app_language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = currentColors.fontCaptive
                )
                Spacer(Modifier.weight(1f))
                Text(
                    languages.firstOrNull { it.first == state.selectedLanguage }?.second
                        ?: state.selectedLanguage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = currentColors.fontCaptive
                )
                IconButton({ component.openMenu() }) {
                    Icon(
                        vectorResource(Res.drawable.language),
                        "",
                        tint = LightBlue
                    )
                }
            }
        }

        if (state.isOpenLanguage) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { component.closeMenu() })
                    }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .widthIn(min = 220.dp, max = 320.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp))
                        .background(currentColors.componentBackground, RoundedCornerShape(20.dp))
                ) {
                    Text(
                        text = "Выберите язык",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        color = currentColors.fontCaptive,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 28.dp, bottom = 12.dp)

                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, currentColors.stroke)
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 340.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        itemsIndexed(languages) { idx, lang ->
                            TextButton(
                                onClick = {
                                    component.selectLanguage(lang.first)
                                    component.closeMenu()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        lang.second,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (state.selectedLanguage == lang.first) Orange else currentColors.fontCaptive,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (state.selectedLanguage == lang.first) {
                                        Icon(
                                            vectorResource(Res.drawable.checkmark_circle),
                                            contentDescription = null,
                                            tint = Orange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            if (idx < languages.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                    thickness = DividerDefaults.Thickness,
                                    color = currentColors.stroke
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        if (showPrivacyDialog) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { showPrivacyDialog = false })
                    }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .widthIn(min = 220.dp, max = 320.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
                        .background(currentColors.componentBackground, RoundedCornerShape(20.dp))
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(
                        "Политика конфиденциальности",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = currentColors.fontCaptive
                    )
                    Spacer(Modifier.height(18.dp))
                    Text(
                        "Ваши персональные данные используются только с целью предоставления функциональности приложения и не передаются третьим лицам. Честно-честно. PS: не забыть заменить эти данные на реальную политику конфиденциальности.",
                        fontSize = 15.sp,
                        color = currentColors.fontInactive,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { showPrivacyDialog = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = White
                        )
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionRow(
    isSelected: Boolean,
    onClick: (Boolean) -> Unit,
    onShowPrivacy: () -> Unit
) {
    val currentColors = LocalExtraColors.current
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(currentColors.componentBackground)
            .clickable { onClick(!isSelected) }
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton({ onShowPrivacy() }, Modifier.size(22.dp)) {
            Icon(
                vectorResource(Res.drawable.profile),
                "",
                modifier = Modifier.size(18.dp),
                tint = LightBlue
            )
        }
        Column(Modifier
            .padding(start = 8.dp)
            .weight(1f)) {
            Text(
                "Я даю согласие",
                color = LightBlue,
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier
                    .clickable { onShowPrivacy() }
            )
            Text(
                "на обработку персональных данных",
                fontSize = 15.sp,
                color = currentColors.footnote
            )
        }
        Checkbox(
            isSelected,
            { onClick(!isSelected) },
            Modifier.align(Alignment.CenterVertically),
            colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = LightBlue,
                checkedCheckmarkColor = White,
                checkedBorderColor = LightBlue,
                uncheckedBoxColor = currentColors.componentBackground,
                uncheckedCheckmarkColor = LightBlue,
                uncheckedBorderColor = LightBlue,
            )
        )
    }
}


//class LanguageChooseUi : LanguageComponent {
//    override fun onNext() {
//        TODO("Not yet implemented")
//    }
//
//    override fun openMenu() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBack() {
//        TODO("Not yet implemented")
//    }
//
//    override fun clickOnToggleButton(isSelected: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override val state = MutableStateFlow(
//        LanguageComponent.State()
//    )
//
//    @Preview(showBackground = true, showSystemUi = true)
//    @Composable
//    fun Preview() {
//        LanguageChooseUi(this)
//    }
//}