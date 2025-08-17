package com.example.inrussian.root.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.auth.login.LoginComponent
import com.example.inrussian.stores.auth.login.LoginStore.State
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.cancel
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.eye_off
import inrussian.composeapp.generated.resources.eye_show
import inrussian.composeapp.generated.resources.forgot_password
import inrussian.composeapp.generated.resources.password
import inrussian.composeapp.generated.resources.placeholder
import inrussian.composeapp.generated.resources.sign_in
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LoginUi(component: LoginComponent) {
    val state by component.state.collectAsState(Dispatchers.Main.immediate)

    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.placeholder),
                "",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        CommonTextField(
            value = state.email,
            onValueChange = { component.onEmailChange(it) },
            label = stringResource(Res.string.email),
            error = state.emailError,
            icon = if (state.email.isBlank()) null else vectorResource(
                Res.drawable.cancel
            ),
            onIconClick = component::onDeleteEmailClick,
        )
        CommonTextField(
            value = state.password,
            onValueChange = component::onPasswordChange,
            label = stringResource(Res.string.password),
            error = state.passwordError,
            icon = if (state.password.isBlank()) null else if (state.showPassword) vectorResource(
                Res.drawable.eye_show
            ) else vectorResource(
                Res.drawable.eye_off
            ),
            onIconClick = component::onShowPasswordClick,
            visualTransformation = if (state.showPassword) PasswordVisualTransformation() else null
        )
        Spacer(modifier = Modifier.height(16.dp))
        CommonButton(
            onClick = { component.onLogin(state.email, state.password) },
            text = stringResource(Res.string.sign_in),
            enable = state.isButtonActive,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { component.onForgotPasswordClicked() }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.forgot_password))
        }
        Spacer(modifier = Modifier.height(40.dp))

    }
}


class LoginUi() : LoginComponent {
    override val state = MutableStateFlow(State())
    override fun onLogin(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun onForgotPasswordClicked() {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

    override fun onShowPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onDeleteEmailClick() {
        TODO("Not yet implemented")
    }

    override fun onEmailChange(email: String) {
        TODO("Not yet implemented")
    }

    override fun onPasswordChange(password: String) {
        TODO("Not yet implemented")
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewLoginUi() {
        LoginUi(this)
    }
}