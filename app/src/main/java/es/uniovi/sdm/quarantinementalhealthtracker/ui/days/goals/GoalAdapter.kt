package es.uniovi.sdm.quarantinementalhealthtracker.ui.days.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.sdm.quarantinementalhealthtracker.data.Goal
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.RecyclerGoalItemBinding

class GoalAdapter(private val controller: GoalController) :
    RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {
    private var goals: List<Goal> = emptyList()

    fun updateAllGoals(list: List<Goal>) {
        goals = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding =
            RecyclerGoalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding, controller)
    }

    fun deleteItem(pos: Int){
        controller.removeGoal(goals[pos])
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.bindView(goal)
    }

    override fun getItemCount() = goals.size

    class GoalViewHolder(private val itemBinding: RecyclerGoalItemBinding, private val controller: GoalController) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindView(goal: Goal){
            with(itemBinding) {
                goalCheckBox.text = goal.body
                goalCheckBox.isChecked = goal.isDone
                goalCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    goal.isDone = isChecked
                    controller.updateGoal(goal)
                }
            }
        }
    }
}