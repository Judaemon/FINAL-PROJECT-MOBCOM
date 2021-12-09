package com.example.courseconsultationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostQueryActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var topic: TextInputLayout
    private lateinit var question: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_query)

        auth = Firebase.auth
        user = auth.currentUser!! //non nullable

        topic = findViewById(R.id.topic)
        question = findViewById(R.id.question)

        val postQuery = findViewById<Button>(R.id.btn_postQuery)
        postQuery.setOnClickListener {
            postQuery()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val cancelQuery = findViewById<Button>(R.id.btn_cancelQuery)
        cancelQuery.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun postQuery(){
        val db = Firebase.firestore

        val userInfo = hashMapOf(
            "uid" to user.uid,
            "topic" to topic.editText?.text.toString(),
            "question" to question.editText?.text.toString(),
            "answer_status" to false,
        )

        db.collection("queries")
            .add(userInfo)
            .addOnSuccessListener { Log.d(RegisterActivity.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(RegisterActivity.TAG, "Error writing document", e) }
    }
}