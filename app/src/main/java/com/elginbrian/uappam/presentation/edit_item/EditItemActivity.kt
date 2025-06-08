package com.elginbrian.uappam.presentation.edit_item

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditItemActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLANT_NAME = "extra_plant_name"
    }

    private val viewModel: EditItemViewModel by viewModel()
    private var originalPlantName: String? = null

    private lateinit var ivPlantImage: ImageView
    private lateinit var etPlantName: EditText
    private lateinit var etPlantPrice: EditText
    private lateinit var etPlantDescription: EditText
    private lateinit var btnUpdatePlant: Button
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

        topAppBar.setNavigationOnClickListener {
            finish()
        }

        originalPlantName = intent.getStringExtra(EXTRA_PLANT_NAME)

        if (originalPlantName == null) {
            val rootView = findViewById<View>(R.id.main)
            Snackbar.make(rootView, "Data tanaman tidak ditemukan", Snackbar.LENGTH_LONG).show()
            finish()
            return
        }

        observeViewModel()
        originalPlantName?.let {
            viewModel.fetchPlantDetails(it)
        }

        btnUpdatePlant.setOnClickListener {
            handleUpdate()
        }
    }

    private fun initViews() {
        ivPlantImage = findViewById(R.id.iv_plant_image)
        etPlantName = findViewById(R.id.et_plant_name)
        etPlantPrice = findViewById(R.id.et_plant_price)
        etPlantDescription = findViewById(R.id.et_plant_description)
        btnUpdatePlant = findViewById(R.id.btn_update_plant)
        topAppBar = findViewById(R.id.topAppBar)
    }

    private fun observeViewModel() {
        val rootView = findViewById<View>(R.id.main)
        viewModel.plantDetails.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> Unit
                is Resource.Success -> resource.data?.let { populateForm(it) }
                is Resource.Error -> Snackbar.make(rootView, resource.message ?: "Gagal memuat data", Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.updateStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> Snackbar.make(rootView, "Memperbarui...", Snackbar.LENGTH_SHORT).show()
                is Resource.Success -> {
                    Snackbar.make(rootView, "Data berhasil diperbarui", Snackbar.LENGTH_LONG).show()
                    rootView.postDelayed({
                        finish()
                    }, 1500)
                }
                is Resource.Error -> Snackbar.make(rootView, "Gagal memperbarui: ${resource.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun populateForm(plant: Plant) {
        topAppBar.title = "Edit ${plant.plantName}"
        etPlantName.setText(plant.plantName)
        etPlantPrice.setText(plant.price)
        etPlantDescription.setText(plant.description)
        ivPlantImage.setImageResource(R.drawable.plant_image)
    }

    private fun handleUpdate() {
        val newName = etPlantName.text.toString().trim()
        val newPrice = etPlantPrice.text.toString().trim()
        val newDescription = etPlantDescription.text.toString().trim()

        if (originalPlantName != null) {
            viewModel.updatePlant(originalPlantName!!, newName, newDescription, newPrice)
        } else {
            val rootView = findViewById<View>(R.id.main)
            Snackbar.make(rootView, "Nama tanaman asli tidak ditemukan, tidak dapat memperbarui.", Snackbar.LENGTH_LONG).show()
        }
    }
}