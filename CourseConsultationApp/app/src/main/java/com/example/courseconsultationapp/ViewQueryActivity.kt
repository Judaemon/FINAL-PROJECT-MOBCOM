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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewQueryActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var topic: TextInputLayout
    private lateinit var question: TextInputLayout
    private lateinit var answer: TextInputLayout
    private lateinit var fullName: String
    private lateinit var uid: String
    private lateinit var qid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_query)

        auth = Firebase.auth

        topic = findViewById(R.id.topic)
        question = findViewById(R.id.question)
        answer = findViewById(R.id.answer)

        qid = intent.getStringExtra("qid").toString()

        val db = Firebase.firestore

        val currentUser = auth.currentUser

        val df = db.collection("users").document(currentUser!!.uid)

        df.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")
                    fullName = "${document.getString("firstname")} ${document.getString("lastname")}"
                    uid = currentUser.uid
                    Toast.makeText(this, fullName, Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(LoginActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(LoginActivity.TAG, "get failed with ", exception)
            }

        val viewAnswer = findViewById<Button>(R.id.btn_viewAnswer)
        viewAnswer.setOnClickListener {

            var intent = Intent(this, ViewAnswersActivity::class.java)
            intent.putExtra("qid", qid)

            startActivity(intent)
            finish()
        }

        val submitAnswer = findViewById<Button>(R.id.btn_submitAnswer)
        submitAnswer.setOnClickListener {
            submitAnswer()
            startActivity(Intent(this, StudentMainActivity::class.java))
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()

        val db = Firebase.firestore

        val df = db.collection("queries").document(qid)

        df.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")

                    topic.editText!!.setText(document.getString("topic").toString())
                    question.editText!!.setText(document.getString("question").toString())
                } else {
                    Log.d(LoginActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(LoginActivity.TAG, "get failed with ", exception)
            }
    }

    private fun submitAnswer(){
        val db = Firebase.firestore

        val answer = answer.editText!!.text

        val answerInfo = hashMapOf(
            "answer" to answer.toString(),
            "acceptedAnswer" to false,
            "answeredBy" to fullName.toString(),
            "answeredByUID" to uid,
        )

        val userPath = db.collection("queries").document(qid)
            .collection("answers")
//        db.collection("queries").document(qid).collection("answers")
            userPath.add(answerInfo)
            .addOnSuccessListener { Log.d(RegisterActivity.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(RegisterActivity.TAG, "Error writing document", e) }
    }
}