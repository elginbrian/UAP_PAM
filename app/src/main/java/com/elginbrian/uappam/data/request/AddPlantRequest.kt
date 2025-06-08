package com.elginbrian.uappam.data.request

import com.google.gson.annotations.SerializedName

data class AddPlantRequest(
    @SerializedName("plant_name")
    val plantName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String
)