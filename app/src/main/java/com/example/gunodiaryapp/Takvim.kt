package com.example.gunodiaryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmet.takvim.extensions.isaretle
import com.ahmet.takvim.extensions.toSimpleDate
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.applandeo.materialcalendarview.utils.isToday
import com.example.gunodiaryapp.databinding.TakvimBinding
import com.example.gunodiaryapp.Not_Ekle
import com.example.gunodiaryapp.Notlarim
import com.example.gunodiaryapp.R
import java.util.*
import kotlin.time.Duration.Companion.days

class Takvim : AppCompatActivity(), OnDayClickListener, OnSelectDateListener {

    private lateinit var binding: TakvimBinding

    // notların günlerle eşleştirilerek tutulduğu mutableMap
    private val notlar = mutableMapOf<EventDay, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TakvimBinding'i inflate et
        binding = TakvimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ekle Butonuna click listener ekle, datePicker'ı açar
        binding.ekleButon.setOnClickListener { tarihSecici() }

        // Takvimin herhangi bir gününe tıklandığında çağrılacak metodun listener'ı
        binding.takvimView.setOnDayClickListener(this)
    }



    // Takvimde bir güne tıklandığında çağrılan metod
    override fun onDayClick(eventDay: EventDay) {
        // Notlarim activity'sine gidilecek intent
        val intent = Intent(this, Notlarim::class.java)
        val calendar = eventDay.calendar
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        intent.putExtra("yil", year)
        intent.putExtra("ay", month)
        intent.putExtra("gun", dayOfMonth)

        // Seçilen güne ait takvimi intent'e ekle
        intent.putExtra(CALENDAR_EXTRA, eventDay.calendar)
        // Seçilen güne ait notu intent'e ekle
        intent.putExtra(NOTE_EXTRA, notlar[eventDay])
        // Notlarim activity'sine git
        startActivity(intent)
    }
    // DatePicker'ı açmak için çağrılan metod
    private fun tarihSecici() {
        DatePickerBuilder(this, this)
            .pickerType(CalendarView.ONE_DAY_PICKER)
            .headerColor(R.color.temaRengi)
            .todayLabelColor(R.color.secondary)
            .selectionColor(R.color.secondary_light)
            .dialogButtonsColor(R.color.secondary)
            .build()
            .show()
    }
    // DatePicker'dan seçilen tarihleri aldığında çağrılan metod
    override fun onSelect(calendar: List<Calendar>) {
        // Not_Ekle activity'sine gidilecek intent
        val intent = Intent(this, Not_Ekle::class.java)
        // Seçilen tarihi intent'e ekle
        intent.putExtra("tarih", calendar.first())
        // Not_Ekle activity'sinden veri almak için Notlarim activity'si ile startActivityForResult kullanılır
        startActivityForResult(intent, RESULT_CODE)
    }

    // Not_Ekle activity'sinden veri alındığında çağrılan metod
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Eğer veri alınabildiyse ve istenilen kod ile geldiyse çalışır
        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            // Notu al, eğer boşsa return ile metodun sonlandırılmasını sağla
            val not = data?.getStringExtra(NOTE_EXTRA) ?: return
            // Seçilen tarihi al
            val calendar = data.getSerializableExtra(CALENDAR_EXTRA) as Calendar
            // Seçilen tarihe notu eşleştir ve map'e ekle
            val seciliGun = EventDay(calendar, applicationContext.isaretle())
            // Oluşturulan notu notes adlı mutableMap'e kaydetme
            notlar[seciliGun] = not
            // Takvimdeki etkinlikleri güncelleme
            binding.takvimView.setEvents(notlar.keys.toList())
        }
    }
    // Bu sabitler, aktiviteler arasında veri alışverişi yaparken kullanılan Intent nesnelerinde kullanılır.
    // const anahtar kelimesi ile tanımlanan bu sabitlerin değeri değiştirilemez.
    companion object {
        const val CALENDAR_EXTRA = "calendar"
        const val NOTE_EXTRA = "note"
        const val RESULT_CODE = 8
    }
}