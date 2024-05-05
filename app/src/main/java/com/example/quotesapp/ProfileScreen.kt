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
import com.google.firebase.firestore.Query
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

class ProfileScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var dobTextView: TextView
    private lateinit var updatedDateTextView: TextView

    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!
        database = FirebaseDatabase.getInstance("https://quotesapp-5a307-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(currentUser.uid)


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


            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            firstNameTextView.text = it.firstName
                            lastNameTextView.text = it.lastName
                            genderTextView.text = it.gender
                            dobTextView.text = it.dob
                            updatedDateTextView.text = it.updatedDate
                        }
                    } else {
                        Toast.makeText(this@ProfileScreen, "Profile not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProfileScreen, "Failed to fetch profile data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}








