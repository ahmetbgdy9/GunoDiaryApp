package com.example.gunodiaryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SifreDegistir : AppCompatActivity() {

    private lateinit var eposta: EditText
    private lateinit var mevcutSifre: EditText
    private lateinit var yeniSifre: EditText
    private lateinit var yeniSifreTekrar: EditText
    private lateinit var uygula: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sifre_degistir)

        eposta = findViewById(R.id.sifirlaEposta)
        mevcutSifre = findViewById(R.id.sifirlaMevcutSifre)
        yeniSifre = findViewById(R.id.sifirlaYeniSifre)
        yeniSifreTekrar = findViewById(R.id.sifirlaYeniSifreTekrar)
        uygula = findViewById(R.id.sifirlaBtnUygula)

        uygula.setOnClickListener {
            val eposta2 = eposta.text.toString()
            val mevcutSifre2 = mevcutSifre.text.toString()
            val yeniSifre2 = yeniSifre.text.toString()
            val yeniSifreTekrar2 = yeniSifreTekrar.text.toString()

            if (eposta2.isNotEmpty() && mevcutSifre2.isNotEmpty() && yeniSifre2.isNotEmpty() && yeniSifreTekrar2.isNotEmpty()) {
                if (yeniSifre2 == yeniSifreTekrar2) {
                    // Firebase Authentication nesnesi al
                    val auth = FirebaseAuth.getInstance()


                    // Kullanıcının mevcut şifresi ile giriş yapmasını sağla
                    auth.signInWithEmailAndPassword(eposta2, mevcutSifre2).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Giriş başarılı ise, yeni şifreyi güncelle
                            auth.currentUser?.updatePassword(yeniSifre2)?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Şifre güncelleme başarılı ise, kullanıcıya bildir
                                    Toast.makeText(this, "Şifreniz başarıyla güncellendi.", Toast.LENGTH_LONG).show()
                                } else {
                                    // Şifre güncelleme başarısız ise, hatayı kullanıcıya bildir
                                    Toast.makeText(this, "Şifre güncelleme hatası: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            // Giriş başarısız ise, hatayı kullanıcıya bildir
                            Toast.makeText(this, "Giriş hatası: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    // Yeni şifreler eşleşmiyorsa, kullanıcıya bildir
                    Toast.makeText(this, "Yeni şifreler eşleşmiyor.", Toast.LENGTH_LONG).show()
                }
            } else {
                // Bilgilerden biri boşsa, kullanıcıya bildir
                Toast.makeText(this, "Lütfen tüm bilgileri giriniz.", Toast.LENGTH_LONG).show()
            }
        }
    }
}