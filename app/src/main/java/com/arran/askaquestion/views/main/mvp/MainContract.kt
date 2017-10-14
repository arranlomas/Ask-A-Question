package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BaseContract
import com.arran.askaquestion.views.main.list.QuestionsAdapter

/**
 * Created by arran on 11/07/2017.
 */
interface MainContract {
    interface View : BaseContract.MvpView {
        fun showQuestions(questions: List<Question>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onListItemAction(question: Question, action: QuestionsAdapter.ACTION)
        fun sendNewQuestion(text: String)
    }
}