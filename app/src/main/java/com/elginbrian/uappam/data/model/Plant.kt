package com.elginbrian.uappam.data.model

import com.google.gson.annotations.SerializedName

data class Plant(
    @SerializedName("id")
    val id: Int,

    @SerializedName("plant_name")
    val plantName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)