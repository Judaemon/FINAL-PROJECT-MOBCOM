package com.example.courseconsultationapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AnswerAdapter(private val answerList : ArrayList<Answer>) : RecyclerView.Adapter<AnswerAdapter.AnswerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        val answerView = LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)

        return AnswerHolder(answerView)
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        val answer : Answer = answerList[position]
        holder.answer.text = answer.answer
        holder.answeredBy.text = answer.answeredBy
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    inner class AnswerHolder(answerView: View) : RecyclerView.ViewHolder(answerView) {
        val answer: TextView = answerView.findViewById(R.id.tv_topic)
        val answeredBy: TextView = answerView.findViewById(R.id.tv_isAnswered)

        init {
            answerView.setOnClickListener {
                val position: Int = adapterPosition

                Toast.makeText(itemView.context, "You clicked on item #${position + 1}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}