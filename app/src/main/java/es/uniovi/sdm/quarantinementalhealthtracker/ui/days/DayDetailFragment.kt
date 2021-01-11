package es.uniovi.sdm.quarantinementalhealthtracker.ui.days

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import es.uniovi.sdm.quarantinementalhealthtracker.R
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.DayDetailFragmentBinding
import javax.inject.Inject


@AndroidEntryPoint
class DayDetailFragment : Fragment() {

    private val arg: DayDetailFragmentArgs by navArgs()

    @Inject
    lateinit var dayViewModelFactory: DayDetailViewModel.AssistedFactory

    private val dayViewModel: DayDetailViewModel by navGraphViewModels(R.id.day_graph) {
        DayDetailViewModel.provideFactory(
            dayViewModelFactory,
            arg.dayId
        )
    }

    private var _binding: DayDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DayDetailFragmentBinding.inflate(inflater, container, false)
        val root = binding.root
        val tabLayout = binding.tabLayout
        val viewPager = binding.pager
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getBoolean("disableSurvey", false)
        viewPager.adapter = DayPagerAdapter(requireParentFragment())
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTitle(position)
        }.attach()
        if (name) (tabLayout.getTabAt(SURVEY_POSITION)!!.view as LinearLayout).visibility = View.GONE
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Test", dayViewModel.toString())
        val result = arg.finishedSurveyResult
        if (result != null) {
            dayViewModel.saveSurvey(result.asList())
        }
    }

    private fun getTitle(pos: Int) = when (pos) {
        GOALS_POSITION -> "Goals"
        SURVEY_POSITION -> "Survey"
        else -> null
    }

}