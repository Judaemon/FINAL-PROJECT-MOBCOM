package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StudentMainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firstname: TextView
    private lateinit var lastname: TextView
    private lateinit var email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        val studSendEmail=findViewById<Button>(R.id.btn_studentSendEmail)
        val context = studSendEmail.context
        val iNext = Intent(context, StudentSendEmailActivity::class.java)
        //onclick -> button
        studSendEmail?.setOnClickListener {
            context.startActivity(iNext)
        }

        auth = Firebase.auth

        val logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val createEvent = findViewById<Button>(R.id.btn_createEvent)
        createEvent.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }

        val profile = findViewById<Button>(R.id.btn_profile)
        profile.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }

        val addQuery = findViewById<Button>(R.id.btn_addQuery)
        addQuery.setOnClickListener {
            startActivity(Intent(this, PostQueryActivity::class.java))
        }

        val viewQuery = findViewById<Button>(R.id.btn_viewQuery)
        viewQuery.setOnClickListener {
            startActivity(Intent(this, ViewQueriesActivity::class.java))
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
                        val firstname = document.getString("firstname")
                        val lastname = document.getString("lastname")
                        val fullname = findViewById<TextView>(R.id.tv_fullname)
                        fullname.setText("$firstname $lastname")

                        //lastname
//                        val lastname = findViewById<TextView>(R.id.inputUserLastname)
//                        fullname.setText(document.getString("lastname"))
                        //email
//                        val email = findViewById<TextView>(R.id.tv_email)
//                        email.setText(document.getString("email"))
                        val email = findViewById<TextView>(R.id.tv_email)
                        val currentEmail = currentUser.email.toString()
                        email.setText(currentEmail)

                    } else {
                        Log.d(LoginActivity.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(LoginActivity.TAG, "get failed with ", exception)
                }
        }
    }
}