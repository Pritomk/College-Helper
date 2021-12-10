package com.example.collegehelper.ui.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.collegehelper.databinding.FragmentAssignmentBinding

class AssignmentFragment : Fragment() {

    private lateinit var assignmentViewModel: AssignmentViewModel
    private var _binding: FragmentAssignmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        assignmentViewModel =
            ViewModelProvider(this).get(AssignmentViewModel::class.java)

        _binding = FragmentAssignmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        assignmentViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}