package com.arran.askaquestion.utils

import com.google.firebase.database.DatabaseReference
import rx.Emitter
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by arran on 14/10/2017.
 */
fun <T> DatabaseReference.setValueObservable(item: T): Observable<String> {
    return Observable.create<String>({ subscriber ->
        this.setValue(item, { error, _ ->
            if (error == null) {
                subscriber.onNext(this.key)
            } else {
                subscriber.onError(IllegalStateException("error posting item: ${error.message}"))
            }
        })
    }, Emitter.BackpressureMode.BUFFER)
            .observeOn(Schedulers.computation())
}