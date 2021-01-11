package es.uniovi.sdm.quarantinementalhealthtracker.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentSurveyStatisticsBinding

class SurveyStatisticsFragment : Fragment() {
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private var _binding: FragmentSurveyStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSurveyStatisticsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticsViewModel.dayStats.observe(viewLifecycleOwner, {
            val surveyEntries = it.sortedBy { x -> x.timeStamp }
                ?.mapIndexed { idx, x ->
                    Entry(idx.toFloat(), x.surveyValue.toFloat())
                }
            val surveySet = LineDataSet(surveyEntries, "Done Surveys")
            val surveysChart = binding.surveyChart
            surveysChart.data = LineData(surveySet)
            surveysChart.invalidate()
        })
    }
}