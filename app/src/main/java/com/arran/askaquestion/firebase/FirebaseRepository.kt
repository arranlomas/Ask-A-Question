package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Question
import com.arran.askaquestion.utils.composeIo
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
class FirebaseRepository(val firebaseApi: IFirebaseApi): IFirebaseRepository {
    override fun voteUpQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult> {
        return firebaseApi.voteUpQuestion(firebaseKey)
                .composeIo()
    }

    override fun voteDownQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult> {
        return firebaseApi.voteDownQuestion(firebaseKey)
                .composeIo()
    }

    override val questionUpdateObservable: PublishSubject<List<Question>>
        get() = firebaseApi.questionUpdateObservable

    override fun attachListenerToQuestionsDatabase() {
        firebaseApi.listenToAllQuestionUbdates()
    }

    override fun addNewQuestion(question: String): Observable<String> {
        return firebaseApi.postQuestion(question)
                .composeIo()
    }
}