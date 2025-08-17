package com.example.inrussian.root.auth.recovery.email


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.EnterEmailComponent
import com.example.inrussian.stores.auth.recovery.RecoveryStore
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.password_recovery
import inrussian.composeapp.generated.resources.send_code
import inrussian.composeapp.generated.resources.write_email
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EnterEmailUi(component: EnterEmailComponent) {
    val state by component.state.collectAsState()

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
            onValueChange = component::omEmailChange,
            label = stringResource(Res.string.email),
        )

        Spacer(modifier = Modifier.height(125.dp))
        CommonButton(
            onClick = component::onContinueClick,
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

    override val state = MutableStateFlow(RecoveryStore.State())
    override fun omEmailChange(email: String) {
        TODO("Not yet implemented")
    }


    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

    override fun onContinueClick() {
        TODO("Not yet implemented")
    }
}