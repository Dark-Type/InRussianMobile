package com.example.inrussian.root.auth.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.auth.base.BaseAuthComponent
import com.example.inrussian.ui.theme.Black
import com.example.inrussian.ui.theme.Blue
import com.example.inrussian.ui.theme.LightBlue
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.enter_with_ss
import inrussian.composeapp.generated.resources.placeholder
import inrussian.composeapp.generated.resources.sign_in
import inrussian.composeapp.generated.resources.sign_in_with_vk
import inrussian.composeapp.generated.resources.sign_in_with_yandex
import inrussian.composeapp.generated.resources.sign_up
import inrussian.composeapp.generated.resources.vk
import inrussian.composeapp.generated.resources.yandex
import nekit.corporation.shift_app.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BaseAuthUi(component: BaseAuthComponent) {
    val currentColors = LocalExtraColors.current
    val buttonHeight = 45.dp
    val buttonShape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .background(currentColors.baseBackground)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
        ) {
            val adaptiveFontSize = (maxHeight.value * 0.35).sp
            Button(
                onClick = { component.onRegisterClicked() },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ).copy(containerColor = Blue, disabledContainerColor = LightBlue.copy(alpha = 0.8f)),
                shape = buttonShape
            ) {
                Text(stringResource(Res.string.sign_up), fontSize = adaptiveFontSize)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
        ) {
            val adaptiveFontSize = (maxHeight.value * 0.35).sp
            Button(
                onClick = { component.onLoginClicked() },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ).copy(containerColor = Orange, disabledContainerColor = Orange.copy(alpha = 0.8f)),
                shape = buttonShape
            ) {
                Text(stringResource(Res.string.sign_in), fontSize = adaptiveFontSize)
            }
        }



        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = currentColors.stroke
            )
            Text(
                stringResource(Res.string.enter_with_ss),
                Modifier.padding(horizontal = 10.dp),
                textAlign = TextAlign.Center,
                color = currentColors.stroke
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = currentColors.stroke
            )
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
        ) {
            val adaptiveFontSize = (maxHeight.value * 0.35).sp
            Button(
                onClick = { component.onVkClicked() },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ).copy(
                    containerColor = LightBlue,
                    disabledContainerColor = LightBlue.copy(alpha = 0.8f)
                ),
                shape = buttonShape
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.sign_in_with_vk), fontSize = adaptiveFontSize)
                    Spacer(Modifier.width(14.dp))
                    Icon(
                        painter = painterResource(Res.drawable.vk),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
        ) {
            val adaptiveFontSize = (maxHeight.value * 0.35).sp
            Button(
                onClick = { component.onYandexClicked() },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ).copy(
                    containerColor = Black,
                    disabledContainerColor = Orange.copy(alpha = 0.8f)
                ),
                shape = buttonShape
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.sign_in_with_yandex), fontSize = adaptiveFontSize)
                    Spacer(modifier = Modifier.width(14.dp))
                    Icon(
                        painter = painterResource(Res.drawable.yandex),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

class BaseAuth() : BaseAuthComponent {


    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun PreviewBaseAuthUi() {
        BaseAuthUi(this)
    }

    override fun onLoginClicked() {
        TODO("Not yet implemented")
    }

    override fun onRegisterClicked() {
        TODO("Not yet implemented")
    }

    override fun onVkClicked() {
        TODO("Not yet implemented")
    }

    override fun onYandexClicked() {
        TODO("Not yet implemented")
    }


}