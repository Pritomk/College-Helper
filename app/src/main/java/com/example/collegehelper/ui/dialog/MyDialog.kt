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

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
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
        val addClassBtn: Button = dialogBinding.addClassBtn
        val cancelBtn: Button = dialogBinding.cancelBtn
        cancelBtn.setOnClickListener { v -> dismiss() }
        addClassBtn.setOnClickListener { v ->
            val className = classEdit.text.toString()
            val subName = subjectEdit.text.toString()
            listener.onClicked(className, subName)
            dismiss()
        }
        return builder.create()
    }
}

interface AddButtonClicked {
    fun onClicked(className : String, subjectName : String)
}