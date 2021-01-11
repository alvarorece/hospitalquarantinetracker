package es.uniovi.sdm.quarantinementalhealthtracker.data

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.DocumentId
import es.uniovi.sdm.quarantinementalhealthtracker.data.survey.FilledSurvey
import java.util.*

@IgnoreExtraProperties
data class Day(
    @DocumentId
    val id: String,
    val filledSurvey: FilledSurvey?,
    val timeStamp : Date?,
    val doneGoals : Int?
) {
    constructor() : this("", null, null, 0) {
    }
}