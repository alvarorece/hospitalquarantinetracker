package es.uniovi.sdm.quarantinementalhealthtracker.ui.days.goals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import es.uniovi.sdm.quarantinementalhealthtracker.R
import es.uniovi.sdm.quarantinementalhealthtracker.data.Goal
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.GoalsFragmentBinding
import es.uniovi.sdm.quarantinementalhealthtracker.ui.NewGoalDialog
import es.uniovi.sdm.quarantinementalhealthtracker.ui.days.DayDetailViewModel

interface GoalController {
    fun removeGoal(goal: Goal)
    fun updateGoal(goal: Goal)
}

@AndroidEntryPoint
class GoalsFragment : Fragment(),
    NewGoalDialog.NewGoalDialogListener, GoalController {

    fun showDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = NewGoalDialog()
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, "NoticeDialogFragment")
    }

    override fun onDialogPositiveClick(goal: String) {
        dayViewModel.addGoal(goal)
    }

    private val dayViewModel: DayDetailViewModel by navGraphViewModels(R.id.day_graph)
    private lateinit var adapter: GoalAdapter
    private var _binding: GoalsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GoalsFragmentBinding.inflate(inflater, container, false)
        val root = binding.root
        adapter = GoalAdapter (this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goalsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.goalsRecycler.adapter = adapter
        val touchHelper = ItemTouchHelper(SwipeToDeleteGoal(adapter))
        touchHelper.attachToRecyclerView(binding.goalsRecycler)
        observeGoals()
        binding.floatingActionButton.setOnClickListener {
            showDialog()
        }
        binding.randomMovieButton.setOnClickListener {
            dayViewModel.createRandomMovieGoal()
        }
    }

    private fun observeGoals() {
        dayViewModel.goals.observe(viewLifecycleOwner, {
            adapter.updateAllGoals(it)
        })
    }

    override fun removeGoal(goal: Goal) {
        dayViewModel.removeGoal(goal)
        val snackbar = Snackbar.make(requireView(), "Undo", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") { undoRemove(goal) }
        snackbar.show()
    }

    private fun undoRemove(goal:Goal) {
        dayViewModel.updateGoal(goal, true)
    }

    override fun updateGoal(goal: Goal) = dayViewModel.updateGoal(goal)

}