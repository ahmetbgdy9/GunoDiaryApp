package com.example.gunodiaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class GunlukActivity : AppCompatActivity() {


    val calendar = Calendar.getInstance()
    val suankiKullaniciID = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().reference//database referansımız

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gunlugum)

        val adSoyad = findViewById<TextView>(R.id.gunlukAdSoyad)
        val tarih = findViewById<TextView>(R.id.gunlukTarih)
        val gunlukYazisi = findViewById<TextView>(R.id.gunlugumYazi)
        val kaydetButonu = findViewById<Button>(R.id.gunlukKaydet)
        val oncekiGun = findViewById<Button>(R.id.gunlukSolGecis)
        val sonrakiGun = findViewById<Button>(R.id.gunlukSagGecis)




        // Calendar sınıfını kullanarak bugünkü tarihi alın
        var oAnkiGun = calendar.get(Calendar.DAY_OF_MONTH)
        var oAnkiAy = (calendar.get(Calendar.MONTH) + 1)
        var oAnkiYil = calendar.get(Calendar.YEAR)
        tarih.text = tarihiGetir(oAnkiGun,oAnkiAy,oAnkiYil)

        //fonksiyona gönderiilip ona göre o ankigünü değiştiririz falan filan

        //realtime databasedeki idye ulaşıp altındaki childların içindeki veriyi isim soyisim yerine aktarıc
        databaseReference.child("Kullanici").child(suankiKullaniciID.toString()).child("AdiSoyadi").get().addOnSuccessListener { adiSoyadiniGetir ->
            adSoyad.text = adiSoyadiniGetir.value.toString()
        }


        //veritabanından o günün günlüğünü çekme
        suankiKullaniciID?.let { kullaniciID ->
            databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).get().addOnSuccessListener { yillar ->
                if (yillar.exists()) {
                    yillar.children.forEach { aylar ->
                        if (aylar.key.toString() == oAnkiAy.toString()) {
                            aylar.children.forEach { gunler ->
                                if (gunler.key.toString() == oAnkiGun.toString()) {
                                    if (gunler.exists()){
                                        gunlukYazisi.text = gunler.value.toString()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        //Veritabanına o günü kaydet
        kaydetButonu.setOnClickListener {
            databaseReference.child("Kullanici").child(suankiKullaniciID.toString()).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).setValue(gunlukYazisi.text.toString())
            Toast.makeText(this,"Kaydetme Başarılı",Toast.LENGTH_LONG).show()
        }

        fun oncekiGun (){
            oAnkiAy--
            suankiKullaniciID?.let { kullaniciID ->
                // Önce verilen günün verilerini çekelim
                databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { kontrol ->
                    if (kontrol.exists()) {
                        // Verilen günün verileri var, bir sonraki güne bakalım
                        databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { sonrakiGun ->
                            if (sonrakiGun.exists()) {
                                tarih.text = tarihiGetir(oAnkiGun,oAnkiAy,oAnkiYil)
                                gunlukYazisi.text = sonrakiGun.value.toString()
                            }
                            else {
                                Toast.makeText(this, "Sonraki güne ait veri yok.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else {
                        Toast.makeText(this, "Günlük veri yok.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        //önceki gün butonu
        oncekiGun.setOnClickListener {
            if((oAnkiAy == 1 ||oAnkiAy == 3 ||oAnkiAy == 5 ||oAnkiAy == 7 ||oAnkiAy == 8 ||oAnkiAy == 10 ||oAnkiAy == 12) && oAnkiGun == 1) {
                oAnkiGun = 30
                oncekiGun()
            }
            else if((oAnkiAy == 4 ||oAnkiAy == 6 ||oAnkiAy == 9 ||oAnkiAy == 11) && oAnkiGun == 1) {
                oAnkiGun = 31
                oncekiGun()
            }
            else if(oAnkiAy == 3 && oAnkiGun == 1){
                oAnkiGun == 28
                oncekiGun()
            }
            else {
                suankiKullaniciID?.let { kullanidiID ->
                    // Önce verilen günün verilerini çekelim
                    databaseReference.child("Kullanici").child(kullanidiID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { kontrol ->
                        if (kontrol.exists()) {
                            // Verilen günün verileri var, bir önceki güne bakalım
                            oAnkiGun--
                            databaseReference.child("Kullanici").child(kullanidiID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { oncekiGun ->
                                if (oncekiGun.exists()) {
                                    tarih.text = tarihiGetir(oAnkiGun,oAnkiAy,oAnkiYil)
                                    gunlukYazisi.text = oncekiGun.value.toString()
                                }
                                else {
                                    oAnkiGun++
                                    Toast.makeText(this, "Önceki güne ait veri yok.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else {
                            Toast.makeText(this, "Güne ait veri yok.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
        fun sonrakiGun(){
            oAnkiAy++
            oAnkiGun = 1
            suankiKullaniciID?.let { kullaniciID ->
                // Önce verilen günün verilerini çekelim
                databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { kontrol ->
                    if (kontrol.exists()) {
                        // Verilen günün verileri var, bir sonraki güne bakalım
                        databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { sonrakiGun ->
                            if (sonrakiGun.exists()) {
                                tarih.text = tarihiGetir(oAnkiGun,oAnkiAy,oAnkiYil)
                                gunlukYazisi.text = sonrakiGun.value.toString()
                            }
                            else {
                                Toast.makeText(this, "Sonraki güne ait veri yok.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else {
                        Toast.makeText(this, "Günlük veri yok.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //sonraki gun butonu
        sonrakiGun.setOnClickListener {
            if((oAnkiAy == 1 ||oAnkiAy == 3 ||oAnkiAy == 5 ||oAnkiAy == 7 ||oAnkiAy == 8 ||oAnkiAy == 10 ||oAnkiAy == 12) && oAnkiGun == 31) {
                sonrakiGun()
            }
            else if((oAnkiAy == 4 ||oAnkiAy == 6 ||oAnkiAy == 9 ||oAnkiAy == 11) && oAnkiGun == 30) {
                sonrakiGun()
            }
            else if(oAnkiAy == 2 && oAnkiGun == 28){
                sonrakiGun()
            }
            else {
                suankiKullaniciID?.let { kullaniciID ->
                    // Önce verilen günün verilerini çekelim
                    databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { kontrol ->
                        if (kontrol.exists()) {
                            // Verilen günün verileri var, bir sonraki güne bakalım
                            oAnkiGun++
                            databaseReference.child("Kullanici").child(kullaniciID).child("Gunluk").child(oAnkiYil.toString()).child(oAnkiAy.toString()).child(oAnkiGun.toString()).get().addOnSuccessListener { sonrakiGun ->
                                if (sonrakiGun.exists()) {
                                    tarih.text = tarihiGetir(oAnkiGun,oAnkiAy,oAnkiYil)
                                    gunlukYazisi.text = sonrakiGun.value.toString()
                                }
                                else {
                                    oAnkiGun--
                                    Toast.makeText(this, "Sonraki güne ait veri yok.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else {
                            Toast.makeText(this, "Günlük veri yok.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun tarihiGetir(gun: Int, ay: Int, yil: Int): String {
        val tarih = "$gun/$ay/$yil"
        return tarih
    }
}