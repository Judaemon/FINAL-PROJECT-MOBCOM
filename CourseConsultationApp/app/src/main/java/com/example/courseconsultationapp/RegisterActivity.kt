package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var firstname: TextInputLayout
    private lateinit var lastname: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        // Getting the input field by ID
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)

        var emailValue: String
        var passwordValue: String

        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            if (validateEmail() || validatePassword()){
                emailValue = email.editText?.text.toString()
                passwordValue = password.editText?.text.toString()

                createAccount(emailValue, passwordValue)
            }
        }

        val textLogin = findViewById<TextView>(R.id.login)
        textLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateEmail(): Boolean {
        val newEmail = email.editText?.text.toString().trim { it <= ' ' }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

        return if (newEmail.isEmpty()) {
            email.error = "Required field"
             false
        } else if (!newEmail.matches(emailPattern)) {
            email.error = "Invalid email address"
            false
        } else {
            email.error = null
            email.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        val newPassword = password.editText?.text.toString().trim { it <= ' ' }

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        val passwordMatcher = Regex(passwordPattern)

        return when {
            newPassword.isEmpty() -> {
                password.error = "Required field"
                false
            }
            passwordMatcher.find(newPassword) != null -> {
                password.error = "Password is too weak"
                false
            }
            newPassword.length <= 8 -> {
                password.error = "Password too short"
                false
            }
            else -> {
                password.error = null
                password.isErrorEnabled = false
                true
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(TAG, "createUserWithEmail:success")

                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()

                    val user = auth.currentUser

                    val db = Firebase.firestore

                    val userInfo = hashMapOf(
                        "firstname" to firstname.editText?.text.toString(),
                        "lastname" to lastname.editText?.text.toString(),
                        "isAdmin" to false,
                    )

                    db.collection("users").document(user?.uid.toString())
                        .set(userInfo)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Please try again later.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val TAG = "Register"
    }
}