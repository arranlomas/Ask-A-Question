package com.arran.askaquestion.views.question

import com.arran.askaquestion.R
import com.arran.askaquestion.firebase.FirebaseApi
import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BasePresenter
import com.arran.askaquestion.views.question.list.QuestionsAdapter
import rx.Subscriber

/**
 * Created by arran on 11/07/2017.
 */
class QuestionsPresenter(val firebaseRepository: IFirebaseRepository) : BasePresenter<QuestionsContract.View>(), QuestionsContract.Presenter {
    private var channelKey: String? = null

    override fun reload(channelKey: String?) {
        channelKey?.let { this.channelKey = it }
        if (channelKey == null) return
        firebaseRepository.subscribeToQuestionUpdates(channelKey)
                .subscribe(object : BaseSubscriber<List<Question>>() {
                    override fun onNext(questions: List<Question>) {
                        mvpView.showQuestions(questions.sortedByDescending { it.votes })
                    }
                })
    }

    override fun sendNewQuestion(text: String) {
        channelKey?.let {
            firebaseRepository.addNewQuestion(text, it)
                    .subscribe(object : BaseSubscriber<String>() {
                        override fun onNext(firebaseKey: String) {
                            mvpView.showSuccess(R.string.send_question_success)
                        }
                    })
        } ?: mvpView.showError(R.string.error_not_in_channel)
    }

    override fun onListItemAction(question: Question, action: QuestionsAdapter.ACTION) {
        when (action) {
            QuestionsAdapter.ACTION.CLICK -> TODO()
            QuestionsAdapter.ACTION.VOTE_UP -> {
                val userVoteState = question.getUserVote()
                when (userVoteState) {
                    Question.UserVoteState.UNVOTED -> upvote(question.firebaseKey)
                    Question.UserVoteState.UP -> retractVote(question.firebaseKey, userVoteState)
                    Question.UserVoteState.DOWN -> {
                        retractVote(question.firebaseKey, userVoteState)
                        upvote(question.firebaseKey)
                    }
                }

            }
            QuestionsAdapter.ACTION.VOTE_DOWN -> {
                val userVoteState = question.getUserVote()
                when (userVoteState) {
                    Question.UserVoteState.UNVOTED -> downvote(question.firebaseKey)
                    Question.UserVoteState.UP -> {
                        retractVote(question.firebaseKey, userVoteState)
                        downvote(question.firebaseKey)
                    }
                    Question.UserVoteState.DOWN -> {
                        retractVote(question.firebaseKey, userVoteState)
                    }
                }
            }
        }
    }

    private fun upvote(firebaseKey: String) {
        firebaseRepository.voteUpQuestion(firebaseKey)
                .subscribe(getVoteSubscriber())
    }

    private fun downvote(firebaseKey: String) {
        firebaseRepository.voteDownQuestion(firebaseKey)
                .subscribe(getVoteSubscriber())
    }

    private fun retractVote(firebaseKey: String, userVoteState: Question.UserVoteState) {
        firebaseRepository.retractVote(firebaseKey, userVoteState)
                .subscribe(getRetractVoteSubscriber())
    }

    private fun getRetractVoteSubscriber(): Subscriber<Boolean> {
        return object : BaseSubscriber<Boolean>() {
            override fun onNext(retracted: Boolean) {
                if (!retracted) mvpView.showError()
            }
        }
    }

    private fun getVoteSubscriber(): Subscriber<FirebaseApi.VoteResult> {
        return object : BaseSubscriber<FirebaseApi.VoteResult>() {
            override fun onNext(voteResult: FirebaseApi.VoteResult) {
                if (voteResult != FirebaseApi.VoteResult.Success) mvpView.showError()
            }
        }
    }
}