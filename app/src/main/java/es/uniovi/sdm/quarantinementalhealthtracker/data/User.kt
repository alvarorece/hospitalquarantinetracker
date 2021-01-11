package es.uniovi.sdm.quarantinementalhealthtracker.data

import com.google.firebase.firestore.DocumentId
import java.util.*

data class User(
    @DocumentId
    val id: String,
    val name: String,
    val quarantineStart: Date
) {
    constructor() : this("", "", Date())
}

