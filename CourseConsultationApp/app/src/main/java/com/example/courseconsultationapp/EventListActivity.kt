package com.example.courseconsultationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class EventListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventArrayList: ArrayList<Event>
    private lateinit var eventAdapter: EventAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf()

        eventAdapter = EventAdapter(eventArrayList)

        recyclerView.adapter = eventAdapter

        eventChangeListener()
    }
    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                }

                val test = snapshot!!.documents
                test.forEach {
                    val event  = it.toObject(Event::class.java)
                    event!!.id = it.id
                    eventArrayList.add(event)
                }

                eventAdapter.notifyDataSetChanged()
            }
    }
}