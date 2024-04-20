package com.example.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class DashboardScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_screen)

        val  goBack  = findViewById<Button>(R.id.gotoLoginScreen)
        goBack.setOnClickListener {
            val intent = Intent(applicationContext, LoginScreen::class.java)
            startActivity(intent)
        }


        auth = FirebaseAuth.getInstance()
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonCreateAccount = findViewById<Button>(R.id.buttonCreateUser)

        buttonCreateAccount.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                editTextEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                editTextPassword.error = "Password cannot be empty"
                return@setOnClickListener
            }


           if (validateEmail(email) && validatePassword(password)) {
                createAccount(email, password)
            } else {
                if (!validateEmail(email)) {
                    showToast("Invalid Email format")
                } else if (!validatePassword(password)) {
                    showToast("Weak Password")
                }
            }
        }
    }
    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    startActivity(Intent(this, HomeScreen::class.java))
                    finish()
                } else {
                    showToast("Failed : ${task.exception?.message}")
                }
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}