package com.example.gunodiaryapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GorevlerimDetay : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null//başlangıç değeri boş olabilir
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gorevlerim_detay)

        var intent = intent
        var baslik = intent.getStringExtra("putbaslik")
        var aciklama = intent.getStringExtra("putaciklama")
        var tarih = intent.getStringExtra("puttarih")
        var veritabaniTarih = intent.getStringExtra("putveritabanitarih")
        var veritabaniGun = intent.getStringExtra("putveritabanigun")
        var veritabaniAy = intent.getStringExtra("putveritabaniay")
        var veritabaniYil = intent.getStringExtra("putveritabaniyil")
        var basliktext = findViewById<TextView>(R.id.detayTextBaslik)
        var aciklamatext = findViewById<TextView>(R.id.detayTextAciklama)
        var tarihtext = findViewById<TextView>(R.id.detayTextTarih)
        val silButon = findViewById<Button>(R.id.detaySilButon)
        val duzenleButon= findViewById<Button>(R.id.detayDuzenle)

        basliktext.text = baslik.toString()
        aciklamatext.text = aciklama.toString()
        tarihtext.text=tarih.toString()



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        var suankiKullaniciID = auth.currentUser!!.uid
        databaseReference = database?.reference!!.child("Kullanici").child(suankiKullaniciID).child("Gorevler").child(veritabaniTarih.toString())
        silButon.setOnClickListener {
            databaseReference!!.child(baslik.toString()).removeValue()
            Toast.makeText(this@GorevlerimDetay,"Silme Başarılı", Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext,GorevListesi::class.java)
            startActivity(intent)
            finish()
        }
        duzenleButon.setOnClickListener {
            var duzenleIntent =Intent(applicationContext,GorevListesiYeniGorevEkle::class.java)
            duzenleIntent.putExtra("putbaslik",baslik)
            duzenleIntent.putExtra("putaciklama",aciklama)
            duzenleIntent.putExtra("puttarih",tarih)
            duzenleIntent.putExtra("putveritabanigun",veritabaniGun)
            duzenleIntent.putExtra("putveritabaniay",veritabaniAy)
            duzenleIntent.putExtra("putveritabaniyil",veritabaniYil)
            duzenleIntent.putExtra("Duzenle","true")
            startActivity(duzenleIntent)
        }
    }

    /*hesabımı sil
        * currentUser?.delete()?.addOnCompleteListener{
        *       if(it.isSuccseful){
        *           "Silindi Mesajı"
        *             auth.singOut()
        * v                 ar currentUserDb = databaseReference?.child(currentUser.uid)
                            currentUserDb?.removeValue()
        *              "giriş sayfasına gönder"}
        *           }
        * */
}