package com.arran.askaquestion.views.question

import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BaseContract
import com.arran.askaquestion.views.question.list.QuestionsAdapter

/**
 * Created by arran on 11/07/2017.
 */
interface QuestionsContract {
    interface View : BaseContract.MvpView {
        fun showQuestions(questions: List<Question>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun reload(channelKey: String?)
        fun onListItemAction(question: Question, action: QuestionsAdapter.ACTION)
        fun sendNewQuestion(text: String)
    }
}