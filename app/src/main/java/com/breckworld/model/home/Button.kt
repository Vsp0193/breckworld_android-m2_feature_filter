package com.breckworld.model.home

data class Button(
    var active: Boolean,
    val `data`: DataX,
    val ids: IdsX,
    val images: ImagesX,
    val labels: LabelsX,
    val type: String
)