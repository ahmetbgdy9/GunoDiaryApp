package com.ahmet.takvim.extensions

import java.text.SimpleDateFormat // java.text paketindeki SimpleDateFormat sınıfını kullanabilmek için import ediyoruz.
import java.util.* // java.util paketindeki Locale sınıfını kullanabilmek için import ediyoruz.

// Date sınıfına yeni bir fonksiyon ekliyoruz.
fun Date.toSimpleDate(): String {
    // SimpleDateFormat sınıfının "dd MMMM yyyy" formatındaki örneğini oluşturuyoruz.
    val tarihformati = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    // Verilen tarihi oluşturulan format ile biçimlendiriyoruz ve geri döndürüyoruz.
    return tarihformati.format(this)
}
