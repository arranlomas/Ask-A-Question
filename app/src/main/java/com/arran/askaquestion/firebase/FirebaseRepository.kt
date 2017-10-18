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

    override fun subscribeToQuestionUpdates(firebaseChannelKey: String): PublishSubject<List<Question>> {
        return firebaseApi.subscribeToQuestionUpdates(firebaseChannelKey)
    }

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

    override fun addNewQuestion(question: String, channelKey: String): Observable<String> {
        return firebaseApi.postQuestion(question, channelKey)
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

    override val channelsUpdateObservable: PublishSubject<List<Channel>>
        get() = firebaseApi.channelsUpdateObservable

    override fun createChannel(channelName: String, channelPassword: String): Observable<String> {
        return firebaseApi.createChannel(Channel(channelName, channelPassword))
                .flatMap { firebaseApi.addSelfToChannel(it, it, true) }
                .composeIo()
    }

    override fun attachListenerToChannelsDatabase() {
        firebaseApi.listenToChannelUpdates()
    }

    override fun joinChannel(channelName: String, channelPassword: String): Observable<Channel> {
        return firebaseApi.findChannel(channelName)
                .flatMapIterable { it }
                .filter { it.channelPassword == channelPassword }
                .flatMap { firebaseApi.addSelfToChannel(it, it.firebaseKey, false) }
    }

    override fun findQuestionsForChannel(channelKey: String): Observable<List<Question>> {
        return firebaseApi.findQuestionsForChannel(channelKey)
                .composeIo()
    }
}