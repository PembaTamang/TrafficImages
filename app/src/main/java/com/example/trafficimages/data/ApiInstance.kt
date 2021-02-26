package com.example.trafficimages.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ApiInstance {
companion object{
   private const val baseURL = "https://api.data.gov.sg/v1/transport/"
    fun getInstance():Retrofit{
        return  Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
}