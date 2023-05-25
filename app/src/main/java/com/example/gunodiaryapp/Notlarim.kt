package com.example.gunodiaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.ahmet.takvim.extensions.toSimpleDate
import com.example.gunodiaryapp.databinding.NotlarimBinding

import java.util.*

class Notlarim : AppCompatActivity() {
    // NotlarimBinding tipinde değişken tanımlanır ve ileride inflate edilecek.
    private lateinit var binding: NotlarimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // NotlarimBinding sınıfı inflate edilir ve içerik görüntülenir.
        binding = NotlarimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İntent ile gönderilen takvim bilgisi alınır ve ilgili alana yazılır.

        val takvimm = intent.getSerializableExtra(Takvim.CALENDAR_EXTRA) as Calendar
        binding.toolbar.subtitle = takvimm.time.toSimpleDate()

        // İntent ile gönderilen not bilgisi alınır ve eğer varsa ilgili alanda görüntülenir.
        val not = intent.getStringExtra(Takvim.NOTE_EXTRA)
        if (not != null) {
            binding.noteTextView.text = not
            binding.emptyStateTextView.isVisible = false
        }
    }
}
