package com.example.inrussian.root.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent
import com.example.inrussian.ui.theme.BackButton
import com.example.inrussian.ui.theme.ContinueButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.reallyLightGrey
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.citizenship
import inrussian.composeapp.generated.resources.citizenship_data
import inrussian.composeapp.generated.resources.delete
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.eath
import inrussian.composeapp.generated.resources.nationality
import inrussian.composeapp.generated.resources.residence_city
import inrussian.composeapp.generated.resources.residence_country
import inrussian.composeapp.generated.resources.study_country
import inrussian.composeapp.generated.resources.time_of_stay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenshipDate(component: CitizenshipComponent) {
    var state = component.state
    Column(
        Modifier
            .background(reallyLightGrey)
            .padding(horizontal = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(true, component::onBack)
            ContinueButton(state.continueEnable, component::onNext)
        }
        Text(
            stringResource(Res.string.citizenship_data),
            fontSize = 40.sp,
            fontWeight = FontWeight.W600
        )
        Box(
            Modifier
                .weight(0.8f)
                .fillMaxWidth(),

            ) {
            Icon(
                vectorResource(Res.drawable.eath),
                "",
                Modifier
                    .fillMaxWidth(0.5f)
                    .padding(bottom = 22.dp)
                    .align(Alignment.Center),
                tint = Orange
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White)
        ) {
            ClipsContainer(
                isOpen = state.expanded,
                variants = listOf("as", "asdasf", "ASfasfasf"),
                active = state.citizenship,
                onClick = component::deleteCountry,
                onChangeExpanded = component::onChangeExpanded,
                onAddClick = component::selectCountry,
                placeholder = stringResource(Res.string.citizenship)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            ClipsContainer(
                isOpen = state.expanded,
                variants = listOf("as", "asdasf", "ASfasfasf"),
                active = if (state.nationality == "") listOf() else listOf(state.nationality),
                onClick = component::deleteCountry,
                onChangeExpanded = component::onChangeExpanded,
                onAddClick = component::selectCountry,
                placeholder = stringResource(Res.string.nationality)
            )
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.residence_country))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.residence_city))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField("", {}, stringResource(Res.string.study_country))
            HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            InputFormField(
                "",
                {},
                stringResource(Res.string.time_of_stay),
                Modifier,
                vectorResource(Res.drawable.down_arrow),
                {})
        }
    }
}

@Composable
fun CommonClips(onClick: (String) -> Unit, text: String) {
    Box(Modifier.padding(end = 16.dp, bottom = 4.dp)) {
        Text(
            text, modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(DarkGrey.copy(0.1f))
                .padding(horizontal = 9.dp, vertical = 3.dp)
                .align(Alignment.Center)
        )
        IconButton(
            { onClick(text) },
            Modifier
                .align(Alignment.TopEnd)
                .size(14.dp)
                .offset(7.dp, (-7).dp)
        ) {
            Icon(vectorResource(Res.drawable.delete), "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipsContainer(
    isOpen: Boolean,
    variants: List<String>,
    active: List<String>,
    onClick: (String) -> Unit,
    onChangeExpanded: (Boolean) -> Unit,
    onAddClick: (String) -> Unit,
    placeholder: String
) {
    Box(Modifier.fillMaxWidth()) {
        FlowRow(Modifier.padding(start = 16.dp, end = 12.dp, top = 12.dp, bottom = 8.dp)) {
            active.forEach { text ->
                CommonClips(onClick, text)
            }
            if (active.isEmpty())
                Text(placeholder)
        }

        IconButton(
            onClick = { onChangeExpanded(!isOpen) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) { Icon(vectorResource(Res.drawable.down_arrow), contentDescription = null) }
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = White
        ) {
            variants.forEach { country ->
                DropdownMenuItem(
                    text = { Text(country) },
                    onClick = {
                        if (!active.contains(country)) {
                            onAddClick(country)
                        }
                        onChangeExpanded(false)
                    }
                )
            }
        }
    }
}

class CitizenshipDate : CitizenshipComponent {
    override val state = CitizenshipComponent.State(expanded = false)

    override fun onNext() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun selectCountry(country: String) {
        TODO("Not yet implemented")
    }

    override fun onChangeExpanded(expanded: Boolean) {
        TODO("Not yet implemented")
    }

    override fun selectNationality(string: String) {
        TODO("Not yet implemented")
    }

    override fun deleteCountry(country: String) {
        TODO("Not yet implemented")
    }

    override fun selectTime(time: String) {
        TODO("Not yet implemented")
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        CitizenshipDate(this)
    }
}