package es.uniovi.sdm.quarantinementalhealthtracker.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import es.uniovi.sdm.quarantinementalhealthtracker.repository.UserRepository

class StatisticsViewModel : ViewModel() {
     val dayStats =
        UserRepository.getUserDayStatistics().asLiveData()
}