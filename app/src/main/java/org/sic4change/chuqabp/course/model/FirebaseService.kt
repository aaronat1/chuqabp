package org.sic4change.chuqabp.course.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object ChuqabpFirebaseService {
    val fbAuth = FirebaseAuth.getInstance()
    val mFirestore = FirebaseFirestore.getInstance()
}