package com.complicated.triviaappapis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // selected values
        var selectedDiff = ""
        var selectedCat = 0
        val categoryValues = arrayOf(9,10,11,
            12,13,14,
            15,16,17,
            18,19,20,
            21,22,23,
            24,25,26,
            27,28,29,
            30,31,32,)
        // difficulty
        val difficulty = arrayOf("Hard", "Medium", "Easy")
        val autoComplete : AutoCompleteTextView = findViewById(R.id.select_difficulty)
        val adapter = ArrayAdapter(this, R.layout.list_items, difficulty)
        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

            selectedDiff = difficulty[i]
        }
        // category
        val category = arrayOf("General knowledge", "Books", "Film",
            "Music", "Musicals and theatre", "Television",
            "Video games", "Board games", "Science and nature",
            "Computers", "Mathematics", "Mythology",
            "Sports", "Geography", "History",
            "Politics", "Art", "Celebrities",
            "Animals", "Vehicles", "Comics",
            "Gadgets", "Anime and manga", "Cartoons and animations")

        val autoComplete2 : AutoCompleteTextView = findViewById(R.id.select_category)
        val adapter2 = ArrayAdapter(this, R.layout.list_items, category)
        autoComplete2.setAdapter(adapter2)

        autoComplete2.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            selectedCat = categoryValues[i]
        }

        val btn = findViewById<Button>(R.id.btnGenerate)
        btn.setOnClickListener{
            Log.d("tag1", "diff " + selectedDiff + "    cat " + selectedCat)
            if (selectedDiff == "" || selectedCat == 0){
                Toast.makeText(this, "Select a difficulty and category" , Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, QuestionsLayout::class.java)
                intent.putExtra("diff", selectedDiff)
                intent.putExtra("cat", selectedCat.toString())
                startActivity(intent)
            }
        }

        }
    }

