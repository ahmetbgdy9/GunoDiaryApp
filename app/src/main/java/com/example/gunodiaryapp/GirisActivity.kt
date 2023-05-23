package com.example.gunodiaryapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth


class GirisActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)
        //veritabanı izin alıyoruz
        auth = FirebaseAuth.getInstance()

        //ANİMASYONLAR
        val anasayfaGunoYazi = findViewById<TextView>(R.id.girisGunoYazi)//textyazımızı buluyor
        val yukariAsagi = AnimationUtils.loadAnimation(this, R.anim.yukaridanasagi)//yukarıdan asagi kaydırır
        anasayfaGunoYazi.startAnimation(yukariAsagi)//animasyonu başlatır

        val girisEposta = findViewById<EditText>(R.id.girisEposta)//kullanıcı textini buluyor
        val soldanSaga = AnimationUtils.loadAnimation(this, R.anim.soldansaga)//kullanici textini kaydıran animasyonu yüklüyor
        girisEposta.startAnimation(soldanSaga)//soldan saga kaydiran animasyon

        val girisParola = findViewById<EditText>(R.id.girisParola)// girişdeki parola edittextini buluyoruz
        val sagdanSola = AnimationUtils.loadAnimation(this, R.anim.sagdansola)
        girisParola.startAnimation(sagdanSola)//sağdan sola kaydıran animasyonu cagirdik

        val girisGirisYap = findViewById<Button>(R.id.btngirisGirisYap)//girisdeki giriş butonunu buluyoruz
        val asagidanYukari = AnimationUtils.loadAnimation(this, R.anim.asagidanyukari)//animasyonumuzu yüklüyoruz
        girisGirisYap.startAnimation(asagidanYukari)//asagidan yukarı kaydiran animasyon

        val girisKayitOl = findViewById<Button>(R.id.btngiriskayitol)//girisdeki kayit ol butonunu buluyoruz
        girisKayitOl.startAnimation(asagidanYukari)//asagidan yukarı kaydıran animasyon

        val girisSifremiUnuttum = findViewById<Button>(R.id.btngirisSifremiUnuttum)//sifremiunuttum butonunu bul
        girisSifremiUnuttum.startAnimation(asagidanYukari)//asagidanyukari



        val epostakontrol = findViewById<CheckBox>(R.id.chbEpostaHatirla)
        //epostamı hatırla seçeneğini tutmak için shared preferences kullanıyoruz
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        epostakontrol.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("remember_email", isChecked).apply()
        }
        val rememberEmail = sharedPreferences.getBoolean("remember_email", false)


        val currentUser = auth.currentUser
        if (rememberEmail) {
            if(currentUser!=null) {
                epostakontrol.isChecked = true
                val kullaniciEmail = currentUser.email
                if(!kullaniciEmail.isNullOrEmpty()) {
                    girisEposta.setText(kullaniciEmail)
                }
            }
        }



        //giriş yap butonuna tıklandığında
        //giriş bilgileri doğrulayıp giriş yap
        girisGirisYap.setOnClickListener {
            val bgirisEposta = girisEposta.text.toString()
            val bgirisParola = girisParola.text.toString()
            if(bgirisEposta.isEmpty() || bgirisParola.isEmpty()) {
                val uyari = Toast.makeText(applicationContext, "Lütfen boş alan bırakmayınız.", Toast.LENGTH_SHORT)
                uyari.show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(bgirisEposta,bgirisParola)
                .addOnCompleteListener(this) {
                    if(it.isSuccessful){
                        intent = Intent(applicationContext,AnaMenu::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(applicationContext, "Giriş Hatalı, lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        girisKayitOl.setOnClickListener {
            val intent = Intent(this, KayitOlActivity::class.java)
            startActivity(intent)
            finish()
        }
      /*  girisSifremiUnuttum.setOnClickListener{
            val intent = Intent(this, SifremiUnuttum::class.java)
            startActivity(intent)
            finish()
        }*/
        /*val butonGirisYap = findViewById<Button>(R.id.btngirisGirisYap)
        butonGirisYap.setOnClickListener{
            val intent = Intent(this, anaMenu::class.java)
            startActivity(intent)

        }*/
    }
}