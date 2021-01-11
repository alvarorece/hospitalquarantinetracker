package es.uniovi.sdm.quarantinementalhealthtracker.ui.days.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import es.uniovi.sdm.quarantinementalhealthtracker.R
import es.uniovi.sdm.quarantinementalhealthtracker.data.UserState
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentDayDetailSurveyBinding
import es.uniovi.sdm.quarantinementalhealthtracker.ui.days.DayDetailViewModel

class DayDetailSurvey : Fragment() {
    private var _binding: FragmentDayDetailSurveyBinding? = null
    private val binding get() = _binding!!
    private val dayViewModel: DayDetailViewModel by  navGraphViewModels(R.id.day_graph)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDayDetailSurveyBinding.inflate(inflater, container, false)
        binding.fillSurveyButton.setOnClickListener {
            view?.findNavController()?.navigate(dayViewModel.goToSurveyDirection())
        }
        dayViewModel.userDayState().observe(viewLifecycleOwner, {
            when (it) {
                UserState.GOOD -> binding.textView.text = getString(R.string.survey_severity_minimal)
                UserState.BAD -> binding.textView.text = getString(R.string.survey_severity_moderate)
                UserState.URGENT -> binding.textView.text = getString(R.string.survey_severity_severe)
                else -> binding.textView.text = getString(R.string.survey_take)
            }
        })
        return binding.root
    }
}