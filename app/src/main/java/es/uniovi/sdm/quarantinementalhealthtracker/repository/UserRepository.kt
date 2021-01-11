package es.uniovi.sdm.quarantinementalhealthtracker.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import es.uniovi.sdm.quarantinementalhealthtracker.data.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

object UserRepository {
    private val TAG = "USER_REPO"
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocument = db.collection("users")
        .document(getCurrentUserUid())
    private val currentUserDays = currentUserDocument.collection("days")

    fun getCurrentUserUid(): String {
        val u = firebaseAuth.currentUser
        return u?.uid.toString();
    }

    suspend fun saveUser(user: User, uid: String): Boolean {
        var success: Boolean = false
        db.collection("users")
            .document(uid).set(user).addOnSuccessListener {
                success = true
            }.addOnFailureListener {
                success = false
            }.await()
        return success
    }

    fun setDay(day: Day) = db.collection("users")
        .document(getCurrentUserUid()).collection("days")
        .document(day.id).set(day)

    suspend fun newDay(): Boolean {

        var success = false
        val timeStamp = Date()
        val ref = currentUserDays.document()
        ref.set(Day(ref.id, null, timeStamp, 0)).addOnSuccessListener {
            success = true
        }.addOnFailureListener {
            success = false
        }.await()
        return success
    }

    fun getUserDays(): Flow<List<Day>> {
        return callbackFlow {
            val listenerRegistration = currentUserDays
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException
                : FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching days",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents
                        ?.mapNotNull { it.toObject(Day::class.java) }
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        } as Flow<List<Day>>
    }

    fun getUserDayStatistics(): Flow<List<DayStatistics>> {
        return callbackFlow {
            val listenerRegistration = currentUserDays
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException
                : FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching days",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toObject(Day::class.java) }
                    val result = map?.map {
                        DayStatistics(
                            it.timeStamp,
                            it.doneGoals ?: 0,
                            it.filledSurvey?.answers?.sum() ?: 0
                        )
                    }
                    offer(result)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        } as Flow<List<DayStatistics>>
    }


}