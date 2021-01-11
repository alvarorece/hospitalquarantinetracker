package es.uniovi.sdm.quarantinementalhealthtracker.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.*
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentGoalStatisticsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [GoalStatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalStatisticsFragment : Fragment() {
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private var _binding: FragmentGoalStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalStatisticsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticsViewModel.dayStats.observe(viewLifecycleOwner, {
            val goalEntries = it.sortedBy { x -> x.timeStamp }
                ?.mapIndexed { idx, x ->
                    BarEntry(idx.toFloat(), x.doneGoals.toFloat())
                }
            val goalsSet = BarDataSet(goalEntries, "Done goals")
            val goalsChart = binding.goalsChart
            goalsChart.data = BarData(goalsSet)
            goalsChart.invalidate()
        })
    }
}