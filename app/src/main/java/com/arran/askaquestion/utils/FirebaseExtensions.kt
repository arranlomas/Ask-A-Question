package com.arran.askaquestion.utils

import android.util.Log
import com.arran.askaquestion.models.FirebaseObject
import com.google.firebase.database.*
import rx.Emitter
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject

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

fun <T> DatabaseReference.attachPublishSubjectToEventList(clazz: Class<T>, publishSubject: PublishSubject<List<T>>) {
    this.addValueEventListener(getEventListenerList(onError = {
        publishSubject.onError(IllegalStateException("value event listener cancelled ${it.message}"))
        Log.v("ERROR", "loadCloudItems: onCancelled", it)
    }, onDataChange = {
        publishSubject.onNext(it)
    }, clazz = clazz))
}

fun <T> getEventListenerList(onError: (Exception) -> Unit, onDataChange: (List<T>) -> Unit, clazz: Class<T>): ValueEventListener {
    return object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.v("ERROR", "onCancelled", databaseError.toException())
            onError.invoke(kotlin.IllegalStateException("No values found or request cancelled"))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val listOfItems = mutableListOf<T>()
            dataSnapshot.children.forEach {
                val firebaseKey = it.key
                val obj: T? = it.getValue(clazz)
                obj?.let {
                    (it as FirebaseObject).firebaseKey = firebaseKey
                    listOfItems.add(obj)
                }
            }
            onDataChange.invoke(listOfItems)
        }
    }
}

fun <T, R> DatabaseReference.createTransactionObservable(clazz: Class<T>, action: (T) -> Unit, success: R, error: R): Observable<R> {
    return Observable.create<R>({ subscriber ->
        this.transactAsync(clazz, action, { subscriber.onNext(error) }, { subscriber.onNext(success) })
    }, Emitter.BackpressureMode.BUFFER)
            .observeOn(Schedulers.computation())
}

fun <T> DatabaseReference.transactAsync(clazz: Class<T>, action: (T) -> Unit, onError: () -> Unit, onComplete: () -> Unit) {
    this.runTransaction(object : Transaction.Handler {
        override fun doTransaction(mutableData: MutableData): Transaction.Result {
            val p = mutableData.getValue<T>(clazz) ?: return Transaction.success(mutableData)
            action.invoke(p)
            mutableData.value = p
            return Transaction.success(mutableData)
        }

        override fun onComplete(error: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
            if (error != null) onError.invoke()
            onComplete.invoke()
        }
    })
}