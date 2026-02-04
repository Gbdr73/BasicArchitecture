package ru.otus.basicarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class AddressViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val wizardCache: WizardCache = mock()
    private val addressSuggestUseCase: AddressSuggestUseCase = mock()
    private val viewModel: AddressViewModel = AddressViewModel(wizardCache, addressSuggestUseCase)

    @Before
    fun before(){
        Dispatchers.setMain(testDispatcher)
        println("Начинается тест")
    }
    @After
    fun after(){
        Dispatchers.resetMain()
        println("Тест закончился")
    }
    @Test
    fun `empty full address returns error`() {
        runTest {
            whenever(wizardCache.userAddress).thenReturn(UserAddress("", "", "", "", "", ""))
            viewModel.validateData()
            val actual = viewModel.errorEmptyAddress.value ?: throw RuntimeException("errorEmptyAddress == null")
            assertTrue(actual)
        }
    }
    @Test
    fun `valid input returns success`() {
        runTest {
            whenever(wizardCache.userAddress).thenReturn(UserAddress("", "", "", "", "", "Россия, Москва"))
            viewModel.validateData()
            val actual = viewModel.errorEmptyAddress.value ?: throw RuntimeException("errorEmptyAddress == null")
            assertFalse(actual)
        }
    }
    @Test
    fun `network success`() {
        runTest {
            whenever(addressSuggestUseCase.invoke(any())).thenReturn(getAddress())
            launch {
                viewModel.searchAddress("query")
            }
            advanceUntilIdle()
            val actual = viewModel.listUserAddress.value
            assertNotNull(actual)
        }
    }

    @Test
    fun `network error`() {
        runTest {
            whenever(addressSuggestUseCase.invoke(any())).thenThrow(RuntimeException("Network error"))
            launch {
                viewModel.searchAddress("query")
            }
            advanceUntilIdle()
            val actual = viewModel.errorNetwork.value ?: throw RuntimeException("errorNetwork == null")
            assertTrue(actual)
        }
    }

    private fun getAddress(): List<UserAddress> {
        return listOf(UserAddress("Россия", "Москва", "Фёдора Полетаева", "13", "", "Россия, Москва, Фёдора Полетаева, 13"))
    }
}
