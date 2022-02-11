package com.example.collegehelper.ui.attendance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.collegehelper.SharedPreferenceClass
import com.example.collegehelper.attendance.attendanceActivity.AttendanceActivity
import com.example.collegehelper.databinding.FragmentAttendanceBinding
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.ui.dialog.AddButtonClicked
import com.example.collegehelper.ui.dialog.MyDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class AttendanceFragment : Fragment(), AddButtonClicked, ClassItemClicked {

    private lateinit var attendanceViewModel: AttendanceViewModel
    private var _binding: FragmentAttendanceBinding? = null
    private lateinit var fab : FloatingActionButton
    private lateinit var dialog: MyDialog
    private lateinit var classRecyclerView : RecyclerView
    private lateinit var sharedPreferenceClass: SharedPreferenceClass
    private lateinit var classItemList: ArrayList<ClassItem>
    private lateinit var updateClassItem: ClassItem
    private lateinit var deleteClassItem: ClassItem
    private val TAG = "com.example.collegehelper.ui.attendance.AttendanceFragment"

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
        sharedPreferenceClass = SharedPreferenceClass(requireContext())

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
        attendanceViewModel.allClassItems.observe(viewLifecycleOwner) {
            classItemList = it as ArrayList<ClassItem>
            classAdapter.updateClassItems(it)
        }
}

    private fun setupDialog() {
        dialog.show(requireFragmentManager(),dialog.CLASS_ADD_DIALOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAddClicked(text01: String, text02: String) {
        attendanceViewModel.insertClassItem(ClassItem(0,text01, text02))


        //Insert data to mongodb
        attendanceViewModel.insertClassOnline(text01,text02)
    }


    override fun onUpdateClicked(text01: String, text02: String) {

        //Update in local database
        updateClassItem.className = text01
        updateClassItem.subjectName = text02
        attendanceViewModel.updateClassItem(updateClassItem)

        //Update in online database

    }

    override fun onDeleteClicked() {
        //Delete in local database
        attendanceViewModel.deleteClassItem(deleteClassItem)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            0 ->
                showUpdateDialog(item.groupId)
            1 ->
                showDeleteDialog(item.groupId)
        }

        return super.onContextItemSelected(item)
    }

    private fun showDeleteDialog(groupId: Int) {
        deleteClassItem = classItemList[groupId]
        fragmentManager?.let { dialog.show(it, dialog.CLASS_DELETE_DIALOG) }
    }

    private fun showUpdateDialog(groupId: Int) {
        updateClassItem = classItemList[groupId]
        fragmentManager?.let { dialog.show(it, dialog.CLASS_UPDATE_DIALOG) }
    }

    override fun onClassItemClicked(item: ClassItem) {
        val intent = Intent(activity,AttendanceActivity::class.java)
        intent.putExtra("className",item.className)
        intent.putExtra("subjectName",item.subjectName)
        intent.putExtra("cid",item.cid)
        startActivity(intent)
    }
}