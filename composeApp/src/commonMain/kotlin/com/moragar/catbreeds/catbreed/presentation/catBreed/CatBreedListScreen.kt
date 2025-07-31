package com.moragar.catbreeds.catbreed.presentation.catBreed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catbreeds.composeapp.generated.resources.Res
import catbreeds.composeapp.generated.resources.breed_list
import catbreeds.composeapp.generated.resources.cat_breeds
import catbreeds.composeapp.generated.resources.error_icon
import catbreeds.composeapp.generated.resources.error_message
import catbreeds.composeapp.generated.resources.favorites
import catbreeds.composeapp.generated.resources.ic_breed_error
import catbreeds.composeapp.generated.resources.no_favorite_books
import catbreeds.composeapp.generated.resources.retry
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.model.CatImage
import com.moragar.catbreeds.core.presentation.styles.Black
import com.moragar.catbreeds.core.presentation.styles.DesertWhite
import com.moragar.catbreeds.core.presentation.styles.SandYellow
import com.moragar.catbreeds.core.presentation.uicomponents.OfflineBanner
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatBreedListScreen(
    uiState: CatBreedsState,
    networkState: ConnectivityState,
    onAction: (CatBreedAction) -> Unit = {},
    navigateToDetail: (CatBreed) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedTabIndex,
        pageCount = { 2 }
    )
    val breedListState: LazyGridState = rememberLazyGridState()
    val favoriteBreedListState = rememberLazyGridState()
    val showOfflineBanner = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onAction(CatBreedAction.LoadInitialBreeds)
    }

    LaunchedEffect(uiState.breeds) {
        if (uiState.endReached || uiState.isLoading) return@LaunchedEffect
        snapshotFlow {
            breedListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if(lastVisibleIndex == uiState.breeds.lastIndex) {
                    onAction(CatBreedAction.LoadNextPage)
                }
            }
    }

    LaunchedEffect(networkState.isOnline) {
        if (networkState.showOfflineWarning) {
            showOfflineBanner.value = true
        }
    }

    LaunchedEffect(uiState.selectedTabIndex){
        if (pagerState.currentPage != uiState.selectedTabIndex) {
            pagerState.animateScrollToPage(uiState.selectedTabIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.selectedTabIndex) {
            onAction(CatBreedAction.OnTabSelected(pagerState.currentPage))
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.cat_breeds),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
            color = Black,
        )
        TabRow(
            selectedTabIndex = uiState.selectedTabIndex,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            containerColor = DesertWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    color = SandYellow,
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[uiState.selectedTabIndex])
                )
            }
        ) {
            Tab(
                selected = uiState.selectedTabIndex == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                modifier = Modifier.weight(1f),
                selectedContentColor = SandYellow,
                unselectedContentColor = Color.Black.copy(alpha = 0.5f)
            ) {
                Text(
                    text = stringResource(Res.string.breed_list),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
            Tab(
                selected = uiState.selectedTabIndex == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                modifier = Modifier.weight(1f),
                selectedContentColor = SandYellow,
                unselectedContentColor = Color.Black.copy(alpha = 0.5f)
            ) {
                Text(
                    text = stringResource(Res.string.favorites),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
        }
        OfflineBanner(showWarning = networkState.showOfflineWarning,)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                val columns = when {
                    maxWidth < 600.dp -> 2
                    else -> 5
                }
                when(page) {
                    0 -> {
                        when {
                            uiState.isLoading -> {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                            uiState.error != null -> {
                                CatErrorSection(
                                    errorMessage = uiState.error,
                                    onAction = onAction
                                )
                            }
                            else -> {
                                CatBreedSection(
                                    breedListState = breedListState,
                                    breedList = uiState.breeds,
                                    isRefreshing = uiState.isRefreshing,
                                    navigateToDetail = navigateToDetail,
                                    columns = columns
                                )
                            }
                        }
                    }
                    1 -> {
                        if(uiState.favorites.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.no_favorite_books),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        } else {
                            CatBreedSection(
                                breedListState = favoriteBreedListState,
                                breedList = uiState.favorites,
                                isRefreshing = uiState.isRefreshing,
                                navigateToDetail = navigateToDetail,
                                columns = columns
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatBreedSection(
    breedListState: LazyGridState,
    breedList: List<CatBreed>,
    isRefreshing: Boolean,
    columns: Int,
    navigateToDetail: (CatBreed) -> Unit
) {
    LazyVerticalGrid(
        state = breedListState,
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(breedList) { breed ->
            CatBreedItem(
                breed = breed,
                navigateToDetail = navigateToDetail
            )
        }
    }
    if(isRefreshing) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CatErrorSection(
    errorMessage: String,
    onAction: (CatBreedAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(resource = Res.drawable.ic_breed_error),
            contentDescription = stringResource(Res.string.error_icon),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.error_message, errorMessage),
            color = Color.Red
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                onAction(CatBreedAction.LoadInitialBreeds)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(stringResource(Res.string.retry))
        }
    }
}

@Composable
fun CatBreedItem(
    breed: CatBreed,
    navigateToDetail: (CatBreed) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = breed.image?.url,
        error = painterResource(Res.drawable.ic_breed_error),
        placeholder = painterResource(Res.drawable.ic_breed_error)
    )
    val state = painter.state.collectAsState()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                navigateToDetail.invoke(breed)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.value is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = painter,
                    contentDescription = breed.name,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                )
            }
            Text(
                text = breed.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = breed.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun CatBreedSectionPreview() {
    val breed1 = CatBreed(
        id = "abys",
        name = "Abyssinian",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        origin = "Egypt",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        lifeSpan = "14 - 15",
        image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")
    )
    val breed2 = CatBreed(
        id = "aege",
        name = "Aegean",
        description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
        origin = "Egypt",
        temperament = "Active, Energetic, Independent, Intelligent, Gentle",
        lifeSpan = "14 - 15",
        image = CatImage("https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg")
    )
    val breedList: MutableList<CatBreed> = mutableListOf()
    breedList.add(breed1)
    breedList.add(breed2)
    breedList.add(breed1)
    breedList.add(breed2)
    breedList.add(breed1)
    breedList.add(breed2)
    breedList.add(breed1)
    breedList.add(breed2)
    val uiState = CatBreedsState(
        breeds = breedList,
        isLoading = false,
        isRefreshing = false,
        endReached = false,
        error = null,
        currentPage = 1
    )
    MaterialTheme {
        CatBreedSection(
            breedListState = rememberLazyGridState(),
            breedList = uiState.breeds,
            isRefreshing = uiState.isRefreshing,
            columns = 2,
            navigateToDetail = {},
        )
    }
}

@Preview
@Composable
fun CatErrorSectionPreview() {
    CatErrorSection(
        errorMessage = "Error loading breeds",
        onAction = {}
    )
}