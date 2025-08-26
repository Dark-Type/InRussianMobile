package com.example.inrussian.root.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.auth.register.RegisterComponent
import com.example.inrussian.stores.auth.register.RegisterStore
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.cancel
import inrussian.composeapp.generated.resources.confirm_password
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.eye_off
import inrussian.composeapp.generated.resources.eye_show
import inrussian.composeapp.generated.resources.password
import inrussian.composeapp.generated.resources.placeholder
import inrussian.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RegisterUi(component: RegisterComponent) {
    val state by component.state.subscribeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            BackButton(enable = false, onClick = component::onBackClicked)
        }
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
            onValueChange = component::changeEmail,
            label = stringResource(Res.string.email),
            icon = if (state.email.isBlank()) null else vectorResource(
                Res.drawable.cancel
            ),
            onIconClick = component::onEmailDeleteClick,
            error = state.emailError
        )
        Spacer(modifier = Modifier.height(4.dp))
        CommonTextField(
            value = state.password,
            onValueChange = component::changePassword,
            label = stringResource(Res.string.password),
            icon = if (state.showPassword) vectorResource(Res.drawable.eye_show) else vectorResource(
                Res.drawable.eye_off
            ),
            onIconClick = component::onShowPasswordClick,
            visualTransformation = if (!state.showPassword) PasswordVisualTransformation() else null,

            error = state.passwordError
        )
        Spacer(modifier = Modifier.height(4.dp))
        CommonTextField(
            value = state.confirmPassword,
            onValueChange = component::changeConfirmPassword,
            label = stringResource(Res.string.confirm_password),
            icon = if (state.showConfirmPassword) vectorResource(Res.drawable.eye_show) else vectorResource(
                Res.drawable.eye_off
            ),
            onIconClick = component::onShowConfirmPasswordClick,
            visualTransformation = if (!state.showConfirmPassword) PasswordVisualTransformation() else null,
            error = state.confirmPasswordError
        )
        Spacer(modifier = Modifier.height(57.dp))
        CommonButton(
            onClick = { component.onRegister(state.email, state.password) },
            text = stringResource(Res.string.sign_in),
            enable = state.isButtonActive
        )
        Spacer(modifier = Modifier.height(45.dp))
    }
}

class RegisterUi : RegisterComponent {
    override fun onRegister(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

    override fun changeEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun changePassword(password: String) {
        TODO("Not yet implemented")
    }

    override fun changeConfirmPassword(confirmPassword: String) {
        TODO("Not yet implemented")
    }

    override fun onShowPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onShowConfirmPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onEmailDeleteClick() {
        TODO("Not yet implemented")
    }

    override val state = MutableValue(RegisterStore.State())

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewRegisterUi() {
      //  RegisterUi(this)
    }
}
