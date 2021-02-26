package com.example.trafficimages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trafficimages.databinding.ActivityMapsBinding
import com.example.trafficimages.databinding.ImageAlertBinding
import com.example.trafficimages.model.Camera
import com.example.trafficimages.model.MapData
import com.example.trafficimages.model.response
import com.example.trafficimages.viewmodel.MyViewModel
import com.example.trafficimages.viewmodel.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private var camera = ArrayList<Camera>()
    private var mapData = ArrayList<MapData>()
    private var ready = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipe.setOnRefreshListener {
            getData()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun getData() {
        binding.swipe.isRefreshing = true
        Toast.makeText(this, "Getting data", Toast.LENGTH_SHORT).show()
        if (ready) {
            viewModel.callApi()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        ready = true
        viewModelFactory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MyViewModel::class.java)
        viewModel.getLiveData().observe(this, Observer<response> {
            if (it != null) {
                mlog("Size of data ${it.items.size}")
                it.items.forEach { dataItem ->
                    camera.clear()
                    camera.addAll(dataItem.cameras)
                    processData(camera)
                }
            }

        }).also {
           getData()
        }

        mMap.setOnMarkerClickListener { marker ->
            mlog("Tag ${marker.tag}")
            val alert = AlertDialog.Builder(this, R.style.mAlertDialog)
            val alertBinding = ImageAlertBinding.inflate(LayoutInflater.from(applicationContext),null,false)
            alert.setView(alertBinding.root)
            GlideInstance.glideInstance(application).load(marker.tag).into(alertBinding.image)
            alert.setCancelable(true)
            alert.show()

            true
        }
    }

    private fun processData(camera: java.util.ArrayList<Camera>) {
        CoroutineScope(IO).launch {
            camera.forEach { cam ->
                withContext(Main){
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(cam.location.latitude, cam.location.longitude))
                            .title(cam.timestamp)
                    ).tag = cam.image
                }
            }.also {
                withContext(Main){
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(
                            camera[camera.size-1].location.latitude,
                            camera[camera.size-1].location.longitude
                        ))
                        .zoom(12f)
                        .build()))
                    if(binding.swipe.isRefreshing){
                        binding.swipe.isRefreshing = false
                    }

                    Toast.makeText(this@MapsActivity,"Success", Toast.LENGTH_SHORT).show()
                }
            }



        }
    }
}