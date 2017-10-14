package com.arran.askaquestion.models

import com.google.firebase.database.Exclude

/**
 * Created by arran on 14/10/2017.
 */
open class FirebaseObject{
    @get:Exclude var firebaseKey: String = ""
}