package com.example.trafficimages

import android.util.Log
private const val TAG = "mTag"
fun mlog(data:String){
    if(BuildConfig.DEBUG){
        Log.d(TAG,data)
    }
}