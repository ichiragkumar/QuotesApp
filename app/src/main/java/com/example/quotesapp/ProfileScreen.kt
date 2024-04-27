package com.example.quotesapp

import android.content.Intent
import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var dobTextView: TextView
    private lateinit var updatedDateTextView: TextView

    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database reference
        database =
            FirebaseDatabase.getInstance("https://quotesapp-5a307-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        currentUser = auth.currentUser!!
        fetchUserData()

        firstNameTextView = findViewById(R.id.textView2)
        lastNameTextView = findViewById(R.id.textView3)
        genderTextView = findViewById(R.id.textView4)
        dobTextView = findViewById(R.id.textView5)
        updatedDateTextView = findViewById(R.id.textView6)


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
        val gotoEditScreen = findViewById<ImageButton>(R.id.imageButton4)
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

        }
    }

    private fun fetchUserData() {
        val userId = currentUser.uid
        database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    firstNameTextView.text = it.firstName
                    lastNameTextView.text = it.lastName
                    genderTextView.text = it.gender
                    dobTextView.text = it.dob
                    updatedDateTextView.text = it.updatedDate
                    // Show success message
                    Toast.makeText(applicationContext, "Data fetched successfully", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(applicationContext, "Failed to fetch data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}


