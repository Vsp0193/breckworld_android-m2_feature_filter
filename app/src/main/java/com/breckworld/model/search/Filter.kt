package com.breckworld.model.search

data class Filter(
    val default: Int,
    val items: List<Item>,
    val title: String
)