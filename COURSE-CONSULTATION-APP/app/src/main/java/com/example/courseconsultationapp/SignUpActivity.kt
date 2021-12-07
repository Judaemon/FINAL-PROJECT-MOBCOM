package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var email: TextInputLayout
    private lateinit var password: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        // Getting the input field by ID
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        var emailValue: String
        var passwordValue: String

        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            if (!isAllInputFieldEmpty()){
                emailValue = email.editText?.text.toString()
                passwordValue = password.editText?.text.toString()

                 createAccount(emailValue, passwordValue)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(SignInAcivity.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SignInAcivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    private fun isAllInputFieldEmpty(): Boolean {
        return isEmpty(email) || isEmpty(password)
    }

    // Check if inputField is empty then set error message
    private fun isEmpty(editTextLayout: TextInputLayout): Boolean {
        val isEmpty = editTextLayout.editText?.text.toString().trim { it <= ' ' }.isEmpty()

        if (isEmpty){
            editTextLayout.error = "Required"
            return isEmpty
        }

        editTextLayout.error = null
        return isEmpty
    }
}