package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InstructorMainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firstname: TextView
    private lateinit var lastname: TextView
    private lateinit var email: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_main)

        val studSendEmail=findViewById<Button>(R.id.btn_instructorSendEmail)
        val context = studSendEmail.context
        val iNext = Intent(context, InstructorSendEmailActivity::class.java)

        studSendEmail?.setOnClickListener {
            context.startActivity(iNext)
        }

        val viewEvent = findViewById<Button>(R.id.btn_viewEvent)
        viewEvent.setOnClickListener {
            startActivity(Intent(this, EventListActivity::class.java))
        }

        val profile = findViewById<Button>(R.id.btn_profile)
        profile.setOnClickListener {
            startActivity(Intent(this, InstructorUpdateProfileActivity::class.java))
        }

        val logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Firebase.auth.signOut()
            finish()
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