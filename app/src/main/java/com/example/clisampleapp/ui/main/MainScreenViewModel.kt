package com.example.clisampleapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clisampleapp.data.ItemRepository
import com.example.clisampleapp.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
  private val itemRepository: ItemRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(MainScreenUiState())
  val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      itemRepository.items.collect { items ->
        _uiState.update { state -> state.copy(items = items) }
      }
    }
  }

  fun onInputChange(text: String) {
    _uiState.update { state -> state.copy(inputText = text) }
  }

  fun onEditClick(itemId: Long) {
    val item = _uiState.value.items.find { it.id == itemId } ?: return
    _uiState.update { state -> state.copy(editingItemId = itemId, inputText = item.text) }
  }

  fun onPrimaryActionClick() {
    val state = _uiState.value
    val trimmed = state.inputText.trim()
    if (trimmed.isEmpty()) {
      return
    }

    val editingItemId = state.editingItemId
    viewModelScope.launch {
      if (editingItemId != null) {
        itemRepository.updateItem(editingItemId, trimmed)
        _uiState.update { current -> current.copy(editingItemId = null, inputText = "") }
      } else {
        itemRepository.addItem(trimmed)
        _uiState.update { current -> current.copy(inputText = "") }
      }
    }
  }

  fun onCancelClick() {
    _uiState.update { state -> state.copy(editingItemId = null, inputText = "") }
  }

  fun onDeleteClick(itemId: Long) {
    viewModelScope.launch {
      itemRepository.deleteItem(itemId)
      _uiState.update { state ->
        if (state.editingItemId == itemId) {
          state.copy(editingItemId = null, inputText = "")
        } else {
          state
        }
      }
    }
  }
}

data class MainScreenUiState(
  val inputText: String = "",
  val items: List<Item> = emptyList(),
  val editingItemId: Long? = null,
) {
  val isEditing: Boolean
    get() = editingItemId != null
}
