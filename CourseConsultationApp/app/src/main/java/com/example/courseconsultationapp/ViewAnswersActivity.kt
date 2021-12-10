package com.example.courseconsultationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewAnswersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var answersArrayList: ArrayList<Answer>
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var qid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_answers)

        qid = intent.getStringExtra("qid").toString()
        Toast.makeText(this, qid, Toast.LENGTH_SHORT).show()
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        answersArrayList = arrayListOf()

        answerAdapter = AnswerAdapter(answersArrayList)

        recyclerView.adapter = answerAdapter

        eventChangeListener()
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("queries").document(qid).collection("answers")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                }

                val test = snapshot!!.documents
                test.forEach {
                    val query  = it.toObject(Answer::class.java)
                    query!!.id = it.id
                    answersArrayList.add(query)
                }

                answerAdapter.notifyDataSetChanged()
            }
    }
}