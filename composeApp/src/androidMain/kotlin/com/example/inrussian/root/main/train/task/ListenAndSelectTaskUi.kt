package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent.Variant
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent.VariantState
import com.example.inrussian.models.models.task.support.AudioBlocks
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import org.jetbrains.compose.resources.vectorResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ListenAndSelectTaskUi(
    component: ListenAndSelectComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    LazyColumn {
        items(state.audioBlocks) {
            SpeakerElement(it)
            Spacer(Modifier.height(24.dp))
        }
        item {
            ChoiceElement(state.variants, {})
        }
    }
    onContinueClick {
        component.onContinueClick()
    }
}

@Composable
fun SpeakerElement(audioBlock: AudioBlocks) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isInPreview = LocalInspectionMode.current

    val player: ExoPlayer? = remember {
        if (isInPreview) {
            null
        } else {
            ExoPlayer.Builder(context).build().apply {
                playWhenReady = false
            }
        }
    }

    val isPlayingState = remember { mutableStateOf(player?.isPlaying == true) }

    DisposableEffect(player, lifecycleOwner) {
        if (player == null) {
            onDispose { }
        } else {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    isPlayingState.value = isPlaying
                }
            }
            player.addListener(listener)

            val observer = object : DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    player.playWhenReady = false
                }

                override fun onStop(owner: LifecycleOwner) {
                    player.pause()
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    player.release()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
                player.removeListener(listener)
                player.release()
            }
        }
    }

    LaunchedEffect(audioBlock.audio, player) {
        if (player == null) return@LaunchedEffect

        if (audioBlock.audio.isNotBlank()) {
            try {
                val mediaItem = MediaItem.fromUri(audioBlock.audio)
                player.setMediaItem(mediaItem)
                player.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                player.stop()
                player.clearMediaItems()
                isPlayingState.value = false
            } catch (_: Exception) {
            }
        }
    }

    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SpeakerItem(
            isPlay = !(isPlayingState.value),
            text = audioBlock.description,
            transcription = audioBlock.descriptionTranslate,
            label = audioBlock.name
        ) {
            if (player == null) return@SpeakerItem
            try {
                if (player.isPlaying) player.pause() else player.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ChoiceElement(variants: List<Variant>, onSelect: (Uuid) -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(10.dp)
    ) {
        for (i in 0 until variants.size step 2) {
            Row {
                ChoiceItem(
                    variants[i].state,
                    { onSelect(variants[i].id) },
                    variants[i].text,
                    Modifier.weight(1f)
                )

                if (i + 1 < variants.size) {
                    Spacer(Modifier.width(15.dp))
                    ChoiceItem(
                        variants[i + 1].state,
                        { onSelect(variants[i + 1].id) },
                        variants[i + 1].text,
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ChoiceItem(
    state: VariantState,
    onSelect: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    var modifier = modifier
    if (state is VariantState.NotSelected) modifier =
        modifier.border(1.dp, DarkGrey.copy(0.3f), RoundedCornerShape(12.dp))
    Button(
        onSelect, modifier, colors = when (state) {
            VariantState.Selected -> ButtonDefaults.textButtonColors().copy(containerColor = Orange)

            VariantState.Correct -> ButtonDefaults.textButtonColors().copy(containerColor = Green)

            VariantState.Incorrect -> ButtonDefaults.textButtonColors()
            VariantState.NotSelected -> ButtonDefaults.textButtonColors()
        }, shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text,
            color = if (state is VariantState.Correct || state is VariantState.Selected) White else DarkGrey.copy(
                0.7f
            )
        )
    }
}


@Composable
fun SpeakerItem(
    isPlay: Boolean, text: String?, transcription: String?, label: String, onClick: () -> Unit
) {
    Text(/*stringResource(Res.string.speaker) + */label,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(Modifier.height(12.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick, Modifier.size(50.dp)) {
            Icon(
                vectorResource(if (isPlay) Res.drawable.play_button else Res.drawable.pause),
                "",
                tint = DarkGrey.copy(0.8f)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column {
            text?.let {
                Text(
                    it, fontSize = 16.sp, color = DarkGrey.copy(0.9f), fontWeight = FontWeight.W500
                )
            }
            Spacer(Modifier.height(4.dp))
            transcription?.let {
                Text(
                    it, fontSize = 16.sp, color = DarkGrey.copy(0.5f), fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
class ListenAndSelectTaskUi : ListenAndSelectComponent {

    override val state = MutableValue(
        ListenAndSelectComponent.State(
            audioBlocks = listOf(
                AudioBlocks(
                    name = "Спикер 1",
                    audio = "",
                ), AudioBlocks(
                    name = "Спикер 3", audio = "", description = "Вася ,ты когда читать научишься"
                ), AudioBlocks(
                    name = "Спикер 2",
                    audio = "",
                    description = "Я подумаю над твоим предложением",
                    descriptionTranslate = "Go fuck "
                )
            ),
            variants = listOf(
                Variant(text = "Home", state = VariantState.Selected),
                Variant(text = "Street", state = VariantState.Correct),
                Variant(text = "House"),
                Variant(text = "Bitch"),
                Variant(text = "Bird"),
                Variant(text = "Fly"),
                Variant(text = "Electronic"),
            ),
        )
    )

    override fun onSelect(variantId: Uuid) {
        TODO("Not yet implemented")
    }

    override fun onContinueClick() {
        TODO("Not yet implemented")
    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        ListenAndSelectTaskUi(this) {

        }
    }
}