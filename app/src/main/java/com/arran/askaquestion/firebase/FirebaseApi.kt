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

    private val database = FirebaseDatabase.getInstance().reference
    private val questionsRef = database.child(KEY_QUESTIONS)

    override val questionUpdateObservable: PublishSubject<List<Question>> = PublishSubject.create()

    override fun postQuestion(question: String): Observable<String> {
        return questionsRef.push().setValueObservable(Question(question, 0))
                .composeIo()
    }

    override fun listenToAllQuestionUbdates() {
        return questionsRef.attachPublishSubjectToEventList(Question::class.java, questionUpdateObservable)
    }

    override fun voteDownQuestion(firebaseKey: String): Observable<VoteResult> {
        val postRef = questionsRef.child(firebaseKey)
        val transaction = createVoteTransaction { it.votes = it.votes - 1 }
        return postRef.createTransactionObservable(Question::class.java, transaction, VoteResult.Success, VoteResult.Success)
    }

    override fun voteUpQuestion(firebaseKey: String): Observable<VoteResult> {
        val postRef = questionsRef.child(firebaseKey)
        val transaction = createVoteTransaction { it.votes = it.votes + 1 }
        return postRef.createTransactionObservable(Question::class.java, transaction, VoteResult.Success, VoteResult.Success)
    }

    sealed class VoteResult{
        object Success: VoteResult()
        object Failure: VoteResult()
    }


    private fun createVoteTransaction(action: (Question) -> Unit): (Question) -> Unit {
        return { question ->
            val uid = AskAQuestion.currentUser?.uid
            if (!question.voters.contains(uid)) {
                action.invoke(question)

                uid?.let { question.voters.put(uid, false) }
            }
        }
    }
}