package es.uniovi.sdm.quarantinementalhealthtracker.data

import java.util.*

data class DayStatistics(
    val timeStamp: Date?, val doneGoals: Int, val surveyValue: Int
)
