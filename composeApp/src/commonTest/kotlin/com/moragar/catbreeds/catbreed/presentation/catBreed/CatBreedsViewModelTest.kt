package com.moragar.catbreeds.catbreed.presentation.catBreed

import com.moragar.catbreeds.catbreed.data.repository.FakeCatRepository
import com.moragar.catbreeds.catbreed.data.usecase.FakeGetCatBreedsUseCase
import com.moragar.catbreeds.catbreed.data.usecase.FakeReadFavoriteBreedsUseCase
import com.moragar.catbreeds.catbreed.data.usecase.FakeToggleFavoriteUseCase
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.model.CatImage
import com.moragar.catbreeds.catbreed.domain.usecase.GetCatBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ReadFavoriteBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ToggleFavoriteUseCase
import com.moragar.catbreeds.core.data.database.FakeCatDatabaseFactoryImpl
import com.moragar.catbreeds.core.data.usecase.FakeIsOnlineUseCase
import com.moragar.catbreeds.core.data.usecase.FakeReadAllCachedBreedsUseCase
import com.moragar.catbreeds.core.data.usecase.FakeSaveCachedBreedUseCase
import com.moragar.catbreeds.core.domain.usecase.IsOnlineUseCase
import com.moragar.catbreeds.core.domain.usecase.ReadAllCachedBreedsUseCase
import com.moragar.catbreeds.core.domain.usecase.SaveCachedBreedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CatBreedsViewModelTest {
    private lateinit var viewModel: CatBreedsViewModel
    private lateinit var getCatBreedsUseCase: GetCatBreedsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var readFavoriteBreedsUseCase: ReadFavoriteBreedsUseCase
    private lateinit var isOnlineUseCase: IsOnlineUseCase
    private lateinit var readAllCachedBreedsUseCase: ReadAllCachedBreedsUseCase
    private lateinit var saveCachedBreedUseCase: SaveCachedBreedUseCase
    private lateinit var fakeDb: FakeCatDatabaseFactoryImpl
    private lateinit var fakeRepository: FakeCatRepository
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDb = FakeCatDatabaseFactoryImpl()
        fakeRepository = FakeCatRepository(fakeDatabase = fakeDb, online = true)
        getCatBreedsUseCase = FakeGetCatBreedsUseCase(fakeRepository)
        toggleFavoriteUseCase = FakeToggleFavoriteUseCase(fakeRepository)
        readFavoriteBreedsUseCase = FakeReadFavoriteBreedsUseCase(fakeRepository)
        isOnlineUseCase = FakeIsOnlineUseCase(fakeRepository)
        readAllCachedBreedsUseCase = FakeReadAllCachedBreedsUseCase(fakeRepository)
        saveCachedBreedUseCase = FakeSaveCachedBreedUseCase(fakeRepository)
        viewModel = CatBreedsViewModel(
            getCatBreedsUseCase =  getCatBreedsUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            readFavoriteBreedsUseCase = readFavoriteBreedsUseCase,
            isOnlineUseCase = isOnlineUseCase,
            readAllCachedBreedsUseCase = readAllCachedBreedsUseCase,
            saveCachedBreedUseCase = saveCachedBreedUseCase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `loadBreeds should update state with breeds on success`() = runTest(testDispatcher) {
        // SetUp
        val breeds = listOf(
            CatBreed(
                id = "abys",
                name = "Abyssinian",
                description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
                origin = "Egypt",
                temperament = "Active, Energetic, Independent, Intelligent, Gentle",
                lifeSpan = "14 - 15",
                image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")
            )
        )
        fakeRepository.setApiBreeds(breeds)

        // Actions
        viewModel.onAction(CatBreedAction.LoadInitialBreeds)
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(breeds, viewModel.state.value.breeds)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loadBreeds should show error on failure`() = runTest(testDispatcher) {
        // SetUp
        fakeRepository.simulateError(Exception("Network error"))

        // Actions
        viewModel.onAction(CatBreedAction.LoadInitialBreeds)
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals("Network error", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.breeds.isEmpty())
    }

    @Test
    fun `toggleFavorite should update breed favorite status`() = runTest(testDispatcher) {
        // SetUp
        val breed =
            CatBreed(
                id = "abys",
                name = "Abyssinian",
                description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
                origin = "Egypt",
                temperament = "Active, Energetic, Independent, Intelligent, Gentle",
                lifeSpan = "14 - 15",
                image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"),
                isFavorite = false
            )
        val initialBreeds = listOf(breed)
        fakeRepository.setApiBreeds(initialBreeds)

        // Actions
        viewModel.onAction(CatBreedAction.LoadInitialBreeds)
        viewModel.onAction(CatBreedAction.ToggleFavorite(breed.id))
        testScheduler.advanceUntilIdle()

        // Assert
        assertTrue(viewModel.state.value.breeds.first().isFavorite)
    }

    @Test
    fun `should show offline warning when network goes offline`() = runTest(testDispatcher) {
        // SetUp
        val cacheBreed =
            CatBreed(
                id = "abys",
                name = "Abyssinian",
                description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
                origin = "Egypt",
                temperament = "Active, Energetic, Independent, Intelligent, Gentle",
                lifeSpan = "14 - 15",
                image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"),
                isFavorite = false
            )
        fakeDb.insertCachedBreed(cacheBreed)
        fakeRepository.setOnline(false)

        // Actions
        viewModel.onAction(CatBreedAction.LoadInitialBreeds)
        testScheduler.advanceUntilIdle()

        // Assert
        assertFalse(viewModel.connectivityState.value.isOnline)
        assertTrue(viewModel.connectivityState.value.showOfflineWarning)
    }

    @Test
    fun `should update connectivity state when network goes offline then online`() = runTest(testDispatcher) {
        // SetUp
        val cacheBreed = CatBreed(
            id = "abys",
            name = "Abyssinian",
            description = "The Abyssinian is easy to care for...",
            origin = "Egypt",
            temperament = "Active, Energetic, Independent, Intelligent, Gentle",
            lifeSpan = "14 - 15",
            image = CatImage("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"),
            isFavorite = false
        )
        fakeDb.insertCachedBreed(cacheBreed)

        // Act: start online
        fakeRepository.setOnline(true)
        viewModel.onAction(CatBreedAction.LoadInitialBreeds)
        testScheduler.advanceUntilIdle()

        // Assert: online
        assertTrue(viewModel.connectivityState.value.isOnline)
        assertFalse(viewModel.connectivityState.value.showOfflineWarning)

        // Act: go offline
        fakeRepository.setOnline(false)
        testScheduler.advanceUntilIdle()

        // Assert: offline
        assertFalse(viewModel.connectivityState.value.isOnline)
        assertTrue(viewModel.connectivityState.value.showOfflineWarning)

        // Act: back online
        fakeRepository.setOnline(true)
        testScheduler.advanceUntilIdle()

        // Assert: online again
        assertTrue(viewModel.connectivityState.value.isOnline)
        assertFalse(viewModel.connectivityState.value.showOfflineWarning)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}