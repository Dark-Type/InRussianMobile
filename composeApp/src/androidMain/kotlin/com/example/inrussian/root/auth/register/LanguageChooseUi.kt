package com.example.inrussian.root.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inrussian.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageChooseUi() {
    Column(Modifier.padding(horizontal = 22.dp)) {
        ContinueButton()
        Text(
            stringResource(Res.string.app_language), fontSize = 40.sp, fontWeight = FontWeight.W600
        )
        Column(Modifier.weight(1f)) {
            Icon(vectorResource(Res.drawable.profile), "", Modifier.fillMaxSize())
            Text(stringResource(Res.tell_about_u), Modifier.fillMaxSize())
        }
        PermissionRow()
        Row {
            Text(
                stringResource(Res.string.app_language),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )
            Spacer(Modifier.weight(1f))
            Text(
                "Русский", fontSize = 16.sp, fontWeight = FontWeight.W400
            )
            IconButton({}) { Icon(vectorResource(Res.drawable.language), "") }
        }
    }
}

@Composable
fun PermissionRow() {

}


@Composable
fun ContinueButton() {
    Row() {
        Text(stringResource(Res.string.continue))
        IconButton({}) {
            Icon(vectorResource(Res.drawable.front_arror), "")
        }
    }
}