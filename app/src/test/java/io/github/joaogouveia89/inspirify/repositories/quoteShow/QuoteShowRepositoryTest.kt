package io.github.joaogouveia89.inspirify.repositories.quoteShow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.github.joaogouveia89.inspirify.R
import io.github.joaogouveia89.inspirify.data.DataRequest
import io.github.joaogouveia89.inspirify.data.api.QuoteApi
import io.github.joaogouveia89.inspirify.data.api.asQuote
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import io.github.joaogouveia89.inspirify.data.local.daos.FavoriteDao
import io.github.joaogouveia89.inspirify.data.local.entities.Favorite
import io.github.joaogouveia89.inspirify.ui.quoteShow.Quote
import io.github.joaogouveia89.inspirify.ui.quoteShow.QuoteRepository
import io.github.joaogouveia89.inspirify.ui.quoteShow.asFavorite
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class QuoteRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val api: RetrofitZenQuotes = mockk()
    private val quote = QuoteApi(quote = "quote", author = "author")
    private val apiRandomQuoteResponseMock: Response<List<QuoteApi>> = mockk<Response<List<QuoteApi>>>()

    private val localDb: LocalDb = mockk()
    private val favoriteDaoMock: FavoriteDao = mockk()

    private val repository = QuoteRepository(api, localDb)

    private val favoriteQuote = quote
        .asQuote(isFavorite = true)
        .asFavorite()

    private val errorMessage = "Some error message"

    @Test
    fun `should be set as favorite if the fetched quote is stored locally as favorite`() = runTest {
// Given
        coEvery { api.fetchRandomQuote() } returns apiRandomQuoteResponseMock
        every { apiRandomQuoteResponseMock.isSuccessful } returns true
        every { apiRandomQuoteResponseMock.body() } returns listOf(quote)
        every { localDb.favoriteDao() } returns favoriteDaoMock
        coEvery { favoriteDaoMock.getQuoteLocally(any<String>(), any<String>()) } returns listOf(favoriteQuote) // Assuming quote is in favorites

        // When
        repository.fetchRandomQuote()

        // Then
        val dataRequest = repository.dataRequest.value
        assertTrue(dataRequest is DataRequest.Success<*>) // Check if it's a success state

        val successData = (dataRequest as DataRequest.Success<*>).data as Quote
        assertEquals(successData.favoriteIconRes, R.drawable.ic_like_fill) // Check if the quote is marked as favorite
    }

    @Test
    fun `fetchRandomQuote should update LiveData with success state when API call is successful`() = runTest {

        // Given
        coEvery { api.fetchRandomQuote() } returns apiRandomQuoteResponseMock
        every { apiRandomQuoteResponseMock.isSuccessful } returns true
        every { apiRandomQuoteResponseMock.body() } returns listOf(quote)
        every { localDb.favoriteDao() } returns favoriteDaoMock
        coEvery { favoriteDaoMock.getQuoteLocally(any<String>(), any<String>()) } returns emptyList() // Assuming quote is not in favorites

        val observer: Observer<DataRequest> = mockk(relaxed = true)
        repository.dataRequest.observeForever(observer)

        // When
        repository.fetchRandomQuote()
        // Then
        coVerify(exactly = 1) { api.fetchRandomQuote() } // Verify that the API was called

        val dataRequest = repository.dataRequest.value
        assertTrue(dataRequest is DataRequest.Success<*>) // Check if it's a success state

        val successData = (dataRequest as DataRequest.Success<*>).data
        assertEquals(quote.asQuote(isFavorite = false), successData) // Check the data
    }

    @Test
    fun `fetchRandomQuote should update LiveData with error state when API call fails`() = runTest {
        // Given
        coEvery { api.fetchRandomQuote() } throws Exception(errorMessage)
        val observer: Observer<DataRequest> = mockk(relaxed = true)
        repository.dataRequest.observeForever(observer)

        // When
        repository.fetchRandomQuote()

        // Then
        val dataRequest = repository.dataRequest.value
        assertTrue(dataRequest is DataRequest.Failed)
        val errorData = (dataRequest as DataRequest.Failed).errorMessage
        assertEquals(errorMessage, errorData)
    }

    @Test
    fun `fetchRandomQuote should update LiveData with error state when API call is not successful`() = runTest {
        // Given
        val errorResponse = mockk<Response<List<QuoteApi>>>()
        coEvery { api.fetchRandomQuote() } returns errorResponse
        every { errorResponse.isSuccessful } returns false
        every { errorResponse.errorBody()?.string() } returns errorMessage
        val observer: Observer<DataRequest> = mockk(relaxed = true)
        repository.dataRequest.observeForever(observer)

        // When
        repository.fetchRandomQuote()

        // Then
        val dataRequest = repository.dataRequest.value
        assertTrue(dataRequest is DataRequest.Failed)
        val errorData = (dataRequest as DataRequest.Failed).errorMessage
        assertEquals(errorMessage, errorData)
    }
}