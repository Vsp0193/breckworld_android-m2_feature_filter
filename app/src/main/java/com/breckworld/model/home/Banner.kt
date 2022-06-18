package com.breckworld.model.home

data class Banner(
    var colors: Colors,
    var icon: String,
    var label: String,
    val target: Target
)