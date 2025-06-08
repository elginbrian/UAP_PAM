package com.elginbrian.uappam.data.repositoty

import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.data.remote.ApiService
import com.elginbrian.uappam.data.request.AddPlantRequest
import com.elginbrian.uappam.data.response.ApiResponse
import retrofit2.Call

class PlantRepository(private val apiService: ApiService) {

    fun getPlants(): Call<ApiResponse<List<Plant>>> {
        return apiService.getPlants()
    }

    fun getPlantByName(name: String): Call<ApiResponse<Plant>> {
        return apiService.getPlantByName(name)
    }

    fun addPlant(request: AddPlantRequest): Call<ApiResponse<Plant>> {
        return apiService.addPlant(request)
    }

    fun updatePlant(name: String, request: AddPlantRequest): Call<ApiResponse<Plant>> {
        return apiService.updatePlant(name, request)
    }

    fun deletePlant(name: String): Call<ApiResponse<Any>> {
        return apiService.deletePlant(name)
    }

    companion object {
        @Volatile
        private var instance: PlantRepository? = null
        fun getInstance(
            apiService: ApiService
        ): PlantRepository =
            instance ?: synchronized(this) {
                instance ?: PlantRepository(apiService).also { instance = it }
            }
    }
}