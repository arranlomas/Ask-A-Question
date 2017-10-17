package com.arran.askaquestion.views.question.list

import android.support.v4.content.res.ResourcesCompat
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
        CLICK,
        VOTE_UP,
        VOTE_DOWN
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
        question_points.text = question.votes.toString()
        question_vote_down.setOnClickListener { onAction.invoke(QuestionsAdapter.ACTION.VOTE_DOWN) }
        question_vote_up.setOnClickListener { onAction.invoke(QuestionsAdapter.ACTION.VOTE_UP) }
        when (question.getUserVote()) {
            Question.UserVoteState.UNVOTED -> {
                question_vote_up.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_faded, null))
                question_vote_down.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward_deselected, null))
            }
            Question.UserVoteState.UP -> {
                question_vote_up.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_accent, null))
                question_vote_down.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward_deselected, null))
            }
            Question.UserVoteState.DOWN -> {
                question_vote_up.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_upward_faded, null))
                question_vote_down.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_downward_accent, null))
            }
        }
    }
}