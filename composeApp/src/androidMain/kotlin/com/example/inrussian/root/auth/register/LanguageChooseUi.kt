package com.example.inrussian.root.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import inrussian.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageChooseUi() {
    Column() {
        ContinueButton()
        Text("")
        Column {
            Icon(vectorResource(Res.drawable.profile), "")
            Text("")
        }
        PermissionRow()
        Row {
            Text("")
            Spacer(Modifier.weight(1f))
            Text("")
            IconButton({}) { Icon(vectorResource(Res.drawable.language), "") }
        }
    }
}

@Composable
fun PermissionRow() {

}


@Composable
fun ContinueButton() {

}