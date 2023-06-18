package com.example.gunodiaryapp


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmet.takvim.extensions.toSimpleDate
import com.example.gunodiaryapp.databinding.NotEkleBinding

import java.util.*

class Not_Ekle : AppCompatActivity() {

    // NotEkleBinding tipinde bir nesne oluşturuyoruz ve
    // ileride layout dosyasındaki elemanlara erişmek için kullanacağız
    private lateinit var binding: NotEkleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // NotEkleBinding nesnesine, layout dosyasındaki elemanlara erişmek için
        // inflate edilmiş layout'un root öğesi atanıyor
        binding = NotEkleBinding.inflate(layoutInflater)

        // Aktivitenin görünümünü, inflate edilmiş layout'a göre ayarlıyoruz
        setContentView(binding.root)

        // Intent'ten alınan takvim nesnesinin alınması
        val calendar = intent.getSerializableExtra(Takvim.CALENDAR_EXTRA) as Calendar?

        // Toolbar altbaşlığına takvim nesnesinin tarihini ekliyoruz
        binding.toolbar.subtitle = calendar?.time?.toSimpleDate()

        // Kaydet butonuna tıklandığında çalışacak kod bloğu
        binding.kaydetButon.setOnClickListener {

            // Notun edittext'ten alınması
            val note = binding.noteEditText.text.toString()

            // Not boş değilse intent'e takvim nesnesi ve not ekleniyor
            if (note.isNotEmpty()) {
                val returnIntent = Intent()
                returnIntent.putExtra(Takvim.CALENDAR_EXTRA, calendar)
                returnIntent.putExtra(Takvim.NOTE_EXTRA, note)

                // İşlem sonucu başarılı olduğunda çalışacak kod bloğu
                setResult(RESULT_OK, returnIntent)
            }
            finish()
        }
    }
}