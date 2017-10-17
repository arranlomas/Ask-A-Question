package com.arran.askaquestion.models

import com.arran.askaquestion.AskAQuestion
import com.google.firebase.database.Exclude

/**
 * Created by arran on 14/10/2017.
 */
data class Question(
        var questionText: String = "",
        var votes: Int = 0,
        var voters: Map<String, Boolean> = mapOf(),
        var channelKey: String? = null
) : FirebaseObject(){
    enum class UserVoteState{
        UNVOTED,
        UP,
        DOWN
    }

     @Exclude fun getUserVote(): UserVoteState{
        AskAQuestion.currentUser?.uid?.let {
            if(this.voters.containsKey(it)) {
                when(this.voters.getValue(it)){
                    true -> return UserVoteState.UP
                    false -> return UserVoteState.DOWN
                }
            }else return UserVoteState.UNVOTED
        } ?: return UserVoteState.UNVOTED
    }

}