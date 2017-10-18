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
    fun postQuestion(question: String, channelKey: String): Observable<String>
    fun incrementQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun decreaseQuestionVoteCount(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun retractVote(firebaseKey: String): Observable<Boolean>
    fun <T> addSelfToVotersList(input: T, firebaseKey: String, voteUp: Boolean): Observable<T>
    fun subscribeToQuestionUpdates(firebaseChannelKey: String): PublishSubject<List<Question>>

    //channels
    val channelsUpdateObservable: PublishSubject<List<Channel>>
    fun listenToChannelUpdates()
    fun createChannel(channel: Channel): Observable<String>
    fun findChannel(channelName: String): Observable<List<Channel>>
    fun <T>addSelfToChannel(input: T, firebaseKey: String, owner: Boolean = false): Observable<T>
    fun findQuestionsForChannel(channelKey: String): Observable<List<Question>>
}