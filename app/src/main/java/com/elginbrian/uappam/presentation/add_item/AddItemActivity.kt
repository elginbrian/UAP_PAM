package com.elginbrian.uappam.presentation.add_item

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddItemActivity : AppCompatActivity() {

    private val viewModel: AddItemViewModel by viewModel()

    private lateinit var etPlantName: EditText
    private lateinit var etPlantPrice: EditText
    private lateinit var etPlantDescription: EditText
    private lateinit var btnAddPlant: Button
    private lateinit var cardPlantImage: MaterialCardView
    private lateinit var tvPlantNameLabel: TextView
    private lateinit var tvPlantPriceLabel: TextView
    private lateinit var tvPlantDescriptionLabel: TextView

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
        applyAnimations()
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
        cardPlantImage = findViewById(R.id.card_plant_image)
        tvPlantNameLabel = findViewById(R.id.tv_plant_name_label)
        tvPlantPriceLabel = findViewById(R.id.tv_plant_price_label)
        tvPlantDescriptionLabel = findViewById(R.id.tv_plant_description_label)
    }

    private fun applyAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        cardPlantImage.startAnimation(fadeIn)
        tvPlantNameLabel.startAnimation(fadeIn)
        etPlantName.startAnimation(fadeIn)
        tvPlantPriceLabel.startAnimation(fadeIn)
        etPlantPrice.startAnimation(fadeIn)
        tvPlantDescriptionLabel.startAnimation(fadeIn)
        etPlantDescription.startAnimation(fadeIn)
        btnAddPlant.startAnimation(fadeIn)
    }

    private fun observeViewModel() {
        val rootView = findViewById<View>(R.id.main)
        viewModel.addPlantStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Snackbar.make(rootView, "Menambahkan...", Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    Snackbar.make(rootView, "Tanaman berhasil ditambahkan", Snackbar.LENGTH_LONG).show()
                    rootView.postDelayed({
                        finish()
                    }, 1500)
                }
                is Resource.Error -> {
                    Snackbar.make(rootView, "Gagal: ${resource.message}", Snackbar.LENGTH_LONG).show()
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