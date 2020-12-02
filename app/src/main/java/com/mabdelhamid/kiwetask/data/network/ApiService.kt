package com.mabdelhamid.kiwetask.data.network

import com.mabdelhamid.kiwetask.data.models.Data
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Urls.VENUES)
    fun getVenues(
        @Query("ll", encoded = true) latLng: String
    ): Single<Data>
}