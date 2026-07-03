package com.example.clisampleapp.ui.main

import com.example.clisampleapp.data.FakeItemRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {
  private val testDispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun uiState_initialValues() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    advanceUntilIdle()

    val state = viewModel.uiState.value
    assertEquals("", state.inputText)
    assertEquals(emptyList<String>(), state.items.map { it.text })
    assertNull(state.editingItemId)
  }

  @Test
  fun onInputChange_updatesInputText() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())

    viewModel.onInputChange("Buy milk")

    assertEquals("Buy milk", viewModel.uiState.value.inputText)
  }

  @Test
  fun onPrimaryActionClick_addsItemAndClearsInput() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")

    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    assertEquals("", viewModel.uiState.value.inputText)
    assertEquals(listOf("Buy milk"), viewModel.uiState.value.items.map { it.text })
  }

  @Test
  fun onPrimaryActionClick_withEmptyInput_doesNotAddItem() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())

    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    assertEquals(emptyList<String>(), viewModel.uiState.value.items.map { it.text })
  }

  @Test
  fun onPrimaryActionClick_withBlankInput_doesNotAddItem() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("   ")

    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    assertEquals(emptyList<String>(), viewModel.uiState.value.items.map { it.text })
  }

  @Test
  fun onDeleteClick_removesOnlySelectedItem() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")
    viewModel.onPrimaryActionClick()
    viewModel.onInputChange("Walk dog")
    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    val itemToDelete = viewModel.uiState.value.items.first { it.text == "Buy milk" }
    viewModel.onDeleteClick(itemToDelete.id)
    advanceUntilIdle()

    assertEquals(listOf("Walk dog"), viewModel.uiState.value.items.map { it.text })
  }

  @Test
  fun onEditClick_loadsItemTextIntoInput() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")
    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    val item = viewModel.uiState.value.items.first()
    viewModel.onEditClick(item.id)

    assertEquals(item.id, viewModel.uiState.value.editingItemId)
    assertEquals("Buy milk", viewModel.uiState.value.inputText)
  }

  @Test
  fun onPrimaryActionClick_inEditMode_updatesOnlySelectedItem() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")
    viewModel.onPrimaryActionClick()
    viewModel.onInputChange("Walk dog")
    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    val itemToEdit = viewModel.uiState.value.items.first { it.text == "Buy milk" }
    viewModel.onEditClick(itemToEdit.id)
    viewModel.onInputChange("Buy oat milk")

    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    assertEquals(listOf("Buy oat milk", "Walk dog"), viewModel.uiState.value.items.map { it.text })
    assertEquals("", viewModel.uiState.value.inputText)
    assertNull(viewModel.uiState.value.editingItemId)
  }

  @Test
  fun onPrimaryActionClick_inEditMode_withEmptyInput_doesNotUpdateItem() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")
    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    val item = viewModel.uiState.value.items.first()
    viewModel.onEditClick(item.id)
    viewModel.onInputChange("   ")

    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    assertEquals(listOf("Buy milk"), viewModel.uiState.value.items.map { it.text })
    assertEquals(item.id, viewModel.uiState.value.editingItemId)
  }

  @Test
  fun onCancelClick_clearsEditModeAndInput() = runTest {
    val viewModel = MainScreenViewModel(FakeItemRepository())
    viewModel.onInputChange("Buy milk")
    viewModel.onPrimaryActionClick()
    advanceUntilIdle()

    val item = viewModel.uiState.value.items.first()
    viewModel.onEditClick(item.id)
    viewModel.onInputChange("Changed text")

    viewModel.onCancelClick()

    assertNull(viewModel.uiState.value.editingItemId)
    assertEquals("", viewModel.uiState.value.inputText)
    assertEquals(listOf("Buy milk"), viewModel.uiState.value.items.map { it.text })
  }
}
