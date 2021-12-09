package com.example.courseconsultationapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EventAdapter (private val eventList : ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.EventHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventHolder {
        val eventView = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)

        return EventHolder(eventView)
    }

    override fun onBindViewHolder(holder: EventAdapter.EventHolder, position: Int) {
        val event : Event = eventList[position]
        holder.topic.text = event.eventTitle
        holder.isAnswered.text = event.eventTitle.toString()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    inner class EventHolder(eventView : View) : RecyclerView.ViewHolder(eventView){
        val topic : TextView = eventView.findViewById(R.id.tv_topic)
        val isAnswered : TextView = eventView.findViewById(R.id.tv_isAnswered)

        init {
            eventView.setOnClickListener{
                val position: Int = adapterPosition
                val test = eventList[position].id

                val intent = Intent(eventView.context, EventListActivity::class.java)
                intent.putExtra("position", position);

                eventView.context.startActivity(intent)

                Toast.makeText(itemView.context, "You clicked on item #${test}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}