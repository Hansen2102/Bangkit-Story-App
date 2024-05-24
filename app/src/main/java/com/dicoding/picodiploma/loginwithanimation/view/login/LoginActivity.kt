package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        observeLoginResult()

        binding.progressBar2.visibility = View.GONE
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            result ?: return@observe
            binding.progressBar2.visibility = View.GONE

            val (success, message) = result

            if (success) {
                val email = binding.edLoginEmailText.text.toString()
                token = loginViewModel.token.value.toString()
                Log.d("token", "ini $token")
                val user = UserModel(email, token)

                loginViewModel.saveSession(user)

                showSuccessDialog(message ?: "Login successful!")
            } else {
                showErrorDialog(message ?: "An error occurred during login.")
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage("Login $message")
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.apply{
                    putExtra("token",token)
                }
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE

            val email = binding.edLoginEmailText.text.toString()
            val password = binding.edLoginPasswordText.text.toString()

            loginViewModel.loginUser(email, password)
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title_text = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val email_text = ObjectAnimator.ofFloat(binding.edLoginEmailTitle, View.ALPHA, 1f).setDuration(500)
        val email_edit_text = ObjectAnimator.ofFloat(binding.edLoginEmailText, View.ALPHA, 1f).setDuration(500)
        val password_text = ObjectAnimator.ofFloat(binding.edLoginPasswordTitle, View.ALPHA, 1f).setDuration(500)
        val password_edit_text = ObjectAnimator.ofFloat(binding.edLoginPasswordText, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title_text,message,email_text,email_edit_text,
                password_text,password_edit_text,loginButton)
            start()
        }
    }

}