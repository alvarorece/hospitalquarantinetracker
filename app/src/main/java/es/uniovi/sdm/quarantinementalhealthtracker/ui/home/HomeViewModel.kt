package es.uniovi.sdm.quarantinementalhealthtracker.ui.home

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import es.uniovi.sdm.quarantinementalhealthtracker.data.User
import es.uniovi.sdm.quarantinementalhealthtracker.repository.UserRepository
import es.uniovi.sdm.quarantinementalhealthtracker.data.Day
import es.uniovi.sdm.quarantinementalhealthtracker.repository.DayRepository
import kotlinx.coroutines.launch
import java.util.*



class HomeViewModel : ViewModel() {
    val days: LiveData<List<Day>> =
        UserRepository.getUserDays().asLiveData()

    suspend fun startQuarantineIfNew(): Boolean {
        val uuid = FirebaseAuth.getInstance().currentUser?.uid
        val displayName = FirebaseAuth.getInstance().currentUser?.displayName
        var saved = false
        if (uuid != null) {
            val user = User(uuid, displayName ?: "", quarantineStart = Date())
            saved = UserRepository.saveUser(user, UserRepository.getCurrentUserUid())
        }
        return saved
    }

    fun removeDay(dayId: String) = DayRepository.removeDayFromCurrentUser(dayId)
    fun setDay(day: Day) = UserRepository.setDay(day)

    fun addDay(): Boolean {
        val uuid = FirebaseAuth.getInstance().currentUser?.uid
        var done = false
        if (uuid != null)
            viewModelScope.launch {
                done = UserRepository.newDay()
            }
        return done
    }

}