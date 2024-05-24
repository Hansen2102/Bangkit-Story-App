package com.dicoding.picodiploma.loginwithanimation.view.signup

import SignupViewModel
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        observeSignupResult()

        binding.progressBar.visibility = View.GONE
    }

    private fun observeSignupResult() {
        signupViewModel.signupResult.observe(this) { result ->
            result ?: return@observe
            binding.progressBar.visibility = View.GONE

            val (success, message) = result

            if (success) {
                showSuccessDialog(message ?: "Registration successful!")
            } else {
                showErrorDialog(message ?: "An error occurred during registration.")
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
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
        binding.signupButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            val name = binding.edRegisterNameText.text.toString()
            val email = binding.edRegisterEmailText.text.toString()
            val password = binding.edRegisterPasswordText.text.toString()

            signupViewModel.registerUser(name, email, password)
        }
    }
    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleText = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameText= ObjectAnimator.ofFloat(binding.edRegisterNameTitle, View.ALPHA, 1f).setDuration(500)
        val nameEditText = ObjectAnimator.ofFloat(binding.edRegisterNameText, View.ALPHA, 1f).setDuration(500)
        val emailText= ObjectAnimator.ofFloat(binding.edRegisterEmailTitle, View.ALPHA, 1f).setDuration(500)
        val emailEditText= ObjectAnimator.ofFloat(binding.edRegisterEmailText, View.ALPHA, 1f).setDuration(500)
        val passwordText = ObjectAnimator.ofFloat(binding.edRegisterPasswordTitle, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edRegisterPasswordText, View.ALPHA, 1f).setDuration(500)
        val signUpBottom = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(titleText,nameText,nameEditText,
            emailText,emailEditText,passwordText,passwordEditText,signUpBottom)
            start()
        }
    }

}