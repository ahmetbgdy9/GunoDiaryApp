package com.example.gunodiaryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Ayarlar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayarlar)

        val sifredegistirBtn = findViewById<Button>(R.id.sifredegistirbtn)
        val pinBtn = findViewById<Button>(R.id.pinbtn)
        val hesapSilBtn = findViewById<Button>(R.id.hesabsilbtn)

        sifredegistirBtn.setOnClickListener {
            val intent = Intent(this, SifreDegistir::class.java)
            startActivity(intent)
            finish()
        }

        pinBtn.setOnClickListener {
            val intent = Intent(this, Pin::class.java)
            startActivity(intent)
            finish()
        }

        hesapSilBtn.setOnClickListener {

            //hesap sil kodu
        }
    }
}