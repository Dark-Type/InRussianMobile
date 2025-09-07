package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.tasks.interfaces.ImageAndSelectComponent
import com.example.inrussian.data.client.models.Variant
import com.example.inrussian.data.client.models.VariantState
import com.example.inrussian.models.models.task.body.ImageBlock
import com.example.inrussian.ui.theme.FontInactiveLight
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.placeholder_coil
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ImageAndSelectTaskUi(
    component: ImageAndSelectComponent, onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    LazyColumn {
        items(state.imageBlocks) {
            ImageBlock(it)
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
fun ImageBlock(
    imageBlock: ImageBlock
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            imageBlock.name,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 28.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        AsyncImage(
            model = imageBlock.image,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(12.dp)),
            placeholder = painterResource(Res.drawable.placeholder_coil),
            error = painterResource(Res.drawable.placeholder_coil)
        )
        imageBlock.description?.let {
            Text(
                it,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 6.dp, bottom = 12.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        imageBlock.descriptionTranslate?.let {
            Text(
                it,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 4.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = FontInactiveLight
            )
        }
        Spacer(Modifier.height(28.dp))
    }
}


@OptIn(ExperimentalUuidApi::class)
class ImageAndSelectTaskUi : ImageAndSelectComponent {
    
    override val state = MutableValue(
        ImageAndSelectComponent.State(
            imageBlocks = listOf(
                ImageBlock(
                    name = "Спикер 1",
                    image = "",
                ), ImageBlock(
                    name = "Спикер 3", image = "", description = "Вася ,ты когда читать научишься"
                ), ImageBlock(
                    name = "Спикер 2",
                    image = "",
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
        ImageAndSelectTaskUi(this) {
        
        }
    }
}