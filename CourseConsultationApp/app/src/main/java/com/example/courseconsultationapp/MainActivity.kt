package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val logout = findViewById<Button>(R.id.btn_logout)
        logout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Firebase.auth.signOut()
            finish()
        }

        val addQuery = findViewById<Button>(R.id.btn_addQuery)
        addQuery.setOnClickListener {
            startActivity(Intent(this, PostQueryActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}