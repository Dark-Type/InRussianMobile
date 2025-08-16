package com.example.inrussian.root.auth.recovery.code

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.EnterRecoveryCodeComponent
import com.example.inrussian.models.EnterRecoveryCodeState
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import com.example.inrussian.ui.theme.DarkGrey100
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.check_spam
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.is_no_got_mail
import inrussian.composeapp.generated.resources.password_recovery
import inrussian.composeapp.generated.resources.question
import inrussian.composeapp.generated.resources.repeat_send_code
import inrussian.composeapp.generated.resources.sms_code
import inrussian.composeapp.generated.resources.support_contact
import inrussian.composeapp.generated.resources.write_email
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EnterRecoveryCodeUi(component: EnterRecoveryCodeComponent) {
    val state by component.state.collectAsState()


    Box() {
        Column(modifier = Modifier.padding(horizontal = 28.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.password_recovery),
                    "",
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .aspectRatio(1f)
                )
                Text(
                    stringResource(Res.string.password_recovery),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(top = 30.dp),
                    textAlign = TextAlign.Center
                )
            }
            Text(
                stringResource(Res.string.write_email), fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 30.dp),
            )
            CommonTextField(
                value = state.email,
                onValueChange = component::emailChange,
                label = stringResource(Res.string.email),
            )

            Spacer(modifier = Modifier.height(18.dp))
            CommonTextField(
                value = state.code,
                onValueChange = component::codeChange,
                label = stringResource(Res.string.sms_code),
            )
            Spacer(modifier = Modifier.height(50.dp))
            CommonButton(
                onClick = { },
                text = stringResource(Res.string.repeat_send_code) + " " + state.timer,
                enable = false,
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
        IconButton(
            component::onQuestionClick, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(28.dp)
        ) {
            Icon(vectorResource(Res.drawable.question), "", tint = Orange)
        }
        if (state.showHint)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .clickable {
                        component.onMissClick()
                    },
                contentAlignment = Alignment.Center,

                ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(LightGrey)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        stringResource(Res.string.is_no_got_mail),
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp, bottom = 4.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = DarkGrey100
                    )
                    Text(
                        stringResource(Res.string.check_spam),
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = DarkGrey100.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider()
                    TextButton(
                        component::onSupportContactClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(Res.string.support_contact), color = LightBlue)
                    }
                }
            }

    }

}

class RegisterUi : EnterRecoveryCodeComponent {
    override val state = MutableStateFlow<EnterRecoveryCodeState>(
        EnterRecoveryCodeState(
            email = "", code = "", timer = "", sendButtonEnable = false, showHint = false
        )
    )

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewRegisterUi() {
        EnterRecoveryCodeUi(this)
    }

    override fun onCodeEntered(code: String) {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

    override fun onQuestionClick() {
        TODO("Not yet implemented")
    }

    override fun emailChange(email: String) {
        TODO("Not yet implemented")
    }

    override fun codeChange(code: String) {
        TODO("Not yet implemented")
    }

    override fun onMissClick() {
        TODO("Not yet implemented")
    }

    override fun onSupportContactClick() {
        TODO("Not yet implemented")
    }
}