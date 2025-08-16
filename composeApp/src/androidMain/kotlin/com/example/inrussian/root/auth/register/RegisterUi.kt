package com.example.inrussian.root.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.auth.register.RegisterComponent
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.CommonTextField
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.confirm_password
import inrussian.composeapp.generated.resources.email
import inrussian.composeapp.generated.resources.password
import inrussian.composeapp.generated.resources.placeholder
import inrussian.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterUi(component: RegisterComponent) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            value = email,
            onValueChange = { email = it },
            label = stringResource(Res.string.email),
        )
        Spacer(modifier = Modifier.height(8.dp))
        CommonTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(Res.string.password),
        )
        Spacer(modifier = Modifier.height(8.dp))
        CommonTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(Res.string.confirm_password),
        )
        Spacer(modifier = Modifier.height(57.dp))
        CommonButton(
            onClick = { component.onRegister(email, password) },
            text = stringResource(Res.string.sign_in),
            enable = false,
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

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PreviewRegisterUi() {
        RegisterUi(this)
    }
}