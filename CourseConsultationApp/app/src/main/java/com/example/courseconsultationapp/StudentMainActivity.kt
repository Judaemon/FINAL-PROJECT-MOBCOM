package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StudentMainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

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
            startActivity(Intent(this, LoginActivity::class.java))
            Firebase.auth.signOut()
            finish()
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
}