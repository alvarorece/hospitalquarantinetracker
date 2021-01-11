package es.uniovi.sdm.quarantinementalhealthtracker.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentStatisticsBinding.inflate(inflater,container, false)
        val tabLayout = binding.tabLayoutStats
        val viewPager = binding.pagerStats
        viewPager.adapter = StatisticsPagerAdapter(requireParentFragment())
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTitle(position)
        }.attach()
        return binding.root
    }

    private fun getTitle(pos: Int) = when (pos) {
        es.uniovi.sdm.quarantinementalhealthtracker.ui.days.GOALS_POSITION -> "Goals"
        es.uniovi.sdm.quarantinementalhealthtracker.ui.days.SURVEY_POSITION -> "Survey"
        else -> null
    }
}