package com.takuma.mvpnotes.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
  val id: Long,
  val text: String,
)
