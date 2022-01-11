package com.example.collegehelper.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.collegehelper.databinding.FragmentDialogBinding
import com.example.collegehelper.room.classItem.ClassItem


class MyDialog(private val listener: AddButtonClicked) : DialogFragment() {

    val CLASS_ADD_DIALOG = "addClass"
    val CLASS_UPDATE_DIALOG = "updateClass"
    val STUDENT_ADD_DIALOG = "addStudent"
    val STUDENT_UPDATE_DIALOG = "updateStudent"
    private lateinit var dialogBinding: FragmentDialogBinding


    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        var dialog: Dialog? = null
        dialogBinding = FragmentDialogBinding.inflate(layoutInflater)

        if (tag == CLASS_ADD_DIALOG) {
            dialog = getAddClassDialog()
        }
        when(tag) {
            CLASS_ADD_DIALOG -> {
                dialog = getAddClassDialog()
            }
            STUDENT_ADD_DIALOG -> {
                dialog = getAddStudentDialog()
            }

            STUDENT_UPDATE_DIALOG -> {
                dialog = getUpdateStudentDialog()
            }
        }

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun getAddStudentDialog(): Dialog? {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setView(dialogBinding.root)
        val title: TextView = dialogBinding.titleDialog
        title.text = "Add new Student"
        val nameEdit: EditText = dialogBinding.edt01
        val rollEdit: EditText = dialogBinding.edt02
        nameEdit.hint = "Enter Student Name"
        rollEdit.hint = "Enter Roll"
        val addBtn: Button = dialogBinding.addBtn
        val cancelBtn: Button = dialogBinding.cancelBtn
        cancelBtn.setOnClickListener { v -> dismiss() }
        addBtn.setOnClickListener { v ->
            val studentName = nameEdit.text.toString()
            val roll = rollEdit.text.toString()
            listener.onAddClicked(studentName, roll)
            dismiss()
        }
        return builder.create()

    }

    private fun getUpdateStudentDialog(): Dialog? {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setView(dialogBinding.root)
        val title: TextView = dialogBinding.titleDialog
        title.text = "Add new Student"
        val nameEdit: EditText = dialogBinding.edt01
        val rollEdit: EditText = dialogBinding.edt02
        nameEdit.hint = "Enter Student Name"
        rollEdit.hint = "Enter Roll"
        val addBtn: Button = dialogBinding.addBtn
        val cancelBtn: Button = dialogBinding.cancelBtn
        addBtn.text = "Update"
        cancelBtn.setOnClickListener { v -> dismiss() }
        addBtn.setOnClickListener { v ->
            val studentName = nameEdit.text.toString()
            val roll = rollEdit.text.toString()
            listener.onUpdateClicked(studentName, roll)
            dismiss()
        }
        return builder.create()

    }


    private fun getAddClassDialog(): Dialog? {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
        builder.setView(dialogBinding.root)
        val title: TextView = dialogBinding.titleDialog
        title.text = "Add new Class"
        val classEdit: EditText = dialogBinding.edt01
        val subjectEdit: EditText = dialogBinding.edt02
        classEdit.hint = "Enter Class"
        subjectEdit.hint = "Enter Subject"
        val addClassBtn: Button = dialogBinding.addBtn
        val cancelBtn: Button = dialogBinding.cancelBtn
        cancelBtn.setOnClickListener { v -> dismiss() }
        addClassBtn.setOnClickListener { v ->
            val className = classEdit.text.toString()
            val subName = subjectEdit.text.toString()
            listener.onAddClicked(className, subName)
            dismiss()
        }
        return builder.create()
    }
}

interface AddButtonClicked {
    fun onAddClicked(text01 : String, text02 : String)
    fun onUpdateClicked(text01: String,text02: String)
}