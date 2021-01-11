package es.uniovi.sdm.quarantinementalhealthtracker.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.sdm.quarantinementalhealthtracker.R
import es.uniovi.sdm.quarantinementalhealthtracker.data.Day
import es.uniovi.sdm.quarantinementalhealthtracker.data.UserState
import es.uniovi.sdm.quarantinementalhealthtracker.data.survey.PHQ9
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.RecyclerViewItemBinding
import java.text.DateFormat


class HomeAdapter(private val dayController : DayController, private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var list = mutableListOf<Day>()
    fun setListData(data: MutableList<Day>) {
        list = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val day = list[position]
        holder.bindView(day)
    }

    fun deleteItem(pos: Int) {
        dayController.removeDay(list[pos])
    }

    override fun getItemCount() = list.size

    inner class HomeViewHolder(private val itemBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                itemBinding.day?.let { day ->
                    openDay(day, it)
                }
            }
        }

        fun bindView(day: Day) =
            with(itemBinding) {
                itemBinding.day = day
                textSurvey.text = if (day.filledSurvey != null) "Filled" else "No survey"
                textGoals.text = DateFormat.getDateInstance().format(day.timeStamp)
                var state = day.filledSurvey?.answers?.let { PHQ9.evaluate(it) }
                when(state){
                    UserState.GOOD -> root.setBackgroundColor(getColor(context, R.color.good))
                    UserState.BAD ->root.setBackgroundColor(getColor(context, R.color.bad))
                    UserState.URGENT -> root.setBackgroundColor(getColor(context, R.color.urgent))
                    else -> root.setBackgroundColor(getColor(context, R.color.white))
                }
            }

        private fun openDay(
            day: Day,
            view: View
        ) {
            val direction = HomeFragmentDirections.actionNavHomeToDayDetailFragment(day.id)
            view.findNavController().navigate(direction)
        }

    }
}