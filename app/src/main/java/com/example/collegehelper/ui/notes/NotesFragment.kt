package com.example.collegehelper.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.databinding.FragmentNotesBinding
import com.example.collegehelper.room.notes.NoteClass
import com.example.collegehelper.ui.dialog.AddButtonClicked
import com.example.collegehelper.ui.dialog.MyDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class NotesFragment : Fragment(), AddButtonClicked, NoteClassItemClicked {

    private lateinit var notesViewModel: NotesViewModel
    private var _binding: FragmentNotesBinding? = null
    private lateinit var dialog: MyDialog
    private lateinit var addFab: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var classRecycler: RecyclerView
    private lateinit var noteClassAdapter: NoteClassAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notesViewModel =
            ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(requireActivity().application))[NotesViewModel::class.java]

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = MyDialog(this)
        addFab = binding.addBtn
        classRecycler = binding.recyclerView

        addFab.setOnClickListener {
            dialog.show(requireFragmentManager(), dialog.CLASS_ADD_DIALOG)
        }

        setupClassList()
    }

    private fun setupClassList() {
        noteClassAdapter = NoteClassAdapter(this)
        notesViewModel.allNoteItems.observe(viewLifecycleOwner) { list ->
            noteClassAdapter.updateClassItems(list as ArrayList<NoteClass>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {



        return super.onContextItemSelected(item)
    }

    override fun onAddClicked(text01: String, text02: String) {
        val noteClass = NoteClass(0,text01, text02)

        notesViewModel.insertNoteClass(noteClass)

        notesViewModel.insertNoteClassOnline(noteClass)
    }

    override fun onUpdateClicked(text01: String, text02: String) {

    }

    override fun onDeleteClicked() {

    }

    override fun onNoteClassItemClicked(noteClass: NoteClass) {

    }
}