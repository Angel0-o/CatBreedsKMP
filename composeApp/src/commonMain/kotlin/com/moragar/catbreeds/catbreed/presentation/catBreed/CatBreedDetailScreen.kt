package com.moragar.catbreeds.catbreed.presentation.catBreed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catbreeds.composeapp.generated.resources.Res
import catbreeds.composeapp.generated.resources.back_icon
import catbreeds.composeapp.generated.resources.detail_icon
import catbreeds.composeapp.generated.resources.ic_breed_error
import catbreeds.composeapp.generated.resources.ic_cat_arrow
import catbreeds.composeapp.generated.resources.ic_heart_empty
import catbreeds.composeapp.generated.resources.ic_heart_full
import catbreeds.composeapp.generated.resources.life_span_detail
import catbreeds.composeapp.generated.resources.origin_detail
import catbreeds.composeapp.generated.resources.temperament_detail
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.model.CatImage
import com.moragar.catbreeds.core.presentation.styles.DarkBlue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CatBreedDetailScreen(
    breed: CatBreed,
    onAction: (CatBreedAction) -> Unit,
    onBackClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = breed.image?.url,
        error = painterResource(Res.drawable.ic_breed_error),
        placeholder = painterResource(Res.drawable.ic_breed_error)
    )
    val state = painter.state.collectAsState()
    val isFavorite = remember { mutableStateOf(breed.isFavorite) }
    LaunchedEffect(isFavorite.value) {
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val imageSize = when {
            maxWidth < 600.dp -> 220.dp
            else -> 500.dp
        }
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painter = painterResource(resource = Res.drawable.ic_cat_arrow),
                    contentDescription = stringResource(Res.string.back_icon),
                    modifier = Modifier
                        .height(90.dp)
                        .width(70.dp)
                        .clickable { onBackClick() }
                )
                Image(
                    painter = painterResource(resource = if (isFavorite.value) Res.drawable.ic_heart_full else Res.drawable.ic_heart_empty),
                    contentDescription = stringResource(Res.string.detail_icon),
                    modifier = Modifier
                        .height(90.dp)
                        .width(70.dp)
                        .clickable {
                            isFavorite.value = !isFavorite.value
                            onAction(CatBreedAction.ToggleFavorite(breed.id))
                        }
                )
            }
            if (state.value is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = painter,
                    contentDescription = breed.name,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(imageSize)
                        .fillMaxWidth()
                )
            }
            Text(
                text = breed.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(Modifier, 1.dp, DarkBlue)
            Text(
                text = breed.description,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(Modifier, 1.dp, DarkBlue)
            Text(text = stringResource(Res.string.temperament_detail, breed.temperament))
            HorizontalDivider(Modifier, 1.dp, DarkBlue)
            Text(text = stringResource(Res.string.origin_detail, breed.origin))
            HorizontalDivider(Modifier, 1.dp, DarkBlue)
            Text(text = stringResource(Res.string.life_span_detail, breed.lifeSpan))
            HorizontalDivider(Modifier, 1.dp, DarkBlue)
        }
    }
}

@Preview
@Composable
fun CatBreedDetailScreenPreview() {
    val breed = CatBreed(
        id = "abys",
        name = "Abyssinian",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        origin = "Egypt",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        lifeSpan = "14 - 15",
        image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")
    )
    MaterialTheme {
        CatBreedDetailScreen(
            breed = breed,
            onAction = {},
            onBackClick = {}
        )
    }
}