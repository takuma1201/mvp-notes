package com.takuma.mvpnotes

import android.app.Application
import com.takuma.mvpnotes.data.DataStoreItemRepository
import com.takuma.mvpnotes.data.ItemRepository

class MvpNotesApplication : Application() {
  lateinit var itemRepository: ItemRepository
    private set

  override fun onCreate() {
    super.onCreate()
    itemRepository = DataStoreItemRepository(context = this)
  }
}
