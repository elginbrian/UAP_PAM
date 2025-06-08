package com.elginbrian.uappam.presentation.add_item

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

class AddItemViewModel(private val plantRepository: PlantRepository) : ViewModel() {

    private val _addPlantStatus = MutableLiveData<Resource<Plant>>()
    val addPlantStatus: LiveData<Resource<Plant>> = _addPlantStatus

    fun addPlant(plantName: String, description: String, price: String) {
        _addPlantStatus.postValue(Resource.Loading())

        if (plantName.isEmpty() || description.isEmpty() || price.isEmpty()) {
            _addPlantStatus.postValue(Resource.Error("Semua kolom harus diisi."))
            return
        }

        val request = AddPlantRequest(
            plantName = plantName,
            description = description,
            price = price
        )

        plantRepository.addPlant(request).enqueue(object : Callback<ApiResponse<Plant>> {
            override fun onResponse(
                call: Call<ApiResponse<Plant>>,
                response: Response<ApiResponse<Plant>>
            ) {
                if (response.isSuccessful) {
                    val newPlant = response.body()?.data
                    if (newPlant != null) {
                        _addPlantStatus.postValue(Resource.Success(newPlant))
                    } else {
                        _addPlantStatus.postValue(Resource.Error("Gagal menambahkan tanaman: data tidak valid."))
                    }
                } else {
                    _addPlantStatus.postValue(Resource.Error("Gagal menambahkan tanaman: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Plant>>, t: Throwable) {
                _addPlantStatus.postValue(Resource.Error("Terjadi kesalahan jaringan: ${t.message}"))
            }
        })
    }
}