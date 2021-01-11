package es.uniovi.sdm.quarantinementalhealthtracker.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import es.uniovi.sdm.quarantinementalhealthtracker.databinding.FragmentNewGoalDialogBinding

class NewGoalDialog : DialogFragment() {
    private lateinit var listener: NewGoalDialogListener
    interface NewGoalDialogListener {
        fun onDialogPositiveClick(goal: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = targetFragment as NewGoalDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NewGoalDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val binding = FragmentNewGoalDialogBinding.inflate(inflater)

            builder.setView(binding.root)
                .setPositiveButton("Add"
                ) { _, _ ->
                    listener.onDialogPositiveClick(binding.goalDescription.text.toString())
                }
                .setNegativeButton("cancel"
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}