package es.uniovi.sdm.quarantinementalhealthtracker.data

interface Evaluator {
    fun evaluate(answers : List<Int>): UserState
}