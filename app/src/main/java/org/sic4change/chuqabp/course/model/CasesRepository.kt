package org.sic4change.chuqabp.course.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import timber.log.Timber

class CasesRepository {

    /**
     * Method to get cases
     */
    suspend fun getCases(): List<Case> {
        lateinit var cases : List<Case>
        return withContext(Dispatchers.IO) {
            try {
                val db = ChuqabpFirebaseService.mFirestore
                val casesRef = db.collection("cases")
                val query = casesRef.whereEqualTo("latitude", 0)
                val result = query.get().await()
                val networkUserContainer = NetworkCasesContainer(result.toObjects(Case::class.java))
                cases = networkUserContainer.results
            } catch (ex: Exception) {
                Timber.d("Get user result: error ${ex.cause}")
            }
            return@withContext cases
        }
    }

}

