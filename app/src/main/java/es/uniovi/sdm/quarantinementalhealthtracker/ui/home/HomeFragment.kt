package es.uniovi.sdm.quarantinementalhealthtracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.uniovi.sdm.quarantinementalhealthtracker.data.Day
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentHomeBinding
import es.uniovi.sdm.quarantinementalhealthtracker.ui.SwipeToDelete
import kotlinx.coroutines.launch
import java.util.*


interface DayController {
    fun removeDay(day: Day)
    fun undoRemove(day: Day)
}
class
HomeFragment : Fragment(), DayController {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        adapter = HomeAdapter(this, requireContext())
        lifecycleScope.launch {
            homeViewModel.startQuarantineIfNew()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        val touchHelper = ItemTouchHelper(SwipeToDelete(adapter))
        touchHelper.attachToRecyclerView(binding.recyclerview)
        binding.fabAddDay.setOnClickListener { homeViewModel.addDay() }
        observeDays()
    }

    private fun observeDays() {
        homeViewModel.days.observe(viewLifecycleOwner, {
            adapter.setListData(it.toMutableList())
            adapter.notifyDataSetChanged()
        })
    }

    override fun removeDay(day: Day) {
        homeViewModel.removeDay(day.id)
        val snackbar = Snackbar.make(requireView(), "Undo", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") { undoRemove(day) }
        snackbar.show()
    }


    override fun undoRemove(day: Day) {
        homeViewModel.setDay(day)
    }
}