package com.example.courseconsultationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewQueryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_query)

        val position = intent.getIntExtra("position", 0)
    }


}