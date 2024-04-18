package com.example.quotesapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.net.ConnectivityManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gotoDashboardScreen = findViewById<Button>(R.id.button)
        gotoDashboardScreen.setOnClickListener {
            if (isNetworkAvailable()) {
                val intent = Intent(applicationContext, DashboardScreen::class.java)
                startActivity(intent)
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
}