package com.example.trafficimages.data

import com.example.trafficimages.model.response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("traffic-images")
    suspend fun getData(
        @Query("date_time") dateTime:String
    ):response
}