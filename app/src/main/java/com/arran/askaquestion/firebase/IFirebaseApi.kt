package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Question
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
interface IFirebaseApi {
    val questionUpdateObservable: PublishSubject<List<Question>>
    fun postQuestion(question: String): Observable<String>
    fun listenToAllQuestionUbdates()
    fun incrementQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun decreaseQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun retractVote(firebaseKey: String): Observable<Boolean>
    fun  <T>addSelfToVotersList(input: T, firebaseKey: String, voteUp: Boolean): Observable<T>
}