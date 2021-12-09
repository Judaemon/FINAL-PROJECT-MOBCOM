package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InstructorMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_main)

        val studSendEmail=findViewById<Button>(R.id.btn_instructorSendEmail)
        val context = studSendEmail.context
        val iNext = Intent(context, InstructorSendEmailActivity::class.java)

        studSendEmail?.setOnClickListener {
            context.startActivity(iNext)
        }

        val profile = findViewById<Button>(R.id.btn_profile)
        profile.setOnClickListener {
            startActivity(Intent(this, EventListActivity::class.java))
        }

        val logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Firebase.auth.signOut()
            finish()
        }
    }
}