package com.takuma.mvpnotes.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.takuma.mvpnotes.MvpNotesApplication
import com.takuma.mvpnotes.R
import com.takuma.mvpnotes.model.Item
import com.takuma.mvpnotes.theme.MvpNotesTheme

@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel =
    viewModel(
      factory =
        MainScreenViewModelFactory(
          (LocalContext.current.applicationContext as MvpNotesApplication).itemRepository,
        ),
    ),
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  MainScreenContent(
    state = state,
    onInputChange = viewModel::onInputChange,
    onPrimaryActionClick = viewModel::onPrimaryActionClick,
    onEditClick = viewModel::onEditClick,
    onCancelClick = viewModel::onCancelClick,
    onDeleteClick = viewModel::onDeleteClick,
    modifier = modifier,
  )
}

@Composable
internal fun MainScreenContent(
  state: MainScreenUiState,
  onInputChange: (String) -> Unit,
  onPrimaryActionClick: () -> Unit,
  onEditClick: (Long) -> Unit,
  onCancelClick: () -> Unit,
  onDeleteClick: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(text = stringResource(R.string.main_title), style = MaterialTheme.typography.headlineLarge)
      Text(
        text = stringResource(R.string.main_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Text(
        text = if (state.isEditing) "Edit item" else "Add item",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
      )

      if (state.isEditing) {
        val editingItem = state.items.find { it.id == state.editingItemId }
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = MaterialTheme.shapes.medium,
          color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
          Text(
            text = "Editing: ${editingItem?.text.orEmpty()}",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
          )
        }
      }

      OutlinedTextField(
        value = state.inputText,
        onValueChange = onInputChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(if (state.isEditing) "Updated text" else "New item") },
        placeholder = { Text(if (state.isEditing) "Update item text" else "Enter a new item") },
        singleLine = true,
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Button(onClick = onPrimaryActionClick, modifier = Modifier.weight(1f)) {
          Text(if (state.isEditing) "Save" else "Add")
        }
        if (state.isEditing) {
          OutlinedButton(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
            Text("Cancel")
          }
        }
      }
    }

    Column(
      modifier = Modifier.fillMaxWidth().weight(1f),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "Your items",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
      )

      if (state.items.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "No items yet",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          items(state.items, key = { it.id }) { item ->
            ItemCard(
              item = item,
              isEditing = state.editingItemId == item.id,
              onEditClick = { onEditClick(item.id) },
              onDeleteClick = { onDeleteClick(item.id) },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ItemCard(
  item: Item,
  isEditing: Boolean,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val containerColor =
    if (isEditing) {
      MaterialTheme.colorScheme.primaryContainer
    } else {
      MaterialTheme.colorScheme.surfaceContainerLow
    }

  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = containerColor),
    border =
      if (isEditing) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
      } else {
        null
      },
    elevation = CardDefaults.cardElevation(defaultElevation = if (isEditing) 2.dp else 1.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = item.text,
          modifier = Modifier.weight(1f).padding(end = 8.dp),
          style = MaterialTheme.typography.titleMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        if (isEditing) {
          Text(
            text = "Editing",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        TextButton(onClick = onEditClick) { Text("Edit") }
        TextButton(onClick = onDeleteClick) { Text("Delete") }
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun MainScreenPreview() {
  MvpNotesTheme {
    MainScreenContent(
      state = MainScreenUiState(items = listOf(Item(id = 0L, text = "Sample item"))),
      onInputChange = {},
      onPrimaryActionClick = {},
      onEditClick = {},
      onCancelClick = {},
      onDeleteClick = {},
      modifier = Modifier.fillMaxSize().padding(16.dp),
    )
  }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun MainScreenEmptyPreview() {
  MvpNotesTheme {
    MainScreenContent(
      state = MainScreenUiState(),
      onInputChange = {},
      onPrimaryActionClick = {},
      onEditClick = {},
      onCancelClick = {},
      onDeleteClick = {},
      modifier = Modifier.fillMaxSize().padding(16.dp),
    )
  }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun MainScreenEditPreview() {
  MvpNotesTheme {
    MainScreenContent(
      state =
        MainScreenUiState(
          inputText = "Sample item",
          items = listOf(Item(id = 0L, text = "Sample item")),
          editingItemId = 0L,
        ),
      onInputChange = {},
      onPrimaryActionClick = {},
      onEditClick = {},
      onCancelClick = {},
      onDeleteClick = {},
      modifier = Modifier.fillMaxSize().padding(16.dp),
    )
  }
}
