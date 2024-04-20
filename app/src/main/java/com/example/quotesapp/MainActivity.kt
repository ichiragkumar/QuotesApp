package com.example.quotesapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Check if user is already authenticated
        if (auth.currentUser != null) {
            // User is already logged in, navigate to DashboardScreen
            navigateToDashboardScreen()
        }

        val gotoDashboardScreen = findViewById<Button>(R.id.button)
        gotoDashboardScreen.setOnClickListener {
            if (isNetworkAvailable()) {
                // Check if user is already authenticated
                if (auth.currentUser != null) {
                    // User is already logged in, navigate to DashboardScreen
                    navigateToDashboardScreen()
                } else {
                    // User is not logged in, navigate to SignInScreen
                    val intent = Intent(applicationContext, DashboardScreen::class.java)
                    startActivity(intent)
                }
            } else {
                val intent = Intent(applicationContext, OfflineScreen::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun navigateToDashboardScreen() {
        val intent = Intent(applicationContext, HomeScreen::class.java)
        startActivity(intent)
        finish() // Finish the MainActivity so that the user can't go back to it after logging in
    }
}
