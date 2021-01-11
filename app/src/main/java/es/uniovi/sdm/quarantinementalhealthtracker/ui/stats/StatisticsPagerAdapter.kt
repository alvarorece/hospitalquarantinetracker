package es.uniovi.sdm.quarantinementalhealthtracker.ui.stats

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val GOALS_POSITION = 0
const val SURVEY_POSITION = 1

class StatisticsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        GOALS_POSITION to { GoalStatisticsFragment() },
        SURVEY_POSITION to { SurveyStatisticsFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int) =
        tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()

}