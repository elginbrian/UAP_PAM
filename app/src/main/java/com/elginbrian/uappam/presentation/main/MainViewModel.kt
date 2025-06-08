package com.elginbrian.uappam.presentation.main

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

class MainViewModel(private val plantRepository: PlantRepository) : ViewModel() {

    private val _plants = MutableLiveData<Resource<List<Plant>>>()
    val plants: LiveData<Resource<List<Plant>>> = _plants

    private val _deleteStatus = MutableLiveData<Resource<Any>>()
    val deleteStatus: LiveData<Resource<Any>> = _deleteStatus

    init {
        fetchPlants()
    }

    fun fetchPlants() {
        _plants.postValue(Resource.Loading())
        plantRepository.getPlants().enqueue(object : Callback<ApiResponse<List<Plant>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Plant>>>,
                response: Response<ApiResponse<List<Plant>>>
            ) {
                if (response.isSuccessful) {
                    val plantList = response.body()?.data
                    if (plantList != null) {
                        _plants.postValue(Resource.Success(plantList))
                    } else {
                        _plants.postValue(Resource.Error("Data tanaman tidak ditemukan."))
                    }
                } else {
                    _plants.postValue(Resource.Error("Gagal memuat data: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Plant>>>, t: Throwable) {
                _plants.postValue(Resource.Error("Terjadi kesalahan jaringan: ${t.message}"))
            }
        })
    }

    fun deletePlant(plantName: String) {
        _deleteStatus.postValue(Resource.Loading())
        plantRepository.deletePlant(plantName).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(
                call: Call<ApiResponse<Any>>,
                response: Response<ApiResponse<Any>>
            ) {
                if (response.isSuccessful) {
                    _deleteStatus.postValue(Resource.Success(response.body()?.message ?: "Tanaman berhasil dihapus"))
                } else {
                    _deleteStatus.postValue(Resource.Error("Gagal menghapus tanaman: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                _deleteStatus.postValue(Resource.Error("Terjadi kesalahan jaringan: ${t.message}"))
            }
        })
    }
}