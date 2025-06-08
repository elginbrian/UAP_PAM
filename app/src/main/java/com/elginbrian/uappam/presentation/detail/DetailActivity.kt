package com.elginbrian.uappam.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.presentation.edit_item.EditItemActivity
import com.elginbrian.uappam.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLANT_NAME = "extra_plant_name"
    }

    private val viewModel: DetailViewModel by viewModel()
    private lateinit var ivPlantDetail: ImageView
    private lateinit var tvPlantName: TextView
    private lateinit var tvPlantPrice: TextView
    private lateinit var tvPlantDescription: TextView
    private lateinit var btnUpdate: Button
    private lateinit var topAppBar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinator_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        topAppBar.setNavigationOnClickListener {
            finish()
        }

        val plantName = intent.getStringExtra(EXTRA_PLANT_NAME)

        if (plantName == null) {
            val rootView = findViewById<View>(R.id.coordinator_layout)
            Snackbar.make(rootView, "Nama tanaman tidak valid", Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        observePlantDetails()
        viewModel.fetchPlantDetails(plantName)

        btnUpdate.setOnClickListener {
            val intent = Intent(this, EditItemActivity::class.java).apply {
                putExtra(EditItemActivity.EXTRA_PLANT_NAME, plantName)
            }
            startActivity(intent)
        }
    }

    private fun initViews() {
        ivPlantDetail = findViewById(R.id.iv_plant_detail)
        tvPlantName = findViewById(R.id.tv_plant_name_detail)
        tvPlantPrice = findViewById(R.id.tv_plant_price_detail)
        tvPlantDescription = findViewById(R.id.tv_plant_description)
        btnUpdate = findViewById(R.id.btn_update)
        topAppBar = findViewById(R.id.topAppBar)
    }

    private fun observePlantDetails() {
        val rootView = findViewById<View>(R.id.coordinator_layout)
        viewModel.plantDetails.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Sebaiknya tampilkan ProgressBar di layout
                }
                is Resource.Success -> {
                    resource.data?.let { plant ->
                        populatePlantDetails(plant)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(rootView, resource.message ?: "Gagal memuat detail", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun populatePlantDetails(plant: Plant) {
        ivPlantDetail.setImageResource(R.drawable.plant_image)
        tvPlantName.text = plant.plantName
        tvPlantPrice.text = plant.price
        tvPlantDescription.text = plant.description
        topAppBar.title = plant.plantName
    }
}