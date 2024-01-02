package com.crisvillamil.platzirecipes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.crisvillamil.platzirecipes.ui.theme.light_gray_bg
import com.crisvillamil.platzirecipes.ui.theme.light_gray_color

@Composable
fun AuthorLabel(modifier: Modifier = Modifier, authorName: String, authorImageUrl: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            model = authorImageUrl,
            contentDescription = "author image"
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Por $authorName",
            color = light_gray_color
        )
    }
}

@Composable
fun ItemImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    rating: String,
    isFavorite:Boolean,
    onFavoriteClick: () -> Unit,
) {
    val favoriteColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        label = "favoriteColorAnimation"
    )
    Box(modifier = modifier) {
        AsyncImage(
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .shadow(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            model = imageUrl,
            contentDescription = null
        )
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp)
                .clickable {
                    onFavoriteClick()
                },
            painter = painterResource(id = R.drawable.bookmark),
            contentDescription = "Favorite button",
            tint = favoriteColor
        )
        Rating(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            rating = rating
        )
    }
}

@Composable
private fun Rating(modifier: Modifier, rating: String) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                light_gray_bg.copy(alpha = 0.3f)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(16.dp),
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.White
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 4.dp),
            text = rating,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
}
