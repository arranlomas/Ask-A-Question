package com.arran.askaquestion.utils

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by arran on 14/10/2017.
 */
fun <T> Observable<T>.composeIo(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
})