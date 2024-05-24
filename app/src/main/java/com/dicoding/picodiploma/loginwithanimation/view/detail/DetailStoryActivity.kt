package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var detailStoryBinding: ActivityDetailStoryBinding
    private var currentImageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val typeface = ResourcesCompat.getFont(this, R.font.google_sans)
        val title = SpannableString(getString(R.string.detail_story))

        title.setSpan(typeface?.let { TypefaceSpan(it) }, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.white)))
        supportActionBar?.title = title

        val data = intent.getParcelableExtra<ListStoryItem>("data")

        Glide.with(detailStoryBinding.root.context)
            .load(data?.photoUrl)
            .into(detailStoryBinding.ivDetailPhoto)
        detailStoryBinding.tvDetailName.text = data?.name
        detailStoryBinding.tvDetailDescription.text = data?.description

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}