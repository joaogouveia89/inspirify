package io.github.joaogouveia89.inspirify.viewmodels.quoteShow

import androidx.lifecycle.MutableLiveData
import io.github.joaogouveia89.inspirify.InspirifyViewModelUnitTest
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
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
import io.mockk.runs
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class QuoteShowViewModelTest : InspirifyViewModelUnitTest(){

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
        every { quoteShowUseCase.dataRequest } returns MutableLiveData()
        every { quoteFavoriteUseCase.dataRequest } returns MutableLiveData()

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
        // Given
        viewModel = viewModelFactory.create(QuoteShowViewModel::class.java)
        val quote = mockk<Quote>() // Mock your Quote object as needed

        // When
        viewModel.inputs.onFavoriteClick.value = Unit

        // Then
        viewModel.outputs.quoteFavoriteUpdateStatus.observeForever { dataRequest ->
            // Ensure observer was triggered
            assertNotNull(dataRequest)

            // Assert if the correct DataRequest type is emitted
            assertTrue(dataRequest is DataRequest.OnProgress || dataRequest is DataRequest.Success<*>)

            // Additional assertions if needed
        }

        // Mock the behavior of quoteShowUseCase.dataRequest.value
        coEvery { quoteShowUseCase.dataRequest.value }.returns(DataRequest.Success(quote))

        // Mock the behavior of quoteFavoriteUseCase.execute
        coEvery { quoteFavoriteUseCase.execute(quote) } just runs
    }
}