package com.arran.askaquestion.firebase

import com.arran.askaquestion.models.Channel
import com.arran.askaquestion.models.Question
import com.arran.askaquestion.utils.composeIo
import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 14/10/2017.
 */
class FirebaseRepository(val firebaseApi: IFirebaseApi): IFirebaseRepository {

    override fun voteUpQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult> {
        return firebaseApi.incrementQuestionVoteCount(firebaseKey)
                .flatMap { firebaseApi.addSelfToVotersList(it, firebaseKey, true) }
                .composeIo()
    }

    override fun voteDownQuestion(firebaseKey: String): Observable<FirebaseApi.VoteResult> {
        return firebaseApi.decreaseQuestionVoteCount(firebaseKey)
                .flatMap { firebaseApi.addSelfToVotersList(it, firebaseKey, false) }
                .composeIo()
    }

    override val questionUpdateObservable: PublishSubject<List<Question>>
        get() = firebaseApi.questionUpdateObservable

    override fun attachListenerToQuestionsDatabase() {
        firebaseApi.listenToAllQuestionUbdates()
    }

    override fun addNewQuestion(question: String, channel: Channel): Observable<String> {
        return firebaseApi.postQuestion(question, channel)
                .composeIo()
    }

    override fun retractVote(firebaseKey: String, userVoteState: Question.UserVoteState): Observable<Boolean> {
        return firebaseApi.retractVote(firebaseKey)
                .flatMap {
                    when(userVoteState){
                        Question.UserVoteState.UP -> firebaseApi.decreaseQuestionVoteCount(firebaseKey)
                        Question.UserVoteState.UNVOTED -> Observable.just(FirebaseApi.VoteResult.Success)
                        Question.UserVoteState.DOWN -> firebaseApi.incrementQuestionVoteCount(firebaseKey)
                    }
                }
                .map { when(it){
                    FirebaseApi.VoteResult.Success -> true
                    FirebaseApi.VoteResult.Failure -> false
                    FirebaseApi.VoteResult.AlreadyVoted -> false
                }
                }
                .composeIo()
    }
}