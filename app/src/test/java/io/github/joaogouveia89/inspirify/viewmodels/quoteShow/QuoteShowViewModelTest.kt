package io.github.joaogouveia89.inspirify.viewmodels.quoteShow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.github.joaogouveia89.inspirify.InspirifyViewModelUnitTest
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModelFactory
import io.github.joaogouveia89.inspirify.ui.quoteShow.asFavorite
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteAddToFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteShowViewModelTest : InspirifyViewModelUnitTest() {

    // Mock dependencies
    private val quoteShowUseCase: QuoteShowUseCase = mockk()
    private val quoteAddToFavoriteUseCase: QuoteAddToFavoriteUseCase = mockk()

    // Lateinit ViewModel and ViewModelFactory
    private lateinit var viewModel: QuoteShowViewModel
    private lateinit var viewModelFactory: QuoteShowViewModelFactory

    private val quote: Quote = mockk(relaxed = true)
    private val favorited: Favorite = quote.asFavorite()

    @Before
    fun setup() {
        // Mock the behavior of inject method
        val inspirifyComponent: InspirifyComponent = mockk()
        every { inspirifyComponent.inject(any<QuoteShowViewModel>()) } answers {
            val viewModel: QuoteShowViewModel = firstArg()
            viewModel.quoteShowUseCase = quoteShowUseCase
            viewModel.quoteAddToFavoriteUseCase = quoteAddToFavoriteUseCase
        }

        // Mock the behavior of getDataRequest() function in QuoteShowUseCase
        every { quoteShowUseCase.dataRequest } returns MutableLiveData()
        every { quoteAddToFavoriteUseCase.dataRequest } returns MutableLiveData()

        // Pass the mocked component to your ViewModelFactory
        viewModelFactory = QuoteShowViewModelFactory(inspirifyComponent)
    }

    @After
    fun tearDown() {
        // Clear any coEvery and coVerify
        coEvery { quoteShowUseCase.execute() } coAnswers { }
    }

    @Test
    fun `requestNewData should execute quoteShowUseCase`() = runTest {
        // Given - Nothing to do here
        coEvery { quoteShowUseCase.execute() } just Runs

        // Create your ViewModel using the mocked ViewModelFactory
        viewModel = viewModelFactory.create(QuoteShowViewModel::class.java)

        // When
        viewModel.inputs.requestNewData.postValue(Unit)

        // Then
        coVerify { quoteShowUseCase.execute() }
    }

    @Test
    fun `onFavoriteClick should execute quoteFavoriteUseCase`() = runTest {
        // Create your ViewModel using the mocked ViewModelFactory
        viewModel = viewModelFactory.create(QuoteShowViewModel::class.java)

        // Mock the behavior of currentQuote to return your quote
        val currentQuoteObserver = Observer<Quote> { updatedQuote ->
            assertEquals(quote, updatedQuote)
            // Verify that quoteAddToFavoriteUseCase.execute was called
            coVerify { quoteAddToFavoriteUseCase.execute(quote) }
        }

        // Observe changes on currentQuote
        viewModel.currentQuote.observeForever(currentQuoteObserver)

        // Trigger the onFavoriteClick action
        viewModel.inputs.onFavoriteClick.postValue(Unit)

        // Remove the observer to avoid leaks
        viewModel.currentQuote.removeObserver(currentQuoteObserver)
    }
}