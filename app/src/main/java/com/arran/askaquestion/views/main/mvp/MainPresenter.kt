package com.arran.askaquestion.views.main.mvp

import com.arran.askaquestion.R
import com.arran.askaquestion.firebase.IFirebaseRepository
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.views.base.BasePresenter

/**
 * Created by arran on 11/07/2017.
 */
class MainPresenter(val firebaseRepository: IFirebaseRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        firebaseRepository.attachListenerToQuestionsDatabase()

        firebaseRepository.questionUpdateObservable
                .subscribe(object : BaseSubscriber<List<Question>>(){
                    override fun onNext(questions: List<Question>) {
                        mvpView.showQuestions(questions)
                    }
                })
    }

    override fun sendNewQuestion(text: String) {
        firebaseRepository.addNewQuestion(text)
                .subscribe(object : BaseSubscriber<String>(){
                    override fun onNext(firebaseKey: String) {
                        mvpView.showSuccess(R.string.send_question_success)
                    }
                })
    }
}