package com.takuma.mvpnotes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.takuma.mvpnotes.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.itemDataStore: DataStore<Preferences> by preferencesDataStore(name = "items")

class DataStoreItemRepository(
  context: Context,
) : ItemRepository {
  private val dataStore = context.applicationContext.itemDataStore
  private val json = Json { ignoreUnknownKeys = true }

  override val items: Flow<List<Item>> =
    dataStore.data.map { preferences ->
      decodeItems(preferences[ITEMS_JSON_KEY].orEmpty())
    }

  override suspend fun addItem(text: String) {
    dataStore.edit { preferences ->
      val currentItems = decodeItems(preferences[ITEMS_JSON_KEY].orEmpty())
      val nextId = (currentItems.maxOfOrNull(Item::id) ?: -1L) + 1L
      val updatedItems = currentItems + Item(id = nextId, text = text)
      preferences[ITEMS_JSON_KEY] = encodeItems(updatedItems)
    }
  }

  override suspend fun updateItem(id: Long, text: String) {
    dataStore.edit { preferences ->
      val currentItems = decodeItems(preferences[ITEMS_JSON_KEY].orEmpty())
      val updatedItems = currentItems.map { item -> if (item.id == id) item.copy(text = text) else item }
      preferences[ITEMS_JSON_KEY] = encodeItems(updatedItems)
    }
  }

  override suspend fun deleteItem(id: Long) {
    dataStore.edit { preferences ->
      val currentItems = decodeItems(preferences[ITEMS_JSON_KEY].orEmpty())
      preferences[ITEMS_JSON_KEY] = encodeItems(currentItems.filterNot { it.id == id })
    }
  }

  private fun decodeItems(rawJson: String): List<Item> {
    if (rawJson.isBlank()) {
      return emptyList()
    }
    return runCatching { json.decodeFromString<List<Item>>(rawJson) }.getOrDefault(emptyList())
  }

  private fun encodeItems(items: List<Item>): String = json.encodeToString(items)

  private companion object {
    val ITEMS_JSON_KEY = stringPreferencesKey("items_json")
  }
}
