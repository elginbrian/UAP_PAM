package com.elginbrian.uappam.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.presentation.add_item.AddItemActivity
import com.elginbrian.uappam.presentation.detail.DetailActivity
import com.elginbrian.uappam.util.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rv_plants)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.rv_plants)
        setupRecyclerView()
        observePlants()
        observeDeleteStatus()

        val addListButton = findViewById<Button>(R.id.btn_add_list)
        addListButton.setOnClickListener {
            it.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()

                    val intent = Intent(this, AddItemActivity::class.java)
                    startActivity(intent)
                }
                .start()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlants()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantAdapter(
            onDetailClick = { plant ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_PLANT_NAME, plant.plantName)
                }
                startActivity(intent)
            },
            onDeleteClick = { plant ->
                showDeleteConfirmationDialog(plant)
            }
        )
        recyclerView.apply {
            adapter = plantAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observePlants() {
        viewModel.plants.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    resource.data?.let { plants ->
                        plantAdapter.submitList(plants)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(recyclerView, resource.message ?: "Gagal memuat data", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeDeleteStatus() {
        viewModel.deleteStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    Snackbar.make(recyclerView, "Tanaman berhasil dihapus", Snackbar.LENGTH_LONG)
                        .setAction("Urungkan") {
                        }
                        .show()
                    viewModel.fetchPlants()
                }
                is Resource.Error -> {
                    Snackbar.make(recyclerView, resource.message ?: "Gagal menghapus", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(plant: Plant) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Tanaman")
            .setMessage("Apakah Anda yakin ingin menghapus ${plant.plantName}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deletePlant(plant.plantName)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}