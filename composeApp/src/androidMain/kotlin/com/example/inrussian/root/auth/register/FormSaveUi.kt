package com.example.inrussian.root.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.onboarding.confirmation.ConfirmationComponent
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.lightGreen
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.checkmark_circle
import inrussian.composeapp.generated.resources.`continue`
import inrussian.composeapp.generated.resources.success_save_date
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun FormSaveUi(component: ConfirmationComponent) {
    Column(Modifier.padding(horizontal = 28.dp)) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(vectorResource(Res.drawable.checkmark_circle), "", tint = lightGreen)
            Text(
                stringResource(Res.string.success_save_date),
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 60.dp),
                textAlign = TextAlign.Center
            )
        }
        CommonButton(stringResource(Res.string.`continue`), true, component::onConfirm)
        Spacer(Modifier.height(40.dp))
    }
}

class FormSaveUi : ConfirmationComponent {
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        FormSaveUi(this)
    }

    override fun onConfirm() {
        TODO("Not yet implemented")
    }

}