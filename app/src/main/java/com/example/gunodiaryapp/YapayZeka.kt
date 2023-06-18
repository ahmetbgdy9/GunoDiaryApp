package com.example.gunodiaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class YapayZeka : AppCompatActivity() {

    private val alici = OkHttpClient()
    lateinit var txtCevap: TextView
    lateinit var soru: TextView
    lateinit var etSoru: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yapay_zeka)
        etSoru=findViewById(R.id.etxtSoru)
        soru=findViewById(R.id.idSoru)
        txtCevap=findViewById(R.id.txtCevap)

        etSoru.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                // setting response tv on below line.
                txtCevap.text = "Lütfen Bekleyiniz.."

                // validating text
                val soru = etSoru.text.toString().trim()
                Toast.makeText(this,soru, Toast.LENGTH_SHORT).show()
                if(soru.isNotEmpty()){
                    cevapAl(soru) { cevap ->
                        runOnUiThread {
                            txtCevap.text = cevap
                        }
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })
    }
    fun cevapAl(soru: String, callback: (String) -> Unit){

        this.soru.text = soru
        etSoru.setText("")

        val apiKey="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"
        val url="https://api.openai.com/v1/engines/text-davinci-003/completions"

        val requestBody="""
            {
            "prompt": "$soru",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()

        val talep = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        alici.newCall(talep).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error","API failed",e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body=response.body?.string()
                if (body != null) {
                    Log.v("data",body)
                }
                else{
                    Log.v("data","empty")
                }
                val jsonObject= JSONObject(body)
                val jsonArray: JSONArray =jsonObject.getJSONArray("choices")
                val textResult=jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }
        })
    }
}