package com.example.gunodiaryapp


import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GorevListesi : AppCompatActivity() {
    private lateinit var tarih: String
    private lateinit var userRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gorev_listesi)

        val calendar = Calendar.getInstance()
        userRecyclerView = findViewById(R.id.recGorevler)
        var veritabaniYil = calendar.get(Calendar.YEAR)
        var veritabaniAy = calendar.get(Calendar.MONTH) + 1
        var veritabaniGun = calendar.get(Calendar.DAY_OF_MONTH)
        tarih = veritabaniGun.toString()+veritabaniAy.toString()+veritabaniYil.toString()

        val gorevEkleyeGit = findViewById<Button>(R.id.gorevEkleSayfaGitButonu)
        gorevEkleyeGit.setOnClickListener {
            val intent = Intent(this,GorevListesiYeniGorevEkle::class.java)
            startActivity(intent)
        }

        val database = FirebaseDatabase.getInstance()
        val suankiKullaniciID = FirebaseAuth.getInstance().currentUser?.uid
        val reference = database.getReference("Kullanici/$suankiKullaniciID/Gorevler/$tarih/")

        data class Gorev( val baslik: String, val aciklama: String)

        class GorevlerAdapter(private val gorevler: List<Gorev>,val context: Context) : RecyclerView.Adapter<GorevlerAdapter.GorevViewHolder>() {

            inner class GorevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val tarihGoster: TextView = itemView.findViewById(R.id.gorevListesiTarihGoster)
                val baslikGoster: TextView = itemView.findViewById(R.id.baslikGoster)
                val aciklamaGoster: TextView = itemView.findViewById(R.id.aciklamaGoster)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GorevViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gorevlerigetir, parent, false)
                return GorevViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: GorevViewHolder, position: Int) {
                val tarih = "$veritabaniGun/$veritabaniAy/$veritabaniYil"
                val baslik = gorevler[position].baslik
                val aciklama = gorevler[position].aciklama

                holder.baslikGoster.text = baslik
                holder.aciklamaGoster.text = aciklama
                holder.tarihGoster.text = tarih

                //seçili pozisyonu alalım
                holder.itemView.setOnClickListener {
                    var intent =Intent(context,GorevlerimDetay::class.java)
                    intent.putExtra("putbaslik",baslik)
                    intent.putExtra("putaciklama",aciklama)
                    intent.putExtra("putveritabanitarih","$veritabaniGun$veritabaniAy$veritabaniYil")
                    intent.putExtra("puttarih",tarih)
                    intent.putExtra("putveritabanigun", veritabaniGun.toString())
                    intent.putExtra("putveritabaniay",veritabaniAy.toString())
                    intent.putExtra("putveritabaniyil",veritabaniYil.toString())
                    context.startActivity(intent)

                }

            }

            override fun getItemCount() = gorevler.size
        }
        userRecyclerView = findViewById(R.id.recGorevler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gorevler = mutableListOf<Gorev>()
                if(dataSnapshot.exists()){
                    for (ds in dataSnapshot.children) {

                        val key = ds.key // örneğin, "gorev1"
                        val value = ds.value // örneğin, "asd"
                        val gorev = Gorev( key.toString(), value as String)
                        gorevler.add(gorev)
                    }
                }


                val adapter = GorevlerAdapter(gorevler,this@GorevListesi)
                userRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata durumunda yapılacaklar
            }
        })


    }
}