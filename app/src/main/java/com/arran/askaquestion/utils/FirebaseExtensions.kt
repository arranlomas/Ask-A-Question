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

fun DatabaseReference.removeValueObservable(): Observable<Boolean> {
    return Observable.create<Boolean>({ subscriber ->
        this.removeValue().addOnCompleteListener {
            if(it.isSuccessful)subscriber.onNext(true)
            else subscriber.onError(it.exception)
        }
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

fun <T> DatabaseReference.attachPublishSubjectToEventListWithFilter(clazz: Class<T>, publishSubject: PublishSubject<List<T>>, predicate: (T) -> Boolean) {
    this.addValueEventListener(getEventListenerList(onError = {
        publishSubject.onError(IllegalStateException("value event listener cancelled ${it.message}"))
        Log.v("ERROR", "loadCloudItems: onCancelled", it)
    }, onDataChange = {
        publishSubject.onNext(it.filter (predicate))
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

fun <T, R> DatabaseReference.createTransactionObservable(clazz: Class<T>, action: (T) -> Boolean, success: R, error: R): Observable<R> {
    return Observable.create<R>({ subscriber ->
        this.transactAsync(clazz, action, { subscriber.onNext(error) }, { subscriber.onNext(success) })
    }, Emitter.BackpressureMode.BUFFER)
            .observeOn(Schedulers.computation())
}

fun <T> DatabaseReference.transactAsync(clazz: Class<T>, action: (T) -> Boolean, onError: () -> Unit, onComplete: () -> Unit) {
    this.runTransaction(object : Transaction.Handler {
        override fun doTransaction(mutableData: MutableData): Transaction.Result {
            val p = mutableData.getValue<T>(clazz) ?: return Transaction.success(mutableData)
            val executed = action.invoke(p)
            if (executed){
                mutableData.value = p
                return Transaction.success(mutableData)
            }else{
                return Transaction.abort()
            }
        }

        override fun onComplete(error: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
            if (!b) onError.invoke()
            onComplete.invoke()
        }
    })
}

fun <T> Query.observeSingleEventList(clazz: Class<T>): Observable<List<T>> {
    return Observable.create<List<T>>({
        this.addListenerForSingleValueEvent(getEventListenerList(onError = { error ->
            it.onError(error)
        }, onDataChange = { list ->
            it.onNext(list)
        }, clazz = clazz))
    }, Emitter.BackpressureMode.BUFFER)
            .observeOn(Schedulers.computation())
}


fun <T> Query.observeSingleEvent(clazz: Class<T>): Observable<T> {
    return Observable.create<T>({
        this.addListenerForSingleValueEvent(getEventListener(onError = { error ->
            it.onError(error)
        }, onDataChange = { item ->
            it.onNext(item)
        }, clazz = clazz))
    }, Emitter.BackpressureMode.BUFFER)
            .observeOn(Schedulers.computation())
}

fun <T> getEventListener(onError: (Exception) -> Unit, onDataChange: (T) -> Unit, clazz: Class<T>): ValueEventListener {
    return object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.v("ERROR", "onCancelled", databaseError.toException())
            onError.invoke(kotlin.IllegalStateException("No values found or request cancelled"))
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val firebaseKey = dataSnapshot.key
            dataSnapshot.getValue(clazz)?.let {
                (it as FirebaseObject).firebaseKey = firebaseKey
                onDataChange.invoke(it)
            }
        }
    }
}

