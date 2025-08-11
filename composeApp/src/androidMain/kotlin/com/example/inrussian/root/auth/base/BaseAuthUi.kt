package com.example.inrussian.root.auth.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import com.example.inrussian.components.auth.base.BaseAuthComponent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseAuthUi(component: BaseAuthComponent) {



    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.fillMaxHeight(0.7f))

        Button(
            onClick = { component.onLoginClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        Button(
            onClick = { component.onRegisterClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Регистрация")
        }
        Button(
            onClick = { component.onSsoClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Войти через SSO")
        }
    }
}