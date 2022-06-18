package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Data(
    //val banners: List< @RawValue Any>,
    val banners: List<Banner>,
    val buttons: List<Button>,
    val ctabs: List<Ctabs>,
    val footer: List<Footer>,
    val ids: IdsXX,
    val slides: List<Slide>,
    val statuses: List<String>,
    val images: Imagess
) : Parcelable