package com.complicated.triviaappapis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import java.net.URL
import java.util.Locale
import java.util.concurrent.Executors

class QuestionsLayout : AppCompatActivity() {
    var questionsList = ArrayList<Result>()
    var counter = 0
    var score = 0
    var difficulty = ""
    var category = "" // 9-32
    lateinit var btnTrue: Button
    lateinit var btnFalse: Button
    lateinit var textbox: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questions_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnTrue = findViewById(R.id.btnTrue)
        btnFalse = findViewById(R.id.btnFalse)
        textbox = findViewById(R.id.txtQuestion)

        difficulty = (intent.getStringExtra("diff")!!).lowercase(Locale.ROOT)
        category = intent.getStringExtra("cat")!!

        ReadQuestions()
    }

    fun updateUI() {
        if (questionsList.isNotEmpty()) {
            textbox.text = questionsList[counter].question

            btnTrue.setOnClickListener {
                if (questionsList[counter].correct_answer == "True") {

                    // congrats screen
                    val intent = Intent(this, CongratsScreen::class.java)
                    intent.putExtra("res", "Success")
                    startActivity(intent)
                    score++
                } else {
                    val intent = Intent(this, CongratsScreen::class.java)
                    intent.putExtra("res", "Fail")
                    startActivity(intent)

                }
                counter++
                if (counter < questionsList.size) {
                    textbox.text = questionsList[counter].question
                }
                else{
                    finish()
                }
            }

            btnFalse.setOnClickListener {
                if (questionsList[counter].correct_answer == "True") {

                    // congrats screen
                    val intent = Intent(this, CongratsScreen::class.java)
                    intent.putExtra("res", "Success")
                    startActivity(intent)
                    score++
                } else {
                    val intent = Intent(this, CongratsScreen::class.java)
                    intent.putExtra("res", "Fail")
                    startActivity(intent)

                }
                counter++
                if (counter < questionsList.size) {
                    textbox.text = questionsList[counter].question
                    textbox.text = Html.fromHtml(questionsList[counter].question, Html.FROM_HTML_MODE_COMPACT)
                }
                else{
                    finish()
                }
            }
        }
        else{
            Toast.makeText(this, "No questions relating questions available at the moment" , Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun ReadQuestions() {
        val executer = Executors.newSingleThreadExecutor()
        executer.execute {
            val url = URL("https://opentdb.com/api.php?amount=5&category=$category&difficulty=$difficulty&type=boolean")
            Log.d("test1", url.toString())
            val json = url.readText()
            val gson = Gson()
            val rootObject = gson.fromJson(json, Questions::class.java)
            Log.d("test1", rootObject.toString())
            Handler(Looper.getMainLooper()).post {
                questionsList.clear()
                for (question in rootObject.results) {
                    question.question = Html
                        .fromHtml(question.question, Html.FROM_HTML_MODE_COMPACT)
                        .toString()
                    questionsList.add(question)
                }
                updateUI()
            }
        }
    }
}