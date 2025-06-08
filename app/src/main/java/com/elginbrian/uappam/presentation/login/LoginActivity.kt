package com.elginbrian.uappam.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.presentation.main.MainActivity
import com.elginbrian.uappam.R
import com.elginbrian.uappam.util.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModel()

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var ivLogo: ImageView
    private lateinit var tvLoginTitle: TextView
    private lateinit var tvEmailLabel: TextView
    private lateinit var tvPasswordLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val mainView = findViewById<View>(R.id.iv_logo)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        applyAnimations()
        observeViewModel()

        btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email)
        etPass = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)
        ivLogo = findViewById(R.id.iv_logo)
        tvLoginTitle = findViewById(R.id.tv_login_title)
        tvEmailLabel = findViewById(R.id.tv_email_label)
        tvPasswordLabel = findViewById(R.id.tv_password_label)
    }

    private fun applyAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        ivLogo.startAnimation(fadeIn)
        tvLoginTitle.startAnimation(fadeIn)
        tvEmailLabel.startAnimation(fadeIn)
        etEmail.startAnimation(fadeIn)
        tvPasswordLabel.startAnimation(fadeIn)
        etPass.startAnimation(fadeIn)
        btnLogin.startAnimation(fadeIn)
    }

    private fun observeViewModel() {
        val rootView = findViewById<View>(android.R.id.content)
        viewModel.loginStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                    Snackbar.make(rootView, "Login berhasil!", Snackbar.LENGTH_LONG).show()
                    rootView.postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1500)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    btnLogin.isEnabled = true
                    Snackbar.make(rootView, "Gagal: ${resource.message}", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPass.text.toString().trim()

        viewModel.login(email, password)
    }
}