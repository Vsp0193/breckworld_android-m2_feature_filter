package com.breckworld.repository.remote.http.model

import java.io.Serializable

data class Wallet(
    var id: String?,
    var name: String?,
    var offers: List<Offer>?,
    var sectors: List<Sector>?,
    var stars: List<Star>?
): Serializable