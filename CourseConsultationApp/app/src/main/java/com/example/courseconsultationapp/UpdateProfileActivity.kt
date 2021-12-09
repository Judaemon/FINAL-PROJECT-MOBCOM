package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var firstname: EditText
    private lateinit var lastname: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        val email: String
        val user = Firebase.auth.currentUser

        firstname = findViewById(R.id.inputFirstname)
        lastname = findViewById(R.id.inputLastname)

        user?.let {
            val name = user.displayName
            val email = user.email.toString()
            val emailVerified = user.isEmailVerified
            val uid = user.uid

            val btnSave = findViewById(R.id.btn_save) as Button
            btnSave.setOnClickListener {
                saveProfile()
            }
//            var hello:String = email
//            val nametxt = findViewById (R.id.inputLastname) as EditText
//            nametxt.setText(hello)
        }

        val back = findViewById<TextView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, StudentMainActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser

        if(currentUser != null){
            val db = Firebase.firestore

            val df = db.collection("users").document(currentUser.uid)

            df.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")
                        //firstname
                        val firstname = findViewById<EditText>(R.id.inputFirstname)
                        firstname.setText(document.getString("firstname"))
                        //lastname
                        val lastname = findViewById<EditText>(R.id.inputLastname)
                        lastname.setText(document.getString("lastname"))
                    } else {
                        Log.d(LoginActivity.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(LoginActivity.TAG, "get failed with ", exception)
                }
        }
    }

    public fun saveProfile(){
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore

        val firstname = firstname.text.toString()
        val lastname = lastname.text.toString()

//        val userInfo = hashMapOf(
//            "firstname" to firstname,
//            "lastname" to lastname,
//
//        )

        db.collection("users").document(user?.uid.toString())
            .update(mapOf(
                "firstname" to firstname,
                "lastname" to lastname,
            ))
            .addOnSuccessListener { Log.d(RegisterActivity.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(RegisterActivity.TAG, "Error writing document", e) }
    }
}