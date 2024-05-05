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
import com.example.quotesapp.databinding.ActivityProfileScreenBinding
import com.example.quotesapp.databinding.ActivityEditProfileScreenBinding
import android.widget.Toast
import com.google.firebase.firestore.Query
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.auth.ktx.auth
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
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

        fetchLoggedInUserData()

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


//            database.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        val user = snapshot.getValue(User::class.java)
//                        user?.let {
//                            firstNameTextView.text = it.firstName
//                            lastNameTextView.text = it.lastName
//                            genderTextView.text = it.gender
//                            dobTextView.text = it.dob
//                            updatedDateTextView.text = it.updatedDate
//                        }
//                    } else {
//                        Toast.makeText(this@ProfileScreen, "Profile not found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(this@ProfileScreen, "Failed to fetch profile data", Toast.LENGTH_SHORT).show()
//                }
//            })
        }
    }


    private fun fetchLoggedInUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        println(userId)
        if (userId != null) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val ref = userId?.let {
                FirebaseDatabase.getInstance("https://quotesapp-5a307-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(
                    it
                )
            }

            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(EditProfileScreen.User::class.java)
                    user?.let {
                                firstNameTextView.text = it.firstName
                                lastNameTextView.text = it.lastName
                                genderTextView.text = it.gender
                                dobTextView.text = it.dob
                                updatedDateTextView.text = it.updatedDate
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(applicationContext, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

}











