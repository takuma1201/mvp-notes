package com.takuma.mvpnotes.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.takuma.mvpnotes.model.Item
import com.takuma.mvpnotes.theme.MvpNotesTheme
import org.junit.Rule
import org.junit.Test

/** UI tests for [MainScreenContent]. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun mainScreenContent_showsTitleAddButtonAndItems() {
    composeTestRule.setContent {
      MvpNotesTheme {
        MainScreenContent(
          state =
            MainScreenUiState(
              items = listOf(Item(id = 1L, text = "Buy milk")),
            ),
          onInputChange = {},
          onPrimaryActionClick = {},
          onEditClick = {},
          onCancelClick = {},
          onDeleteClick = {},
        )
      }
    }

    composeTestRule.onNodeWithText("MVP Notes").assertExists()
    composeTestRule.onNodeWithText("Add").assertExists()
    composeTestRule.onNodeWithText("Buy milk").assertExists()
    composeTestRule.onNodeWithText("Edit").assertExists()
    composeTestRule.onNodeWithText("Delete").assertExists()
  }

  @Test
  fun mainScreenContent_withNoItems_showsEmptyState() {
    composeTestRule.setContent {
      MvpNotesTheme {
        MainScreenContent(
          state = MainScreenUiState(),
          onInputChange = {},
          onPrimaryActionClick = {},
          onEditClick = {},
          onCancelClick = {},
          onDeleteClick = {},
        )
      }
    }

    composeTestRule.onNodeWithText("No items yet").assertExists()
  }

  @Test
  fun mainScreenContent_inEditMode_showsSaveAndCancel() {
    composeTestRule.setContent {
      MvpNotesTheme {
        MainScreenContent(
          state =
            MainScreenUiState(
              inputText = "Buy milk",
              items = listOf(Item(id = 1L, text = "Buy milk")),
              editingItemId = 1L,
            ),
          onInputChange = {},
          onPrimaryActionClick = {},
          onEditClick = {},
          onCancelClick = {},
          onDeleteClick = {},
        )
      }
    }

    composeTestRule.onNodeWithText("Save").assertExists()
    composeTestRule.onNodeWithText("Cancel").assertExists()
  }
}
