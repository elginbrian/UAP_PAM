package com.elginbrian.uappam.presentation.edit_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.data.repositoty.PlantRepository
import com.elginbrian.uappam.data.request.AddPlantRequest
import com.elginbrian.uappam.data.response.ApiResponse
import com.elginbrian.uappam.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditItemViewModel(private val plantRepository: PlantRepository) : ViewModel() {

    private val _plantDetails = MutableLiveData<Resource<Plant>>()
    val plantDetails: LiveData<Resource<Plant>> = _plantDetails

    private val _updateStatus = MutableLiveData<Resource<Plant>>()
    val updateStatus: LiveData<Resource<Plant>> = _updateStatus

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
                    _plantDetails.postValue(Resource.Error("Gagal memuat detail tanaman: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Plant>>, t: Throwable) {
                _plantDetails.postValue(Resource.Error("Terjadi kesalahan jaringan: ${t.message}"))
            }
        })
    }

    fun updatePlant(
        originalName: String,
        newName: String,
        newDescription: String,
        newPrice: String
    ) {
        _updateStatus.postValue(Resource.Loading())

        if (newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
            _updateStatus.postValue(Resource.Error("Semua kolom harus diisi."))
            return
        }

        val request = AddPlantRequest(
            plantName = newName,
            description = newDescription,
            price = newPrice
        )

        plantRepository.updatePlant(originalName, request).enqueue(object : Callback<ApiResponse<Plant>> {
            override fun onResponse(
                call: Call<ApiResponse<Plant>>,
                response: Response<ApiResponse<Plant>>
            ) {
                if (response.isSuccessful) {
                    val updatedPlant = response.body()?.data
                    if (updatedPlant != null) {
                        _updateStatus.postValue(Resource.Success(updatedPlant))
                    } else {
                        _updateStatus.postValue(Resource.Error("Gagal memperbarui: data tidak valid."))
                    }
                } else {
                    _updateStatus.postValue(Resource.Error("Gagal memperbarui tanaman: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Plant>>, t: Throwable) {
                _updateStatus.postValue(Resource.Error("Terjadi kesalahan jaringan: ${t.message}"))
            }
        })
    }
}