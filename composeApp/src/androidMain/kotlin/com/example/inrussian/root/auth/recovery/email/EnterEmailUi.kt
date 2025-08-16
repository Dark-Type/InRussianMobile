package com.example.inrussian.root.auth.recovery.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.EnterEmailComponent
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.EnterRecoveryCodeComponent
import com.example.inrussian.root.auth.recovery.code.EnterRecoveryCodeUi
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.password_recovery
import inrussian.composeapp.generated.resources.send_code
import inrussian.composeapp.generated.resources.write_email
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnterEmailUi(component: EnterEmailComponent) {
    var email by remember { mutableStateOf("") }

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
            value = email,
            onValueChange = { email = it },
            label = stringResource(Res.string.email),
        )

        Spacer(modifier = Modifier.height(125.dp))
        CommonButton(
            onClick = { },
            text = stringResource(Res.string.send_code),
            enable = false,
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}

class RegisterUi : EnterEmailComponent {


    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewRegisterUi() {
        EnterEmailUi(this)
    }


    override fun onEmailEntered(email: String) {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }
}