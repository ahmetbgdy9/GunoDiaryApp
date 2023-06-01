package com.example.gunodiaryapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SifremiUnuttum : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifremiunuttum)
        auth = FirebaseAuth.getInstance()//erişim izni aldık

        val label = findViewById<TextView>(R.id.sifremiUnuttumGuno)
        val anim = AnimationUtils.loadAnimation(this, R.anim.sagdansola)//logoyu kaydıracak yukarıdan asagi animasyonu yüklüyor
        label.startAnimation(anim)//yukarıdan asagı kaydıran animasyon

        val textView = findViewById<TextView>(R.id.sifreDegisEposta)//girisdeki giriş butonunu buluyoruz
        textView.startAnimation(anim)//asagidan yukarı kaydiran animasyon

        val button1 = findViewById<Button>(R.id.btnPinUygula)//girisdeki giriş butonunu buluyoruz
        button1.startAnimation(anim)//asagidan yukarı kaydiran animasyon


        button1.setOnClickListener {
            var emailsifirla = findViewById<TextView>(R.id.sifreDegisEposta).text.toString().trim()
            if(TextUtils.isEmpty(emailsifirla)) {
                Toast.makeText(applicationContext, "E-Posta adresinizi Yazınız.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                auth.sendPasswordResetEmail(emailsifirla)
                    .addOnCompleteListener(this){   sifirlama ->
                        if(sifirlama.isSuccessful){
                            Toast.makeText(applicationContext, "E-Postanızı Kontrol ediniz", Toast.LENGTH_SHORT).show()

                        }
                        else {
                            Toast.makeText(applicationContext, "Sıfırlama işlemi başarısız", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
            val intent = Intent(applicationContext,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}