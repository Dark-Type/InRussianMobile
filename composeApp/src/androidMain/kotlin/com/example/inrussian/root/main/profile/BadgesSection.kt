package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.main.profile.Badge
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.medal
import inrussian.composeapp.generated.resources.there_are_not_achievements
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BadgesSection(badges: List<Badge>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (badges.isEmpty()) {
            Text(
                stringResource(Res.string.there_are_not_achievements),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(badges) { badge ->
                    AchievementItem(badge)
                }
            }
        }
    }
}


@Composable
fun AchievementItem(badge: Badge) {
    Column(
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Icon(vectorResource(Res.drawable.medal), "", tint = Orange)
        Text(
            badge.name,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            color = DarkGrey.copy(alpha = 0.8f),
            fontSize = 10.sp
        )
        badge.description?.let {
            Text(
                it, maxLines = 1,
                color = DarkGrey,
                fontSize = 10.sp
            )
        }
    }
}