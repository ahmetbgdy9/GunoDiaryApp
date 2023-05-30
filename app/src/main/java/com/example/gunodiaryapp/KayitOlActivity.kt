package com.example.gunodiaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class KayitOlActivity : AppCompatActivity() {

    //veritabanı bağlantıları
    private lateinit var auth: FirebaseAuth
    var databaseReference:DatabaseReference?=null//başlangıç değeri boş olabilir
    var database: FirebaseDatabase?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayitol)
        val myTextView = findViewById<TextView>(R.id.kayitolGunoyazi)//textyazımızı buluyor
        val kullaniciadiYazi = findViewById<EditText>(R.id.kayitolAdSoyad)//kullanıcı textini buluyor
        val epostaYazi = findViewById<EditText>(R.id.sifremiunuttumEposta)// girişdeki parola edittextini buluyoruz
        val parolaYazi = findViewById<EditText>(R.id.kayitolParola)//girisdeki giriş butonunu buluyoruz
        val parolaYaziOnay = findViewById<EditText>(R.id.kayitolParolaOnay)//girisdeki kayit ol butonunu buluyoruz
        val kayitolbuton = findViewById<Button>(R.id.btnkayitolKaydet)
        val myAnimation = AnimationUtils.loadAnimation(this, R.anim.sagdansola)//sağdan sola kaydırır
        //hepsi sağdan sola kaydırıyor
        myTextView.startAnimation(myAnimation)//animasyonu başlatır
        kullaniciadiYazi.startAnimation(myAnimation)
        epostaYazi.startAnimation(myAnimation)
        parolaYazi.startAnimation(myAnimation)
        parolaYaziOnay.startAnimation(myAnimation)
        kayitolbuton.startAnimation(myAnimation)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Kullanici")

        //kaydet butonuyla kaydetme adimlari

        kayitolbuton.setOnClickListener {
            val uyari = Toast.makeText(applicationContext, "Alanları Eksiksiz Doldurun Lütfen", Toast.LENGTH_SHORT)

            val kullaniciadi = kullaniciadiYazi.text.toString()
            val eposta = epostaYazi.text.toString()
            val parola = parolaYazi.text.toString()
            if (kullaniciadiYazi.text.toString().isEmpty()){
                uyari.show()
                return@setOnClickListener
            }
            else if(epostaYazi.text.toString().isEmpty()){
                uyari.show()
                return@setOnClickListener
            }
            else if(parolaYazi.text.toString().isEmpty()){
                uyari.show()
                return@setOnClickListener
            }
            else if(parolaYaziOnay.text.toString().isEmpty()){
                uyari.show()
                return@setOnClickListener
            }
            else if (parolaYazi.text.toString() != parolaYaziOnay.text.toString()) {
                val uyariSifre = Toast.makeText(applicationContext, "Şifreler Eşleşmiyor", Toast.LENGTH_SHORT)
                uyariSifre.show()
                return@setOnClickListener
            }
            else if (parolaYazi.text.toString().count()<6) {
                val uyariSifre2 = Toast.makeText(applicationContext, "Şifreniz 6 haneden küçük olamaz", Toast.LENGTH_SHORT)
                uyariSifre2.show()
                return@setOnClickListener
            }
            //verileri ekliyoruz hataları kontrol ediyoruz hata mesajlarını veriyoruz duruma göre
            auth.createUserWithEmailAndPassword(eposta, parola)
                .addOnCompleteListener(this) {  task ->
                    if (task.isSuccessful) {
                        // Kullanıcı başarıyla kaydedildi, veritabanına kaydedelim
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            val currentUserDb = databaseReference?.child(currentUser.uid)
                            currentUserDb?.child("AdiSoyadi")?.setValue(kullaniciadi)
                            Toast.makeText(this,"Kayıt Başarılı",Toast.LENGTH_LONG).show()
                            val intent = Intent(this, GirisActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this,"Kayıt Hatalı",Toast.LENGTH_LONG).show()
                        }
                    }//
                    else {
                        when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> Toast.makeText(this,"Lütfen geçerli bir mail adresi giriniz",Toast.LENGTH_LONG).show()
                            is FirebaseAuthUserCollisionException -> Toast.makeText(this,"Bu e-posta adresi zaten kayıtlı",Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this,"Kayıt Hatalı: ${task.exception?.message}",Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
}