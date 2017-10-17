package com.arran.askaquestion.firebase

import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.utils.*
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 17/07/2017.
 */
class FirebaseApi : IFirebaseApi {
    private val KEY_QUESTIONS = "questions"
    private val KEY_VOTERS = "voters"
    private val KEY_CHANNELS = "channels"

    private val database = FirebaseDatabase.getInstance().reference
    private val questionsRef = database.child(KEY_QUESTIONS)
    private val channelsRef = database.child(KEY_CHANNELS)

    override val questionUpdateObservable: PublishSubject<List<Question>> = PublishSubject.create()

    sealed class VoteResult {
        object Success : VoteResult()
        object Failure : VoteResult()
        object AlreadyVoted : VoteResult()
    }

    private fun <T>getNullAuthObservable(returnObject: T): Observable<T> {
        return Observable.just(returnObject).doOnNext { throw IllegalStateException("User must be logged in") }
    }

    override fun postQuestion(question: String, channelKey: String): Observable<String> {
        return questionsRef.push().setValueObservable(Question(question, 1, channelKey = channelKey))
                .flatMap { addSelfToVotersList(it, it, true) }
                .composeIo()
    }

    override fun listenToAllQuestionUbdates() {
        return questionsRef.attachPublishSubjectToEventList(Question::class.java, questionUpdateObservable)
    }

    override fun decreaseQuestionVoteCount(firebaseKey: String): Observable<VoteResult> {
        val postRef = questionsRef.child(firebaseKey)
        val transaction = createVoteTransaction { it.votes = it.votes - 1 }
        return postRef.createTransactionObservable(Question::class.java, transaction, VoteResult.Success, VoteResult.Failure)
    }

    override fun incrementQuestionVoteCount(firebaseKey: String): Observable<VoteResult> {
        val postRef = questionsRef.child(firebaseKey)
        val transaction = createVoteTransaction { it.votes = it.votes + 1 }
        return postRef.createTransactionObservable(Question::class.java, transaction, VoteResult.Success, VoteResult.Failure)
    }

    override fun <T>addSelfToVotersList(input: T, firebaseKey: String, voteUp: Boolean): Observable<T>{
        AskAQuestion.currentUser?.uid?.let {
            return questionsRef.child(firebaseKey).child(KEY_VOTERS).child(it).setValueObservable(voteUp)
                    .map { input }
        } ?: return getNullAuthObservable(input)
    }

    private fun createVoteTransaction(action: (Question) -> Unit): (Question) -> Boolean {
        return { question ->
            AskAQuestion.currentUser?.uid?.let {
                if (!question.voters.containsKey(it)) {
                    action.invoke(question)
                    return@let true
                }
                else return@let false
            } ?: false
        }
    }

    override fun retractVote(firebaseKey: String): Observable<Boolean> {
        AskAQuestion.currentUser?.uid?.let {
           return questionsRef.child(firebaseKey).child(KEY_VOTERS).child(it).removeValueObservable()
        } ?: return getNullAuthObservable(false)
    }

    override fun createChannel(channel: Channel): Observable<String> {
       return channelsRef.push().setValueObservable(channel)
    }
}