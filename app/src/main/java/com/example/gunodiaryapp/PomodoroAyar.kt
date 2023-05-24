package com.example.gunodiaryapp


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class PomodoroAyar : AppCompatActivity(), View.OnClickListener {
    private lateinit var uygulaButton: Button
    private lateinit var pomoSure: EditText
    private lateinit var kisaSure: EditText
    private lateinit var uzunSure: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoroayar)

        uygulaButton = findViewById(R.id.button5)
        pomoSure = findViewById(R.id.pomoSure)
        kisaSure = findViewById(R.id.kisaSure)
        uzunSure = findViewById(R.id.uzunSure)

        uygulaButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button5 -> {
                val sharedPref = getSharedPreferences("pomoSureler", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("pomoSure", pomoSure.text.toString())
                editor.putString("kisaSure", kisaSure.text.toString())
                editor.putString("uzunSure", uzunSure.text.toString())
                editor.apply()

                val intent = Intent(this, Pomodoro::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}