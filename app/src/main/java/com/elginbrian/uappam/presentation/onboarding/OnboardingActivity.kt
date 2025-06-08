package com.elginbrian.uappam.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elginbrian.uappam.R
import com.elginbrian.uappam.data.repositoty.AuthRepository
import com.elginbrian.uappam.presentation.login.LoginActivity
import com.elginbrian.uappam.presentation.main.MainActivity
import com.elginbrian.uappam.presentation.register.RegisterActivity
import org.koin.android.ext.android.inject

class OnboardingActivity : AppCompatActivity() {

    private val authRepository: AuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (authRepository.getCurrentUser() != null) {
            navigateToMain()
            return
        }

        setContentView(R.layout.activity_onboarding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.iv_logo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvWelcome = findViewById<TextView>(R.id.tv_welcome)
        val ivLogo = findViewById<ImageView>(R.id.iv_logo)
        val loginButton = findViewById<Button>(R.id.btn_login)
        val registerLink = findViewById<TextView>(R.id.tv_register_link)
        val registerLayout = findViewById<LinearLayout>(R.id.layout_register)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        tvWelcome.startAnimation(fadeIn)
        ivLogo.startAnimation(fadeIn)
        loginButton.startAnimation(fadeIn)
        registerLayout.startAnimation(fadeIn)


        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}