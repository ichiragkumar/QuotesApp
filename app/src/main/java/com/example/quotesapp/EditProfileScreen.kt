package com.example.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.app.DatePickerDialog
import android.icu.util.Calendar
import com.google.firebase.database.FirebaseDatabase

class EditProfileScreen : AppCompatActivity() {
    private lateinit var dobTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)

        // Find the views
        val gotoallQuotes = findViewById<ImageButton>(R.id.imageButton)
        val gotoHomeScreen = findViewById<ImageButton>(R.id.imageButton2)
        val gotoProfileScreen = findViewById<ImageButton>(R.id.imageButton3)
        val genderSpinner: Spinner = findViewById(R.id.genderSpinner)
        val dobButton: Button = findViewById(R.id.dobButton)
        val submitButton: Button = findViewById(R.id.submitButton)
        dobTextView = findViewById(R.id.dobTextView)

        // Set click listeners
        gotoallQuotes.setOnClickListener {
            startActivity(Intent(applicationContext, AllQuotesScreen::class.java))
        }
        gotoHomeScreen.setOnClickListener {
            startActivity(Intent(applicationContext, HomeScreen::class.java))
        }
        gotoProfileScreen.setOnClickListener {
            startActivity(Intent(applicationContext, ProfileScreen::class.java))
        }
        dobButton.setOnClickListener {
            showDatePickerDialog()
        }
        submitButton.setOnClickListener {
            saveProfileDetails()
        }

        // Initialize gender spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.genders_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dobTextView.text = selectedDate
            },
            year,
            month,
            day
        )
        // Set max date to today to prevent selecting future dates
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun saveProfileDetails() {
        val firstName = findViewById<EditText>(R.id.firstNameEditText).text.toString()
        val lastName = findViewById<EditText>(R.id.lastNameEditText).text.toString()
        val gender = findViewById<Spinner>(R.id.genderSpinner).selectedItem.toString()
        val dob = dobTextView.text.toString()

        // Save details to Firebase Realtime Database
        val database = FirebaseDatabase.getInstance("https://quotesapp-5a307-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        val user = HashMap<String, String>()
        user["firstName"] = firstName
        user["lastName"] = lastName
        user["gender"] = gender
        user["dob"] = dob

        database.push().setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile details saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save profile details", Toast.LENGTH_SHORT).show()
            }
    }
}
