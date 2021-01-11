package es.uniovi.sdm.quarantinementalhealthtracker.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import es.uniovi.sdm.quarantinementalhealthtracker.ui.home.HomeAdapter

class SwipeToDelete(private val adapter : HomeAdapter) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or (ItemTouchHelper.RIGHT)) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.deleteItem(viewHolder.adapterPosition)
    }

}