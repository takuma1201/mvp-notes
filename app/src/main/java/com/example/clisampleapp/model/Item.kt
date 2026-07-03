package com.example.clisampleapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
  val id: Long,
  val text: String,
)
