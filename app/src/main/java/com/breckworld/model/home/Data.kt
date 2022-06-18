package com.breckworld.model.home

import com.breckworld.model.geostore.Filter
import com.breckworld.model.geostore.Sort

data class Data(
    val banners: List<Banner>,
    val buttons: List<Button>,
    val ctabs: List<Any>,
    val footer: List<Any>,
    val ids: IdsXX,
    val slides: List<SlideX>,
    val statuses: List<String>,
    val filters: List<Filter>,
    val sort: List<Sort>
)