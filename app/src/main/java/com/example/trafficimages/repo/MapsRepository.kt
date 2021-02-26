package com.example.trafficimages.repo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.trafficimages.data.ApiInstance
import com.example.trafficimages.data.ApiInterface
import com.example.trafficimages.model.response

class MapsRepository (application: Application) {
    //application unsed but kept it here so that it can be used for room database

    var mliveData : MutableLiveData<response> = MutableLiveData()

    fun getLiveDatafromRepo():MutableLiveData<response>{
        return mliveData
    }
    suspend fun callApifromRepo(){
        val instance =  ApiInstance.getInstance().create(ApiInterface::class.java)
        val response =  instance.getData("2021-02-25T00:00:00")
        mliveData.postValue(response)
    }
}