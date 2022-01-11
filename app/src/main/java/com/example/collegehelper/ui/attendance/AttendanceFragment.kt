package com.example.collegehelper.ui.attendance

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.attendance.attendanceActivity.AttendanceActivity
import com.example.collegehelper.databinding.FragmentAttendanceBinding
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.ui.dialog.AddButtonClicked
import com.example.collegehelper.ui.dialog.MyDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AttendanceFragment : Fragment(), AddButtonClicked, ClassItemClicked {

    private lateinit var attendanceViewModel: AttendanceViewModel
    private var _binding: FragmentAttendanceBinding? = null
    private lateinit var fab : FloatingActionButton
    private lateinit var dialog: MyDialog
    private lateinit var classRecyclerView : RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceViewModel =
            ViewModelProvider(this, AttendanceViewModelFactory(requireActivity().application)).get(AttendanceViewModel::class.java)

        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fab = binding.addBtn
        classRecyclerView = binding.recyclerView
        dialog = MyDialog(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener { setupDialog() }

        setupRecyclerView()
    }

//    Setup the recyclerview
    private fun setupRecyclerView() {
        val classAdapter = ClassAdapter(this)
        classRecyclerView.adapter = classAdapter
        classRecyclerView.layoutManager = LinearLayoutManager(activity)
        attendanceViewModel.allClassItems.observe(viewLifecycleOwner,{
            classAdapter.updateClassItems(it as ArrayList<ClassItem>)
        })
    }

    private fun setupDialog() {
        dialog.show(requireFragmentManager(),dialog.CLASS_ADD_DIALOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAddClicked(className: String, subjectName: String) {
        attendanceViewModel.insertClassItem(ClassItem(0,className, subjectName))
    }

    override fun onUpdateClicked(text01: String, text02: String) {

    }

    override fun onClassItemClicked(item: ClassItem) {
        val intent = Intent(activity,AttendanceActivity::class.java)
        intent.putExtra("className",item.className)
        intent.putExtra("subjectName",item.subjectName)
        intent.putExtra("cid",item.cid)
        startActivity(intent)
    }
}