package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.models.Question
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
interface IFirebaseRepository {
    fun addNewQuestion(question: String, channelKey: String): Observable<String>
    fun voteUpQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun voteDownQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult>
    fun retractVote(firebaseKey: String, userVoteState: Question.UserVoteState): Observable<Boolean>
    fun subscribeToQuestionUpdates(firebaseChannelKey: String): PublishSubject<List<Question>>
    fun findQuestionsForChannel(channelKey: String): Observable<List<Question>>

    val channelsUpdateObservable: PublishSubject<List<Channel>>
    fun attachListenerToChannelsDatabase()
    fun createChannel(channelName: String, channelPassword: String): Observable<String>
    fun joinChannel(channelName: String, channelPassword: String): Observable<Channel>
}