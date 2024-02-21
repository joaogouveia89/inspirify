package io.github.joaogouveia89.inspirify

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.joaogouveia89.inspirify.di.InspirifyComponent
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
open class InspirifyUnitTest {
    // Rule to force execution of tasks synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock your InspirifyComponent
    protected val inspirifyComponent: InspirifyComponent = mockk()
}