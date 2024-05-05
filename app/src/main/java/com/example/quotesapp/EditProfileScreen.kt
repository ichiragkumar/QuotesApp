    package com.example.quotesapp

    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.ArrayAdapter
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageButton
    import android.widget.Spinner
    import com.google.firebase.auth.FirebaseUser
    import com.google.firebase.database.*
    import android.widget.TextView
    import android.widget.Toast
    import android.app.DatePickerDialog
    import android.content.ContentValues.TAG
    import android.icu.util.Calendar
    import android.util.Log
    import android.view.View
    import androidx.constraintlayout.widget.ConstraintLayout
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import java.io.Serializable
    import java.text.SimpleDateFormat
    import java.util.Locale
    import com.google.firebase.firestore.FirebaseFirestore

    class EditProfileScreen : AppCompatActivity() {
        private lateinit var dobTextView: TextView
        private lateinit var firstNameEditText: EditText
        private lateinit var lastNameEditText: EditText
        private lateinit var genderSpinner: Spinner
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            setContentView(R.layout.activity_edit_profile_screen)
            firstNameEditText = findViewById(R.id.firstNameEditText)
            lastNameEditText = findViewById(R.id.lastNameEditText)
            genderSpinner = findViewById(R.id.genderSpinner)
            dobTextView = findViewById(R.id.dobTextView)



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


//        private fun saveProfileDetails() {
//            val firstName = firstNameEditText.text.toString()
//            val lastName = lastNameEditText.text.toString()
//            val gender = genderSpinner.selectedItem.toString()
//            val dob = dobTextView.text.toString()
//
//            // Check if any field is empty
//            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
//                // Show warning message
//                findViewById<TextView>(R.id.warningTextView).visibility = View.VISIBLE
//                return
//            }
//
//            val currentTime = Calendar.getInstance().time
//            val sdf = SimpleDateFormat("MMMM yyyy, h a", Locale.getDefault())
//            val formattedTime = sdf.format(currentTime)
//
//            val db = FirebaseFirestore.getInstance()
//            val user = HashMap<String, Any>()
//            user["firstName"] = firstName
//            user["lastName"] = lastName
//            user["gender"] = gender
//            user["dob"] = dob
//            user["updatedDate"] = formattedTime
//
//            db.collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                    Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
//                }
//        }
//
//
//
//    }

        private fun saveProfileDetails() {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString()
            val dob = dobTextView.text.toString()

            // Check if any field is empty
            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
                // Show warning message
                findViewById<TextView>(R.id.warningTextView).visibility = View.VISIBLE
                return
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                val userId = user.uid
                val userRef = FirebaseDatabase.getInstance("https://quotesapp-5a307-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users").child(userId)

                val currentTime = Calendar.getInstance().time
                val sdf = SimpleDateFormat("MMMM yyyy, h a", Locale.getDefault())
                val formattedTime = sdf.format(currentTime)

                val userData = User(firstName, lastName, gender, dob, formattedTime)
                userRef.setValue(userData)
                    .addOnSuccessListener {
                        startActivity(Intent(applicationContext, ProfileScreen::class.java))
                        Log.d(TAG, "User data saved successfully")

                        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error saving user data", e)
                        Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
                    }
            }
        }}



        data class User(
        var firstName: String? = "",
        var lastName: String? = "",
        var gender: String? = "",
        var dob: String? = "",
        var updatedDate: String? = ""
    )


