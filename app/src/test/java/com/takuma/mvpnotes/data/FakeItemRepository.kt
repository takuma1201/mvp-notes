package com.takuma.mvpnotes.data

import com.takuma.mvpnotes.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeItemRepository : ItemRepository {
  private val _items = MutableStateFlow<List<Item>>(emptyList())
  override val items: Flow<List<Item>> = _items.asStateFlow()

  private var nextId = 0L

  override suspend fun addItem(text: String) {
    val item = Item(id = nextId++, text = text)
    _items.update { current -> current + item }
  }

  override suspend fun updateItem(id: Long, text: String) {
    _items.update { current ->
      current.map { item -> if (item.id == id) item.copy(text = text) else item }
    }
  }

  override suspend fun deleteItem(id: Long) {
    _items.update { current -> current.filterNot { it.id == id } }
  }
}
