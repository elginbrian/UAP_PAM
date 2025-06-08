package com.elginbrian.uappam.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.data.repositoty.PlantRepository
import com.elginbrian.uappam.data.response.ApiResponse
import com.elginbrian.uappam.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val plantRepository: PlantRepository) : ViewModel() {

    private val _plantDetails = MutableLiveData<Resource<Plant>>()
    val plantDetails: LiveData<Resource<Plant>> = _plantDetails

    fun fetchPlantDetails(plantName: String) {
        _plantDetails.postValue(Resource.Loading())
        plantRepository.getPlantByName(plantName).enqueue(object : Callback<ApiResponse<Plant>> {
            override fun onResponse(
                call: Call<ApiResponse<Plant>>,
                response: Response<ApiResponse<Plant>>
            ) {
                if (response.isSuccessful) {
                    val plant = response.body()?.data
                    if (plant != null) {
                        _plantDetails.postValue(Resource.Success(plant))
                    } else {
                        _plantDetails.postValue(Resource.Error("Data tanaman tidak ditemukan."))
                    }
                } else {
                    _plantDetails.postValue(Resource.Error("Gagal memuat detail tanaman. Silakan coba lagi."))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Plant>>, t: Throwable) {
                _plantDetails.postValue(Resource.Error("Gagal terhubung ke server. Periksa koneksi internet Anda."))
            }
        })
    }
}