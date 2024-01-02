package com.crisvillamil.platzirecipes.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailUIState: DetailUIState,
    onDetailEvent: (DetailEvent) -> Unit,
) {
    LaunchedEffect(key1 = true, block = {
        onDetailEvent(DetailEvent.OnFetchRecipeDetail)
    })
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        if (detailUIState.isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = detailUIState.recipeDetailState.name.orEmpty()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    AsyncImage(
                        modifier = Modifier.height(300.dp),
                        placeholder = painterResource(id = android.R.drawable.ic_menu_camera),
                        model = detailUIState.recipeDetailState.imageUrl,
                        contentDescription = null
                    )
                    val imageVector =
                        if (detailUIState.recipeDetailState.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(35.dp)
                            .clickable {
                                onDetailEvent(DetailEvent.OnFavoriteClicked)
                            },
                        imageVector = imageVector,
                        contentDescription = null,
                        tint = Color.Red,
                    )
                }
                Row(Modifier.align(Alignment.CenterHorizontally)) {
                    Text(modifier = Modifier.weight(0.5f), text = "Dificultad")
                    detailUIState.recipeDetailState.difficulty?.let {
                        Text(modifier = Modifier.weight(0.5f), text = it.text)
                    }
                    detailUIState.recipeDetailState.cookingTime?.let {
                        Text(modifier = Modifier.weight(1f), text = it)
                    }
                }
                Row {
                    Text(text = "Calificación: ${detailUIState.recipeDetailState.rate ?: 0.0}")
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow
                    )
                }
                detailUIState.recipeDetailState.ingredients?.let {
                    Text(text = "Ingredientes: ")
                    Text(text = detailUIState.recipeDetailState.ingredients)
                }
                Text(text = "Pasos: ")
                detailUIState.recipeDetailState.cookingSteps.forEachIndexed { index, step ->
                    Column {
                        Text(
                            text = "Paso ${(index + 1)}:"
                        )
                        Text(
                            text = step
                        )
                    }
                }
                Text(text = "Califica la receta")
                RatingBar(
                    rating = detailUIState.recipeDetailState.ratingBarValue,
                    onRatingChanged = {
                        onDetailEvent(DetailEvent.OnRateSelected(it))
                    })
            }
        }

    }

}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row {
        for (i in 1..5) {
            if (i <= rating) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Filled Star",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .width(32.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onRatingChanged(i)
                            })
                )
            } else {
                Icon(
                    Icons.Sharp.Star,
                    contentDescription = "Empty Star",
                    tint = Color.Gray,
                    modifier = Modifier
                        .width(32.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onRatingChanged(i)
                            })
                )
            }
            if (i < 5) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}


@Preview
@Composable
private fun PreviewDetailScreen() {
    DetailScreen(detailUIState = DetailUIState(), onDetailEvent = {})
}