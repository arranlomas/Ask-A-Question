package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.R
import com.arran.askaquestion.firebase.FirebaseApi
import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BasePresenter
import com.arran.askaquestion.views.main.list.QuestionsAdapter

/**
 * Created by arran on 11/07/2017.
 */
class MainPresenter(val firebaseRepository: IFirebaseRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        firebaseRepository.attachListenerToQuestionsDatabase()
        firebaseRepository.questionUpdateObservable
                .subscribe(object : BaseSubscriber<List<Question>>() {
                    override fun onNext(questions: List<Question>) {
                        mvpView.showQuestions(questions)
                    }
                })
    }

    override fun sendNewQuestion(text: String) {
        firebaseRepository.addNewQuestion(text)
                .subscribe(object : BaseSubscriber<String>() {
                    override fun onNext(firebaseKey: String) {
                        mvpView.showSuccess(R.string.send_question_success)
                    }
                })
    }

    override fun onListItemAction(question: Question, action: QuestionsAdapter.ACTION) {
        when (action) {
            QuestionsAdapter.ACTION.CLICK -> TODO()
            QuestionsAdapter.ACTION.VOTE_UP -> firebaseRepository.voteUpQuestion(question.firebaseKey)
                    .subscribe(object : BaseSubscriber<FirebaseApi.VoteResult>(){
                        override fun onNext(voteResult: FirebaseApi.VoteResult) {
                            when(voteResult){
                                FirebaseApi.VoteResult.Success -> mvpView.showSuccess(R.string.upvote_question_success)
                                FirebaseApi.VoteResult.Failure -> mvpView.showError()
                                FirebaseApi.VoteResult.AlreadyVoted -> mvpView.showError()
                            }
                        }
                    })
            QuestionsAdapter.ACTION.VOTE_DOWN -> firebaseRepository.voteDownQuestion(question.firebaseKey)
                    .subscribe(object : BaseSubscriber<FirebaseApi.VoteResult>(){
                        override fun onNext(voteResult: FirebaseApi.VoteResult) {
                            when(voteResult){
                                FirebaseApi.VoteResult.Success -> mvpView.showSuccess(R.string.downvote_question_success)
                                else -> mvpView.showError()
                            }
                        }
                    })
        }
    }
}