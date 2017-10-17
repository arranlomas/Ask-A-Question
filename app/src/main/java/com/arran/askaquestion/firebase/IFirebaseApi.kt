package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.models.Question
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
interface IFirebaseApi {
    //questions
    val questionUpdateObservable: PublishSubject<List<Question>>
    fun postQuestion(question: String, channelKey: String): Observable<String>
    fun listenToAllQuestionUpdates()
    fun incrementQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun decreaseQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun retractVote(firebaseKey: String): Observable<Boolean>
    fun <T> addSelfToVotersList(input: T, firebaseKey: String, voteUp: Boolean): Observable<T>

    //channels
    val channelsUpdateObservable: PublishSubject<List<Channel>>
    fun listenToChannelUpdates()
    fun createChannel(channel: Channel): Observable<String>
    fun <T>addSelfToChannel(input: T, firebaseKey: String, owner: Boolean = false): Observable<T>
}