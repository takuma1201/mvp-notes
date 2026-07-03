package com.example.clisampleapp.data

import com.example.clisampleapp.model.Item
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FakeItemRepositoryTest {
  @Test
  fun addAndDeleteItem_updatesItemsFlow() = runTest {
    val repository = FakeItemRepository()

    repository.addItem("Buy milk")
    repository.addItem("Walk dog")

    assertEquals(
      listOf("Buy milk", "Walk dog"),
      repository.items.first().map(Item::text),
    )

    val itemToDelete = repository.items.first().first()
    repository.deleteItem(itemToDelete.id)

    assertEquals(listOf("Walk dog"), repository.items.first().map(Item::text))
  }

  @Test
  fun updateItem_changesOnlyTargetItem() = runTest {
    val repository = FakeItemRepository()

    repository.addItem("Buy milk")
    repository.addItem("Walk dog")

    val itemToUpdate = repository.items.first().first { it.text == "Buy milk" }
    repository.updateItem(itemToUpdate.id, "Buy oat milk")

    assertEquals(
      listOf("Buy oat milk", "Walk dog"),
      repository.items.first().map(Item::text),
    )
  }
}
