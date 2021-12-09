package com.example.courseconsultationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class ViewQueriesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var queriesArrayList: ArrayList<Queries>
    private lateinit var queryAdapter: QueryAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_queries)

        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        queriesArrayList = arrayListOf()

        queryAdapter = QueryAdapter(queriesArrayList)

        recyclerView.adapter = queryAdapter

        eventChangeListener()
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("queries")
            .addSnapshotListener(object  : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                         Log.e("Firestore Error", error.message.toString())
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            queriesArrayList.add(dc.document.toObject(Queries::class.java))
                        }
                    }

                    queryAdapter.notifyDataSetChanged()
                }
            })
    }
}