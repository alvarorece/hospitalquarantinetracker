package es.uniovi.sdm.quarantinementalhealthtracker.data

import com.google.firebase.firestore.DocumentId

data class Goal(
    @DocumentId
    val id: String,
    var body: String?,
    var isDone: Boolean
)
{
    constructor() : this("","", false){}
}