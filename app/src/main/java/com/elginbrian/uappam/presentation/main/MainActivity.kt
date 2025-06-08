package com.elginbrian.uappam.presentation.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.model.Plant
import com.elginbrian.uappam.presentation.add_item.AddItemActivity
import com.elginbrian.uappam.presentation.detail.DetailActivity
import com.elginbrian.uappam.presentation.onboarding.OnboardingActivity
import com.elginbrian.uappam.util.Resource
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnRetry: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        initViews()
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

        btnRetry.setOnClickListener {
            viewModel.fetchPlants()
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rv_plants)
        progressBar = findViewById(R.id.progress_bar)
        errorLayout = findViewById(R.id.error_layout)
        tvErrorMessage = findViewById(R.id.tv_error_message)
        btnRetry = findViewById(R.id.btn_retry)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlants()
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
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    errorLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    resource.data?.let { plants ->
                        plantAdapter.submitList(plants)
                        recyclerView.scheduleLayoutAnimation()
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    tvErrorMessage.text = resource.message ?: "Gagal memuat data"
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
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val message = dialog.findViewById<TextView>(R.id.tv_dialog_message)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_dialog_cancel)
        val btnDelete = dialog.findViewById<Button>(R.id.btn_dialog_delete)

        message.text = "Apakah Anda yakin ingin menghapus ${plant.plantName}?"

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            viewModel.deletePlant(plant.plantName)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<Button>(R.id.btn_dialog_cancel)
        val btnLogout = dialog.findViewById<Button>(R.id.btn_dialog_logout)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout.setOnClickListener {
            dialog.dismiss()
            viewModel.logout()
            val intent = Intent(this, OnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
}