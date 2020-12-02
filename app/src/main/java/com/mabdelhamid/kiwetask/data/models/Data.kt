package com.mabdelhamid.kiwetask.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Data(
    val response: BaseResponse?
)

data class BaseResponse(
    val venues: List<Venue>?
)

@Parcelize
data class Venue(
    val name: String?,
    val location: Location?,
    val categories: List<Category>?,
    var markerId: String,
) : Parcelable

@Parcelize
data class Location(
    val address: String?,
    val lat: Double?,
    val lng: Double?
) : Parcelable

@Parcelize
data class Category(
    val name: String?,
    val icon: Icon?
) : Parcelable

@Parcelize
data class Icon(
    val prefix: String?,
    val suffix: String?
) : Parcelable
