package com.example.gunodiaryapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class AnaMenu : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null//başlangıç değeri boş olabilir
    var database: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anamenu)


        // TextView öğesini tanımlayın
        val textView = findViewById<TextView>(R.id.tarih)
// Calendar sınıfını kullanarak bugünkü tarihi alın
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // 0-11 arası olduğu için +1 eklenir
        val day = calendar.get(Calendar.DAY_OF_MONTH)

// Tarihi TextView'a yazdırın
        val todayDate = "$day/$month/$year"
        textView.text = todayDate
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Kullanici")
        val currentUser = auth.currentUser
        val adSoyad = findViewById<TextView>(R.id.anaMenuAdSoyad)

        //realtime databasedeki idye ulaşıp altındaki childların içindeki veriyi isim soyisim yerine aktarıcaz
        val userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adSoyad.text = snapshot.child("AdiSoyadi").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // takvim sayfasına gidiyoruz
        val takvimButon = findViewById<ImageButton>(R.id.btnAnaMenuTakvim)
        takvimButon.setOnClickListener {
            val intent = Intent(applicationContext,Takvim::class.java)
            startActivity(intent)
        }
        val gunlukButon = findViewById<ImageButton>(R.id.btnAnaMenuGunlugum)
        gunlukButon.setOnClickListener {
            val intent = Intent(applicationContext,GunlukActivity::class.java)
            startActivity(intent)
        }
        val gorevButon = findViewById<ImageButton>(R.id.btnAnaMenuGorevListesi)
        gorevButon.setOnClickListener {
            val intent = Intent(applicationContext,GorevListesi::class.java)
            startActivity(intent)
        }
        val pomodoroButon = findViewById<ImageButton>(R.id.btnAnaMenuPomodoro)
        pomodoroButon.setOnClickListener {
            val intent = Intent(applicationContext,Pomodoro::class.java)
            startActivity(intent)
        }
        val ayarlarButon = findViewById<ImageButton>(R.id.btnAnaMenuAyarlar)
        ayarlarButon.setOnClickListener {
            val intent = Intent(applicationContext,Ayarlar::class.java)
            startActivity(intent)
        }
        val yapayZekaButon = findViewById<ImageButton>(R.id.btnYapayzeka)
        yapayZekaButon.setOnClickListener {
            val intent = Intent(applicationContext,YapayZeka::class.java)
            startActivity(intent)
        }
    }
}