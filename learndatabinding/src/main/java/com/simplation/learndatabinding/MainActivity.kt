package com.simplation.learndatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvAuthor = findViewById<TextView>(R.id.tvAuthor)
        val tvRating = findViewById<TextView>(R.id.tvRating)

        val book = Book("Android 进阶之光", "刘望舒", "5", 5)

        tvTitle.text = book.title
        tvAuthor.text = book.author
        tvRating.text = book.rating.toString()
    }
}