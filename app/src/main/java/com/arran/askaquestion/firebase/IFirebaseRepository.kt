package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.models.Question
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
interface IFirebaseRepository {
    val questionUpdateObservable: PublishSubject<List<Question>>
    fun addNewQuestion(question: String, channel: Channel): Observable<String>
    fun attachListenerToQuestionsDatabase()
    fun voteUpQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun voteDownQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun retractVote(firebaseKey: String, userVoteState: Question.UserVoteState): Observable<Boolean>
}