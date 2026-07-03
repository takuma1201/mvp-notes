package com.example.clisampleapp

import android.app.Application
import com.example.clisampleapp.data.DataStoreItemRepository
import com.example.clisampleapp.data.ItemRepository

class CliSampleAppApplication : Application() {
  lateinit var itemRepository: ItemRepository
    private set

  override fun onCreate() {
    super.onCreate()
    itemRepository = DataStoreItemRepository(context = this)
  }
}
