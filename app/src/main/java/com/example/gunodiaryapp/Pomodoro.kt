package com.example.gunodiaryapp


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat


class Pomodoro : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        val sharedPref = getSharedPreferences("pomoSureler", Context.MODE_PRIVATE)

        //region tasarım
        val pomodoroSureSecimButonu = findViewById<Button>(R.id.button1)
        val kisaSureSecimButonu = findViewById<Button>(R.id.button2)
        val uzunSureSecimButonu = findViewById<Button>(R.id.button3)
        val baslaButonu = findViewById<Button>(R.id.button6)
        val ayarlarButonu = findViewById<ImageButton>(R.id.settings_button)
        val zamanGoster = findViewById<TextView>(R.id.timer_textview)

        var pomoSureZaman: Long


        fun butonlaridevredisibirak(){
            pomodoroSureSecimButonu.visibility = View.GONE
            kisaSureSecimButonu.visibility = View.GONE
            uzunSureSecimButonu.visibility = View.GONE
            baslaButonu.visibility = View.GONE
            ayarlarButonu.visibility = View.GONE
        }
        fun butonlarigetir(){
            pomodoroSureSecimButonu.visibility = View.VISIBLE
            kisaSureSecimButonu.visibility = View.VISIBLE
            uzunSureSecimButonu.visibility = View.VISIBLE
            baslaButonu.visibility = View.VISIBLE
            ayarlarButonu.visibility = View.VISIBLE
        }


        pomodoroSureSecimButonu.setOnClickListener {
            pomodoroSureSecimButonu.setBackgroundResource(R.drawable.bgbuton2)
            kisaSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
            uzunSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
        }

        kisaSureSecimButonu.setOnClickListener {
            pomodoroSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
            kisaSureSecimButonu.setBackgroundResource(R.drawable.bgbuton2)
            uzunSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
        }

        uzunSureSecimButonu.setOnClickListener {
            pomodoroSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
            kisaSureSecimButonu.setBackgroundResource(R.drawable.bgbuton)
            uzunSureSecimButonu.setBackgroundResource(R.drawable.bgbuton2)
        }
        baslaButonu.setOnClickListener {
            butonlaridevredisibirak()
            if (pomodoroSureSecimButonu.background.constantState == ContextCompat.getDrawable(this, R.drawable.bgbuton2)?.constantState) {
                pomoSureZaman = sharedPref.getString("pomoSure", null)?.toLongOrNull() ?: 0
            } else if (kisaSureSecimButonu.background.constantState == ContextCompat.getDrawable(this, R.drawable.bgbuton2)?.constantState) {
                pomoSureZaman = sharedPref.getString("kisaSure", null)?.toLongOrNull() ?: 0
            } else if (uzunSureSecimButonu.background.constantState == ContextCompat.getDrawable(this, R.drawable.bgbuton2)?.constantState) {
                pomoSureZaman = sharedPref.getString("uzunSure", null)?.toLongOrNull() ?: 0
            } else {
                Toast.makeText(applicationContext, "Lütfen bir süre seçiniz.", Toast.LENGTH_LONG).show()
                butonlarigetir()
                return@setOnClickListener
            }

            val zamanlayici = object : CountDownTimer(pomoSureZaman*60 *1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if(millisUntilFinished/1000>3600){
                        zamanGoster.text = "${millisUntilFinished/1000/60/60}:${millisUntilFinished/1000/60}:${millisUntilFinished/1000%60}"
                    }
                    else if(millisUntilFinished/1000>60){
                        zamanGoster.text = "${millisUntilFinished/1000/60}:${(millisUntilFinished/1000)%60}"
                    }
                    else {
                        zamanGoster.text = "${millisUntilFinished/1000}"
                    }

                }

                override fun onFinish() {
                    Toast.makeText(applicationContext, "Süre Tamamlandı.", Toast.LENGTH_LONG).show()
                    butonlarigetir()
                }
            }
            zamanlayici.start()
            Toast.makeText(applicationContext, "Süre Başladı.", Toast.LENGTH_LONG).show()
        }

        ayarlarButonu.setOnClickListener {
            val intent = Intent(this, PomodoroAyar::class.java)
            startActivity(intent)
        }
    }
}