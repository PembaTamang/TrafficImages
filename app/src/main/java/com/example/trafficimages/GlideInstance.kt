package com.example.trafficimages

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

class GlideInstance {
    companion object{
        fun glideInstance(application: Application):RequestManager{
            val options = RequestOptions().placeholder(R.drawable.ph)
            return Glide.with(application).applyDefaultRequestOptions(options)
        }
    }
}