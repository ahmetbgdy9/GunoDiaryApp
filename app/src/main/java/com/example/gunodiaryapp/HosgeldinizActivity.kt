package com.example.gunodiaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView

class HosgeldinizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hosgeldiniz)
        val frameLayout = findViewById<FrameLayout>(R.id.yuklemeFrame)//ekrana dokunduğunda girişe gönderir
        frameLayout.setOnClickListener {
            val intent = Intent(this, GirisActivity::class.java)
            startActivity(intent)
            finish()
        }
        val textView = findViewById<TextView>(R.id.yuklemeDokun)   // ekrana dokunulduğunda girişe gitmemizi sağlıyor
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.yuklemedokun)
        textView.startAnimation(fadeIn)
    }
}