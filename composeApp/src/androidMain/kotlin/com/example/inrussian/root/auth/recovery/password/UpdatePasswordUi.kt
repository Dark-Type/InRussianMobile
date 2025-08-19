package com.example.inrussian.root.auth.recovery.password

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.auth.passwordRecovery.updatePassword.UpdatePasswordComponent
import com.example.inrussian.models.state.UpdatePasswordState
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import com.example.inrussian.ui.theme.DarkGrey
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.choose_new_password
import inrussian.composeapp.generated.resources.confirm_password
import inrussian.composeapp.generated.resources.eye_off
import inrussian.composeapp.generated.resources.eye_show
import inrussian.composeapp.generated.resources.password
import inrussian.composeapp.generated.resources.password_recovery
import inrussian.composeapp.generated.resources.update_password
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun UpdatePasswordUi(component: UpdatePasswordComponent) {
    val state by component.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
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
            stringResource(Res.string.choose_new_password),
            fontSize = 16.sp,
            color = DarkGrey.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        CommonTextField(
            value = state.password,
            onValueChange = component::onPasswordChange,
            label = stringResource(Res.string.password),
            icon = if (state.showPassword) vectorResource(Res.drawable.eye_show) else vectorResource(
                Res.drawable.eye_off
            ),
            onIconClick = component::onShowPasswordClick,
            visualTransformation = if (state.showPassword) PasswordVisualTransformation() else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        CommonTextField(
            value = state.confirmPassword,
            onValueChange = component::onConfirmPasswordChange,
            label = stringResource(Res.string.confirm_password),
            icon = if (state.showConfirmPassword) vectorResource(Res.drawable.eye_show) else vectorResource(
                Res.drawable.eye_off
            ),
            onIconClick = component::onShowConfirmPasswordClick,
            visualTransformation = if (state.showConfirmPassword) PasswordVisualTransformation() else null
        )


        Spacer(modifier = Modifier.height(60.dp))
        CommonButton(
            onClick = { component.onBackClicked() },
            enable = state.updateButtonEnable,
            text = stringResource(Res.string.update_password)
        )
        Spacer(modifier = Modifier.height(34.dp))
    }
}

class UpdatePasswordUi : UpdatePasswordComponent {
    override fun onPasswordUpdated() {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(password: String) {
        TODO("Not yet implemented")
    }

    override fun onConfirmPasswordChange(confirmPassword: String) {
        TODO("Not yet implemented")
    }

    override fun onShowPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onShowConfirmPasswordClick() {
        TODO("Not yet implemented")
    }

    override val state = MutableStateFlow(UpdatePasswordState())

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        UpdatePasswordUi(this)
    }
}