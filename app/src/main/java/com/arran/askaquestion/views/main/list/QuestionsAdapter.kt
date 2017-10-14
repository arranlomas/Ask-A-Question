package com.arran.askaquestion.views.main.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arran.askaquestion.R
import com.arran.askaquestion.models.Question
import kotlinx.android.synthetic.main.ls_question.view.*

/**
 * Created by arran on 14/10/2017.
 */
class QuestionsAdapter(val itemClickListener: (Question, ACTION) -> Unit) : RecyclerView.Adapter<QuestionViewHolder>() {

    var items: List<Question> = emptyList()

    enum class ACTION {
        OPEN
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.ls_question, parent, false)
        val holder = QuestionViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: QuestionViewHolder?, position: Int) {
        holder?.bind(items[position], { action ->
            itemClickListener.invoke(items[position], action)
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class QuestionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(question: Question, onAction: (QuestionsAdapter.ACTION) -> Unit) = with(view) {
        question_text.text = question.questionText
    }
}