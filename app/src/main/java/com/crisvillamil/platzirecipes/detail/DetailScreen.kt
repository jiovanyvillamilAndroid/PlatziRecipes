package com.crisvillamil.platzirecipes.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.crisvillamil.platzirecipes.AuthorLabel
import com.crisvillamil.platzirecipes.ItemImage
import com.crisvillamil.platzirecipes.fromHex
import com.crisvillamil.platzirecipes.ui.theme.star_color

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailUIState: DetailUIState,
    navController: NavController,
    onDetailEvent: (DetailEvent) -> Unit,
) {
    val recipeItemState = detailUIState.recipeDetailState
    LaunchedEffect(key1 = true, block = {
        onDetailEvent(DetailEvent.OnFetchRecipeDetail)
    })
    Surface(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        if (detailUIState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        } else {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Header(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    imageUrl = recipeItemState.imageUrl.orEmpty(),
                    rating = recipeItemState.rate.toString(),
                    isFavorite = recipeItemState.isFavorite,
                    name = recipeItemState.name.orEmpty(),
                    authorImageUrl = recipeItemState.authorImageUrl.orEmpty(),
                    authorName = recipeItemState.authorName.orEmpty(),
                    onFavoriteClick = { onDetailEvent(DetailEvent.OnFavoriteClicked) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "Dificultad"
                    )
                    recipeItemState.difficulty?.let {
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            text = it.text
                        )
                    }
                    recipeItemState.cookingTime?.let {
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.fromHex("F3F4F6"))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.fromHex("9CA3AF"),
                            text = it
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = recipeItemState.description.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipeItemState.ingredients?.let {
                    Text(
                        text = "Ingredientes: ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pasos: ",
                    style = MaterialTheme.typography.titleMedium
                )
                recipeItemState.cookingSteps.forEachIndexed { index, step ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.fromHex("F3F4F6"))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.fromHex("9CA3AF"),
                            text = "${index + 1}"
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.fromHex("F3F4F6"))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.fromHex("9CA3AF"),
                            text = step
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Califica la receta",
                    style = MaterialTheme.typography.titleMedium
                )
                RatingBar(
                    rating = recipeItemState.ratingBarValue,
                    onRatingChanged = {
                        onDetailEvent(DetailEvent.OnRateSelected(it))
                    })
            }
        }

    }

}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    imageUrl: String,
    rating: String,
    isFavorite: Boolean,
    name: String,
    authorImageUrl: String,
    authorName: String,
    onFavoriteClick: () -> Unit,
) {
    ItemImage(
        modifier = modifier,
        imageUrl = imageUrl,
        rating = rating,
        isFavorite = isFavorite,
        onFavoriteClick = onFavoriteClick
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge,
    )
    AuthorLabel(
        authorImageUrl = authorImageUrl,
        authorName = authorName
    )
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
                    tint = star_color,
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
    DetailScreen(detailUIState = DetailUIState(), onDetailEvent = {}, navController = NavController(LocalContext.current))
}