package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var showPasswordButtonImage: Drawable
    private var hidePasswordButtonImage: Drawable
    private var isPasswordVisible: Boolean = false

    init {
        val originalShowIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility_on)!!
        val originalHideIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility_off)!!
        val iconSize = (originalShowIcon.intrinsicWidth * 0.5).toInt()

        showPasswordButtonImage = resizeDrawable(originalShowIcon, iconSize)
        hidePasswordButtonImage = resizeDrawable(originalHideIcon, iconSize)

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        updatePasswordVisibilityIcon()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.error_password), null)
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
        setOnTouchListener(this)
    }


    private fun resizeDrawable(drawable: Drawable, newSize: Int): Drawable {
        val width = newSize
        val height = (width.toFloat() / drawable.intrinsicWidth.toFloat() * drawable.intrinsicHeight.toFloat()).toInt()
        drawable.setBounds(0, 0, width, height)
        return drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun updatePasswordVisibilityIcon() {
        val drawable = if (isPasswordVisible) hidePasswordButtonImage else showPasswordButtonImage
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        inputType = if (isPasswordVisible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        text?.let { setSelection(it.length) }
        updatePasswordVisibilityIcon()
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val touchArea = width - paddingRight - compoundDrawables[2]?.bounds?.width()!!
            if (event.x >= touchArea) {
                togglePasswordVisibility()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}

