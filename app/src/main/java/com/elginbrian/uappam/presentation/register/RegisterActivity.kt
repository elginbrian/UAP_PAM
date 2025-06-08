package com.elginbrian.uappam.presentation.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.presentation.login.LoginActivity
import com.elginbrian.uappam.util.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModel()

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var ivLogo: ImageView
    private lateinit var tvRegisterTitle: TextView
    private lateinit var formContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        applyAnimations()
        observeViewModel()

        btnRegister.setOnClickListener {
            handleRegister()
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email)
        etPass = findViewById(R.id.et_password)
        etConfirmPass = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_bar)
        ivLogo = findViewById(R.id.iv_logo)
        tvRegisterTitle = findViewById(R.id.tv_register_title)
        formContainer = findViewById(R.id.form_container)
    }

    private fun applyAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        ivLogo.startAnimation(fadeIn)
        tvRegisterTitle.startAnimation(fadeIn)
        formContainer.startAnimation(fadeIn)
    }

    private fun observeViewModel() {
        val rootView = findViewById<View>(android.R.id.content)
        viewModel.registerStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnRegister.isEnabled = false
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    btnRegister.isEnabled = true
                    Snackbar.make(rootView, "Registrasi berhasil! Silakan login.", Snackbar.LENGTH_LONG).show()
                    rootView.postDelayed({
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1500)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    btnRegister.isEnabled = true
                    Snackbar.make(rootView, "Gagal: ${resource.message}", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleRegister() {
        val email = etEmail.text.toString().trim()
        val password = etPass.text.toString().trim()
        val confirmPassword = etConfirmPass.text.toString().trim()

        viewModel.register(email, password, confirmPassword)
    }
}