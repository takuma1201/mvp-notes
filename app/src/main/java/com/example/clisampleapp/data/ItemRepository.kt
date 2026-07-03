package com.example.clisampleapp.data

import com.example.clisampleapp.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
  val items: Flow<List<Item>>

  suspend fun addItem(text: String)

  suspend fun updateItem(id: Long, text: String)

  suspend fun deleteItem(id: Long)
}
