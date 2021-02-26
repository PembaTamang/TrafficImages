package com.example.trafficimages.model

data class Camera(
    val camera_id: String,
    val image: String,
    val location: Location,
    val timestamp: String
)