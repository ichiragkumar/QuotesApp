package com.example.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AllQuotesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_quotes_screen)



        val gotoHomeScreen = findViewById<ImageButton>(R.id.imageButton2)
        gotoHomeScreen.setOnClickListener {
            val intent = Intent(applicationContext, HomeScreen::class.java)
            startActivity(intent)
        }
        val  gotoProfileScreen = findViewById<ImageButton>(R.id.imageButton3)
        gotoProfileScreen.setOnClickListener {
            val intent = Intent(applicationContext, ProfileScreen::class.java)
            startActivity(intent)
        }



    }
}