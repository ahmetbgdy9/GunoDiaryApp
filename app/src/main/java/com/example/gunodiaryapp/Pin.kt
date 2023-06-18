package com.example.gunodiaryapp

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Pin : AppCompatActivity() {

    private  lateinit var  pin: EditText
    private  lateinit var  tekrarpin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        pin = findViewById(R.id.pinPin)
        tekrarpin = findViewById(R.id.pinPinTekrar)

    }
}