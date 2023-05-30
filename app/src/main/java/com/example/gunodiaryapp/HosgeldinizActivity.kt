package com.example.gunodiaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView

class HosgeldinizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosgeldiniz)
        val sonrakiSayfaBtn = findViewById<ImageButton>(R.id.sonrakiSayfaBtn)
        sonrakiSayfaBtn.setOnClickListener {
            val intent = Intent(this, GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}