package io.github.joaogouveia89.inspirify.quoteShow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModelFactory
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteShowViewModelTest {

    // Rule to force execution of tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Replace Dispatchers.Main with a test dispatcher
    private val testDispatcher = TestCoroutineDispatcher()

    // Mock dependencies
    private val quoteShowUseCase: QuoteShowUseCase = mockk()
    private val quoteFavoriteUseCase: QuoteFavoriteUseCase = mockk()

    // Mock your InspirifyComponent
    val inspirifyComponent: InspirifyComponent = mockk()

    // Lateinit ViewModel and ViewModelFactory
    private lateinit var viewModel: QuoteShowViewModel
    private lateinit var viewModelFactory: QuoteShowViewModelFactory

    @Before
    fun setup() {
        // Mock the behavior of inject method
        every { inspirifyComponent.inject(any()) } answers {
            val viewModel: QuoteShowViewModel = firstArg()
            viewModel.quoteShowUseCase = quoteShowUseCase
            viewModel.quoteFavoriteUseCase = quoteFavoriteUseCase
        }

        // Mock the behavior of getDataRequest() function in QuoteShowUseCase
        every { quoteShowUseCase.dataRequest } returns MutableLiveData<DataRequest>()
        every { quoteFavoriteUseCase.dataRequest } returns MutableLiveData<DataRequest>()

        // Set the test dispatcher as the main dispatcher
        Dispatchers.setMain(testDispatcher)

        // Pass the mocked component to your ViewModelFactory
        viewModelFactory = QuoteShowViewModelFactory(inspirifyComponent)

        // Create your ViewModel using the mocked ViewModelFactory
        viewModel = viewModelFactory.create(QuoteShowViewModel::class.java)
    }

    @After
    fun tearDown() {
        // Reset main dispatcher after the test
        Dispatchers.resetMain()

        // Clear test coroutine dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchRandomQuote should execute quoteShowUseCase`() {
        // Given
        coEvery { quoteShowUseCase.execute() }

        // When
        viewModel.fetchRandomQuote()

        // Then
        coVerify { quoteShowUseCase.execute() }
    }
}