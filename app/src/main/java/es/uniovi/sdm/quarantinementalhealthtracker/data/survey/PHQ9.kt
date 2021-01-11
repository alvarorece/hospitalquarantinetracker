package es.uniovi.sdm.quarantinementalhealthtracker.data.survey

import es.uniovi.sdm.quarantinementalhealthtracker.data.UserState
import es.uniovi.sdm.quarantinementalhealthtracker.data.Evaluator

object PHQ9 : Evaluator {
    override fun evaluate(answers: List<Int>): UserState {
        val r = answers.fold(0, { acc: Int, answer: Int -> acc + answer })
        return when {
            r <= 4 -> UserState.GOOD
            r < 15 -> UserState.BAD
            else -> UserState.URGENT
        }
    }
}