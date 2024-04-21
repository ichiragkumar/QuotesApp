package com.example.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class ProfileScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        auth = FirebaseAuth.getInstance()


        val gotoallQuotes = findViewById<ImageButton>(R.id.imageButton)
        gotoallQuotes.setOnClickListener {
            val intent = Intent(applicationContext, AllQuotesScreen::class.java)
            startActivity(intent)
        }
        val gotoHomeScreen = findViewById<ImageButton>(R.id.imageButton2)
        gotoHomeScreen.setOnClickListener {
            val intent = Intent(applicationContext, HomeScreen::class.java)
            startActivity(intent)
        }
        val  gotoEditScreen = findViewById<ImageButton>(R.id.imageButton4)
        gotoEditScreen.setOnClickListener {
            val intent = Intent(applicationContext, EditProfileScreen::class.java)
            startActivity(intent)
        }
        // Check if user is logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, show logout button
            val logoutButton = findViewById<Button>(R.id.logoutButton)
            logoutButton.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            // User is not logged in, do something else if needed
        }



    }
}