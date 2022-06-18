package com.breckworld.model.search

data class Data(
    val banners: List<Banner>,
    val colors: ColorsX,
    val filters: List<Filter>,
    val images: Images,
    val labels: Labels,
    val sort: List<Sort>,
    val tabs: List<Tab>
)