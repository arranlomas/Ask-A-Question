package com.arran.askaquestion.firebase

import rx.Observable

/**
 * Created by arran on 14/10/2017.
 */
interface IFirebaseApi {
    fun postQuestion(question: String): Observable<String>
}