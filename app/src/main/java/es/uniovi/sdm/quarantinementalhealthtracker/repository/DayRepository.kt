package es.uniovi.sdm.quarantinementalhealthtracker.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import es.uniovi.sdm.quarantinementalhealthtracker.data.Day
import es.uniovi.sdm.quarantinementalhealthtracker.data.Goal
import es.uniovi.sdm.quarantinementalhealthtracker.data.UserState
import es.uniovi.sdm.quarantinementalhealthtracker.data.survey.FilledSurvey
import es.uniovi.sdm.quarantinementalhealthtracker.data.survey.PHQ9
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object DayRepository {
    private val TAG = "DAY_REPO"
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocument = db.collection("users")
        .document(UserRepository.getCurrentUserUid())
    private val currentUserDays = currentUserDocument.collection("days")

    fun setGoalCurrentUser(dayId: String, goal: Goal, isUndo: Boolean) {
        if (!isUndo)
            if (goal.isDone) incrementDayGoals(dayId) else decrementDayGoals(dayId)
        else
            if (goal.isDone) incrementDayGoals(dayId)
        currentUserDays
            .document(dayId).collection("goals").document(goal.id).set(goal)
    }

    suspend fun getCurrentUserState(dayId: String): UserState? {
        return try {
            val day = currentUserDays.document(dayId).get().await()
                .toObject(Day::class.java)
            if (day?.filledSurvey?.answers == null)
                return null
            return PHQ9.evaluate(day.filledSurvey.answers)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user state", e)
            null
        }
    }

    suspend fun addGoalToDay(dayId: String, description: String): Boolean {
        var success = false
        val ref = currentUserDays.document(dayId)
            .collection("goals").document()
        ref.set(Goal(ref.id, description, false)).addOnSuccessListener {
            success = true
        }.addOnFailureListener {
            success = false
        }.await()
        return success
    }

    fun removeGoal(dayId: String, goal: Goal) {
        if (goal.isDone) decrementDayGoals(dayId)
        currentUserDays.document(dayId).collection("goals").document(goal.id).delete()
    }

    suspend fun addSurveyToDay(dayId: String, survey: FilledSurvey): Boolean {
        var success = false
        val data = hashMapOf("filledSurvey" to survey)
        currentUserDays
            .document(dayId).set(data, SetOptions.merge()).addOnSuccessListener {
                success = true
            }.addOnFailureListener {
                success = false
            }.await()
        return success
    }

    fun removeDayFromCurrentUser(dayId: String) = currentUserDays.document(dayId).delete()

    private fun incrementDayGoals(dayId: String) =
        currentUserDays.document(dayId)
            .update("doneGoals", FieldValue.increment(1))

    private fun decrementDayGoals(dayId: String) =
        currentUserDays.document(dayId)
            .update("doneGoals", FieldValue.increment(-1))

    @ExperimentalCoroutinesApi
    fun getCurrentUserDayGoals(dayId: String): Flow<List<Goal>> {
        return callbackFlow {
            val listenerRegistration =
                currentUserDays.document(dayId).collection("goals")
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException
                    : FirebaseFirestoreException? ->
                        if (firebaseFirestoreException != null) {
                            cancel(
                                message = "Error fetching day goals",
                                cause = firebaseFirestoreException
                            )
                            return@addSnapshotListener
                        }
                        val map = querySnapshot?.documents
                            ?.mapNotNull { it.toObject(Goal::class.java) }
                        offer(map)
                    }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        } as Flow<List<Goal>>
    }
}