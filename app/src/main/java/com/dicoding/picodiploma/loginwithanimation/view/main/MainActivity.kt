package com.dicoding.picodiploma.loginwithanimation.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.LoadingStateAdapter
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.addStory.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val storyViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private var token: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else {
                token = user.token
                Log.d("token", "ini $token")
                storyViewModel.setToken(token)
            }
        }

        setUserData()

        binding.writeStoryButton.setOnClickListener {
            Intent(this, AddStoryActivity::class.java).apply {
                putExtra("token", token)
                startActivity(this)
            }
        }

        val typeface = ResourcesCompat.getFont(this, R.font.google_sans)
        val title = SpannableString(getString(R.string.app_name))

        title.setSpan(typeface?.let { TypefaceSpan(it) }, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.white)))
        supportActionBar?.title = title

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserStory.addItemDecoration(itemDecoration)

        setupView()
        playAnimation()
    }

    @SuppressLint("InflateParams")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val menuItem = menu.findItem(R.id.logout)
        val customView = layoutInflater.inflate(R.layout.custom_menu_item, null) as TextView
        customView.text = getString(R.string.logout)

        val typeface = ResourcesCompat.getFont(this, R.font.google_sans)
        customView.typeface = typeface

        menuItem.actionView = customView

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.find_location -> {
                Intent(this, MapsActivity::class.java).apply {
                    putExtra("token", token)
                    startActivity(this)
                }
            }
            R.id.logout -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
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
    }

    private fun setUserData() {
        val adapter = StoryAdapter{data ->
            val intent = Intent(this, DetailStoryActivity::class.java)
            intent.apply{
                putExtra("data", data)
            }
            val imgPhoto: ImageView = findViewById(R.id.iv_detail_photo)
            val tvName: TextView = findViewById(R.id.tv_detail_name)
            val tvDescription: TextView = findViewById(R.id.tv_detail_description)

            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(imgPhoto, imgPhoto.transitionName),
                Pair.create(tvName, tvName.transitionName),
                Pair.create(tvDescription, tvDescription.transitionName)
            )
            startActivity(intent, optionsCompat.toBundle())
        }
        binding.rvUserStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.listStory.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(name,message)
            start()
        }
    }

}