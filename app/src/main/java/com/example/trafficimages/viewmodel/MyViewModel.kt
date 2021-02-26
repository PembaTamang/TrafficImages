package com.example.trafficimages.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.trafficimages.model.response
import com.example.trafficimages.repo.MapsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyViewModel (application: Application) :AndroidViewModel(application){

    var repository = MapsRepository(application)

    fun getLiveData():MutableLiveData<response>{
        return repository.getLiveDatafromRepo()
    }

   fun callApi(){
       CoroutineScope(IO).launch {
           repository.callApifromRepo()
       }

    }
}