package es.uniovi.sdm.quarantinementalhealthtracker.data.survey


import java.util.*

data class FilledSurvey(
    val date: Date?,
    val type: String,
    val answers: List<Int>?
) {
    constructor() : this( Date(), "", emptyList()) {
    }
}
