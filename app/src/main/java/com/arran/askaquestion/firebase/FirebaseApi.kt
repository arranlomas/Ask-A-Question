package com.arran.askaquestion.firebase

import com.arran.askaquestion.AskAQuestion
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.utils.attachPublishSubjectToEventList
import com.arran.askaquestion.utils.composeIo
import com.arran.askaquestion.utils.createTransactionObservable
import com.arran.askaquestion.utils.setValueObservable
import com.google.firebase.database.FirebaseDatabase
import rx.Observable
import rx.subjects.PublishSubject


/**
 * Created by arran on 17/07/2017.
 */
class FirebaseApi : IFirebaseApi {

    private val KEY_QUESTIONS = "questions"
    private val KEY_VOTERS = "voters"

    private val database = FirebaseDatabase.getInstance().reference
    private val questionsRef = database.child(KEY_QUESTIONS)

    override val questionUpdateObservable: PublishSubject<List<Question>> = PublishSubject.create()

    sealed class VoteResult {
        object Success : VoteResult()
        object Failure : VoteResult()
        object AlreadyVoted : VoteResult()
    }

    override fun postQuestion(question: String): Observable<String> {
        return questionsRef.push().setValueObservable(Question(question, 1))
                .flatMap { addSelfToVotersList(it, it, true) }
                .composeIo()
    }

    override fun listenToAllQuestionUbdates() {
        return questionsRef.attachPublishSubjectToEventList(Question::class.java, questionUpdateObservable)
    }

    override fun voteDownQuestion(firebaseKey: String): Observable<VoteResult> {
        val postRef = questionsRef.child(firebaseKey)
        val transaction = createVoteTransaction { it.votes = it.votes - 1 }
        return postRef.createTransactionObservable(Question::class.java, transaction, VoteResult.Success, VoteResult.Failure)
    }

    override fun voteUpQuestion(firebaseKey: String): Observable<VoteResult> {
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

    private fun <T>getNullAuthObservable(returnObject: T): Observable<T> {
        return Observable.just(returnObject).doOnNext { throw IllegalStateException("User must be logged in") }
    }
}