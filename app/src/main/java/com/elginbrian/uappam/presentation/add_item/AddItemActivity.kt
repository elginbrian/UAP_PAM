package com.elginbrian.uappam.presentation.add_item

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddItemActivity : AppCompatActivity() {

    private val viewModel: AddItemViewModel by viewModel()

    private lateinit var etPlantName: EditText
    private lateinit var etPlantPrice: EditText
    private lateinit var etPlantDescription: EditText
    private lateinit var btnAddPlant: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        observeViewModel()

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }

        btnAddPlant.setOnClickListener {
            handleAddPlant()
        }
    }

    private fun initViews() {
        etPlantName = findViewById(R.id.et_plant_name)
        etPlantPrice = findViewById(R.id.et_plant_price)
        etPlantDescription = findViewById(R.id.et_plant_description)
        btnAddPlant = findViewById(R.id.btn_add_plant)
    }

    private fun observeViewModel() {
        viewModel.addPlantStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Toast.makeText(this, "Menambahkan...", Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Toast.makeText(this, "Tanaman berhasil ditambahkan", Toast.LENGTH_LONG).show()
                    finish()
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Gagal: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleAddPlant() {
        val name = etPlantName.text.toString().trim()
        val price = etPlantPrice.text.toString().trim()
        val description = etPlantDescription.text.toString().trim()

        viewModel.addPlant(name, description, price)
    }
}