package com.example.gunodiaryapp


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class Pomodoro : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        val sharedPref = getSharedPreferences("pomoSureler", Context.MODE_PRIVATE)

        val pomodoroSureSecimButonu = findViewById<Button>(R.id.button1)
        val kisaSureSecimButonu = findViewById<Button>(R.id.button2)
        val uzunSureSecimButonu = findViewById<Button>(R.id.button3)
        val baslaButonu = findViewById<Button>(R.id.button6)
        val ayarlarButonu = findViewById<ImageButton>(R.id.settings_button)
        val zamanGoster = findViewById<TextView>(R.id.timer_textview)

        var pomoSureZaman: Long

        fun butonlariGizle() {
            pomodoroSureSecimButonu.visibility = View.GONE
            kisaSureSecimButonu.visibility = View.GONE
            uzunSureSecimButonu.visibility = View.GONE
            baslaButonu.visibility = View.GONE
            ayarlarButonu.visibility = View.GONE
        }

        fun butonlariGoster() {
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
            butonlariGizle()
            pomoSureZaman = when {
                pomodoroSureSecimButonu.background.constantState == ContextCompat.getDrawable(
                    this,
                    R.drawable.bgbuton2
                )?.constantState -> {
                    sharedPref.getString("pomoSure", null)?.toLongOrNull() ?: 0
                }
                kisaSureSecimButonu.background.constantState == ContextCompat.getDrawable(
                    this,
                    R.drawable.bgbuton2
                )?.constantState -> {
                    sharedPref.getString("kisaSure", null)?.toLongOrNull() ?: 0
                }
                uzunSureSecimButonu.background.constantState == ContextCompat.getDrawable(
                    this,
                    R.drawable.bgbuton2
                )?.constantState -> {
                    sharedPref.getString("uzunSure", null)?.toLongOrNull() ?: 0
                }
                else -> {
                    Toast.makeText(
                        applicationContext,
                        "Lütfen bir süre seçiniz.",
                        Toast.LENGTH_LONG
                    ).show()
                    butonlariGoster()
                    return@setOnClickListener
                }
            }

            val zamanlayici = object : CountDownTimer(pomoSureZaman * 60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / 1000 / 60 / 60
                    val minutes = (millisUntilFinished / 1000 / 60) % 60
                    val seconds = (millisUntilFinished / 1000) % 60

                    zamanGoster.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }

                override fun onFinish() {
                    Toast.makeText(applicationContext, "Süre Tamamlandı.", Toast.LENGTH_LONG)
                        .show()
                    butonlariGoster()
                    showNotification()
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
    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "pomodoro_channel"
            val channelName = "Pomodoro Bildirimleri"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "pomodoro_channel")
            .setSmallIcon(R.drawable.bildirim)
            .setContentTitle("Pomodoro Süresi Tamamlandı")
            .setContentText("Mola vermenin zamanı geldi.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000))

        val notification = builder.build()
        notificationManager.notify(1, notification)
    }
}