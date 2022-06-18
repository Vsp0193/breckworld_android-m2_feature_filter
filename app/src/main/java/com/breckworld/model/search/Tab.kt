package com.breckworld.model.search

data class Tab(
    val cards: List<Card>,
    val id: String,
    val load_more: String,
    val tab_count: String,
    val title: String
)