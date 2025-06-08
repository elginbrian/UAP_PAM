package com.elginbrian.uappam.data.remote

import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.data.request.AddPlantRequest
import com.elginbrian.uappam.data.response.ApiResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("plant/all")
    fun getPlants(): Call<ApiResponse<List<Plant>>>

    @GET("plant/{name}")
    fun getPlantByName(
        @Path("name") name: String
    ): Call<ApiResponse<Plant>>

    @POST("plant/new")
    fun addPlant(
        @Body request: AddPlantRequest
    ): Call<ApiResponse<Plant>>

    @PUT("plant/{name}")
    fun updatePlant(
        @Path("name") name: String,
        @Body request: AddPlantRequest
    ): Call<ApiResponse<Plant>>

    @DELETE("plant/{name}")
    fun deletePlant(
        @Path("name") name: String
    ): Call<ApiResponse<Any>>
}