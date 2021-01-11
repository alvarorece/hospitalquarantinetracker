package es.uniovi.sdm.quarantinementalhealthtracker.ui.days

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.uniovi.sdm.quarantinementalhealthtracker.ui.days.survey.DayDetailSurvey
import es.uniovi.sdm.quarantinementalhealthtracker.ui.days.goals.GoalsFragment

const val GOALS_POSITION = 0
const val SURVEY_POSITION = 1

class DayPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        GOALS_POSITION to { GoalsFragment() },
        SURVEY_POSITION to { DayDetailSurvey() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}