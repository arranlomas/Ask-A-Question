package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Question
import com.arran.askaquestion.utils.attachPublishSubjectToEventList
import com.arran.askaquestion.utils.composeIo
import com.arran.askaquestion.utils.setValueObservable
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 17/07/2017.
 */
class FirebaseApi : IFirebaseApi {

    private val KEY_QUESTIONS = "questions"

    private val database = FirebaseDatabase.getInstance().reference

    override val questionUpdateObservable: PublishSubject<List<Question>> = PublishSubject.create()

    override fun postQuestion(question: String): Observable<String> {
        return database.child(KEY_QUESTIONS).push().setValueObservable(Question(question))
                .composeIo()
    }

    override fun listenToAllQuestionUbdates() {
        return database.child(KEY_QUESTIONS).attachPublishSubjectToEventList(Question::class.java, questionUpdateObservable)
    }
}

