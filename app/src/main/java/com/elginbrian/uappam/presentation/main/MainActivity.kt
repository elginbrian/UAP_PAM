package com.elginbrian.uappam.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.presentation.add_item.AddItemActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rv_plants)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addListButton = findViewById<Button>(R.id.btn_add_list)
        addListButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        // CATATAN: Navigasi untuk setiap item di RecyclerView ke DetailActivity
        // harus diimplementasikan di dalam RecyclerView.Adapter.
        // Anda perlu membuat adapter untuk R.id.rv_plants dan menambahkan
        // OnClickListener pada setiap item di sana.
    }
}