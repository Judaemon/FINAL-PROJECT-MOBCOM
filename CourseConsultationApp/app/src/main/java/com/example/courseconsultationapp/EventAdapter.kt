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
        val startDate = event.eventDateStart.toString()
        val endDate = event.eventDateEnd.toString()

        holder.topic.text = event.eventTitle.toString()
        holder.isAnswered.text = "Date: $startDate - $endDate"
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    inner class EventHolder(eventView : View) : RecyclerView.ViewHolder(eventView){
        val topic : TextView = eventView.findViewById(R.id.tv_topic)
        val isAnswered : TextView = eventView.findViewById(R.id.tv_isAnswered)

        init {
            eventView.setOnClickListener{
//                val event : Event = eventList[position]
                val position: Int = adapterPosition
                val eid = eventList[position].id
                //val test = eventList[position].id

                var intent = Intent(eventView.context, ViewEventActivity::class.java)
                intent.putExtra("eid", eid)

                eventView.context.startActivity(intent)

                Toast.makeText(itemView.context, "You clicked on item #${eid}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}