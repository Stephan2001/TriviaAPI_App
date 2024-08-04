package com.complicated.triviaappapis

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors

class CongratsScreen : AppCompatActivity() {
    var respVal = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congrats_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val text = findViewById<TextView>(R.id.label3)
        val btn = findViewById<Button>(R.id.btnNext)

        text.text = intent.getStringExtra("res")
        respVal = if (text.text == "Success"){
            "yes"
        } else{
            "no"
        }
        btn.setOnClickListener{
            finish()
        }
        loadIMG2()
    }


    fun loadIMG2(){
        val executer = Executors.newSingleThreadExecutor()
        executer.execute{
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://Yes-or-No-GIF-Image.proxy-production.allthingsdev.co/api")
                .addHeader("x-apihub-key", BuildConfig.API_KEY)
                .addHeader("x-apihub-host", "Yes-or-No-GIF-Image.allthingsdev.co")
                .addHeader("x-apihub-endpoint", "d71a146c-1134-4984-8019-d4a3812f9926")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        Log.d("test2", responseBody)
                        val imgResponse = Gson().fromJson(responseBody, IMGResponse::class.java)
                        Log.d("test2", imgResponse.toString())
                        if (imgResponse.answer != respVal){
                            loadIMG2()
                        }
                        else{
                            val img = findViewById<ImageView>(R.id.imageViewCon)
                            Handler(Looper.getMainLooper()).post {
                                Glide.with(this)
                                    .asGif()
                                    .load(imgResponse.image)
                                    .into(img)
                            }
                        }

                    }
                } else {
                    Log.e("test2", "Request failed with status: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("test2", "Network request failed", e)
            }
        }

    }
}