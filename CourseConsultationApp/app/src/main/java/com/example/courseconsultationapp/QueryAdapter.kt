package com.example.courseconsultationapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QueryAdapter(private val queryList : ArrayList<Queries>) : RecyclerView.Adapter<QueryAdapter.QueryHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryAdapter.QueryHolder {
        val queryView = LayoutInflater.from(parent.context).inflate(R.layout.item_query, parent, false)

        return QueryHolder(queryView)
    }

    override fun onBindViewHolder(holder: QueryAdapter.QueryHolder, position: Int) {
        val query : Queries = queryList[position]
        holder.topic.text = query.topic
        holder.isAnswered.text = query.answer_status.toString()
    }

    override fun getItemCount(): Int {
        return queryList.size
    }

    public class QueryHolder(queryView : View) : RecyclerView.ViewHolder(queryView){
        val topic : TextView = itemView.findViewById(R.id.tv_topic)
        val isAnswered : TextView = itemView.findViewById(R.id.tv_isAnswered)
    }
}