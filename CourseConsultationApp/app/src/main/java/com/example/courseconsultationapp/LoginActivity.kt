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

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // Getting the input field by ID
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        var emailValue: String
        var passwordValue: String

        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            if (validateEmail() || validatePassword()){
                emailValue = email.editText?.text.toString()
                passwordValue = password.editText?.text.toString()

                auth.signInWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")

                            val currentUser = auth.currentUser

                            val db = Firebase.firestore

                            val df = db.collection("users").document(currentUser!!.uid)

                            df.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                        if (document.getBoolean("isInstructor") == true){
                                            startActivity(Intent(this, InstructorMainActivity::class.java))
                                            finish()
                                        }

                                        if (document.getBoolean("isInstructor") == false){
                                            startActivity(Intent(this, StudentMainActivity::class.java))
                                            finish()
                                        }
                                    } else {
                                        Log.d(TAG, "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Wrong Email or password.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val textRegister = findViewById<TextView>(R.id.register)
        textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val db = Firebase.firestore

            val df = db.collection("queries").document(currentUser.uid)

            df.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        if (document.getBoolean("isInstructor") == true){
                            startActivity(Intent(this, InstructorMainActivity::class.java))
                            finish()
                        }

                        if (document.getBoolean("isInstructor") == false){
                            startActivity(Intent(this, StudentMainActivity::class.java))
                            finish()
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
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

    // Regex not working
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

    companion object {
        const val TAG = "Login"
    }
}