package io.github.joaogouveia89.inspirify.quoteShow

import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.InspirifyUnitTest
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModel
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteShowViewModelFactory
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteFavoriteUseCase
import io.github.joaogouveia89.inspirify.ui.quoteShow.useCases.QuoteShowUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteShowViewModelTest : InspirifyUnitTest(){

    // Mock dependencies
    private val quoteShowUseCase: QuoteShowUseCase = mockk()
    private val quoteFavoriteUseCase: QuoteFavoriteUseCase = mockk()

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

        // Pass the mocked component to your ViewModelFactory
        viewModelFactory = QuoteShowViewModelFactory(inspirifyComponent)
    }

    @After
    fun tearDown() {
        // Clear any coEvery and coVerify
        coEvery { quoteShowUseCase.execute() } coAnswers { }
    }

    @Test
    fun `fetchRandomQuote should execute quoteShowUseCase`() = runTest {
        // Given - Nothing to do here
        coEvery { quoteShowUseCase.execute() } just Runs

        // Create your ViewModel using the mocked ViewModelFactory
        viewModel = viewModelFactory.create(QuoteShowViewModel::class.java)

        // When
        viewModel.fetchRandomQuote()

        // Then
        coVerify { quoteShowUseCase.execute() }
    }
}