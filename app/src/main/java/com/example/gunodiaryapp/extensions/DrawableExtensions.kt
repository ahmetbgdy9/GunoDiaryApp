package com.ahmet.takvim.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import androidx.core.content.ContextCompat
import com.example.gunodiaryapp.R



fun Context.isaretle(): Drawable {
    val cizim = ContextCompat.getDrawable(this, R.drawable.nokta)
    return InsetDrawable(cizim, 24, 14, 24, 4)
}