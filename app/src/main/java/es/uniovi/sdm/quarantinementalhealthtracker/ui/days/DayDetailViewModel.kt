package es.uniovi.sdm.quarantinementalhealthtracker.ui.days

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import es.uniovi.sdm.quarantinementalhealthtracker.DayGraphDirections
import es.uniovi.sdm.quarantinementalhealthtracker.data.Goal
import es.uniovi.sdm.quarantinementalhealthtracker.data.UserState
import es.uniovi.sdm.quarantinementalhealthtracker.data.survey.FilledSurvey
import es.uniovi.sdm.quarantinementalhealthtracker.repository.DayRepository
import es.uniovi.sdm.quarantinementalhealthtracker.repository.MovieRepository
import kotlinx.coroutines.launch
import java.util.*

class DayDetailViewModel @AssistedInject constructor(
    @Assisted private val dayId: String
) : ViewModel() {
    val goals = DayRepository.getCurrentUserDayGoals(dayId).asLiveData()

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(dayId: String): DayDetailViewModel
    }

    fun addGoal(description: String): Boolean {
        var done = false
        viewModelScope.launch {
            done = DayRepository.addGoalToDay(dayId, description)
        }
        return done
    }

    fun updateGoal(goal: Goal, isUndo: Boolean = false) =
        DayRepository.setGoalCurrentUser(dayId, goal, isUndo)

    fun removeGoal(goal: Goal) = DayRepository.removeGoal(dayId, goal)

    fun userDayState(): LiveData<UserState> {
        val result = MutableLiveData<UserState>()
        viewModelScope.launch {
            result.postValue(
                DayRepository.getCurrentUserState(dayId)
            )
        }
        return result
    }

    fun createRandomMovieGoal() {
        viewModelScope.launch {
            val title = MovieRepository.getRandomMovieTitle()
            addGoal("Watch $title")
        }
    }

    fun goToSurveyDirection() = DayGraphDirections.actionGlobalSurveyFragment(dayId)


    fun saveSurvey(results: List<Int>, type: String = "PHQ9"): Boolean {
        val survey = FilledSurvey(Date(), type, results)
        var done = false
        viewModelScope.launch {
            done = DayRepository.addSurveyToDay(dayId, survey)
        }
        return done
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            dayId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(dayId) as T
            }
        }
    }
}