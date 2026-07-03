package com.example.clisampleapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clisampleapp.data.ItemRepository

class MainScreenViewModelFactory(
  private val itemRepository: ItemRepository,
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
      return MainScreenViewModel(itemRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
  }
}
