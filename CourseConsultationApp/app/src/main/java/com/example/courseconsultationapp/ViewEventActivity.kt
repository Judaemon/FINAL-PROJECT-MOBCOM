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

class ViewEventActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var eventTitle: TextInputLayout
    private lateinit var eventLocation: TextInputLayout
    private lateinit var startDate: TextInputLayout
    private lateinit var endDate: TextInputLayout
    private lateinit var startTime: TextInputLayout
    private lateinit var endTime: TextInputLayout
    private lateinit var inviteList: TextInputLayout

    private lateinit var fullName: String
    private lateinit var uid: String
    private lateinit var eid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        auth = Firebase.auth

        eventTitle = findViewById(R.id.newEventTitle)
        eventLocation = findViewById(R.id.newEventLocation)
        startDate = findViewById(R.id.newEventStartingDate)
        endDate = findViewById(R.id.newEventEndingDate)
        startTime = findViewById(R.id.newEventStartingTime)
        endTime = findViewById(R.id.newEventEndingTime)
        inviteList = findViewById(R.id.newEventInviteList)

        eid = intent.getStringExtra("eid").toString()

        val db = Firebase.firestore

        val currentUser = auth.currentUser

        val df = db.collection("users").document(currentUser!!.uid)

        df.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")
                    fullName = "${document.getString("eventLocation")}"
                    uid = currentUser.uid
                } else {
                    Log.d(LoginActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(LoginActivity.TAG, "get failed with ", exception)
            }
    }

    public override fun onStart() {
        super.onStart()

        val db = Firebase.firestore

        val df = db.collection("events").document(eid)
        df.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")
                    eventTitle.editText!!.setText(document.getString("eventTitle").toString())
                    eventLocation.editText!!.setText(document.getString("eventLocation").toString())
                    startDate.editText!!.setText(document.getString("eventDateStart").toString())
                    endDate.editText!!.setText(document.getString("eventDateEnd").toString())
                    startTime.editText!!.setText(document.getString("eventTimeStart").toString())
                    endTime.editText!!.setText(document.getString("eventTimeEnd").toString())
                    inviteList.editText!!.setText(document.getString("eventInviteList").toString())
                } else {
                    Log.d(LoginActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(LoginActivity.TAG, "get failed with ", exception)
            }
    }


}