package com.example.collegehelper.attendance.attendanceActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.collegehelper.repositories.StatusRepository
import com.example.collegehelper.repositories.StudentRepository
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.status.StatusDatabase
import com.example.collegehelper.room.student.Student
import com.example.collegehelper.room.student.StudentDatabase
import com.example.collegehelper.volleyDao.StatusDao
import com.example.collegehelper.volleyDao.StudentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentActivityViewModel(application: Application) : AndroidViewModel(application){

    private val studentRepository : StudentRepository
    private val statusRepository : StatusRepository
    val allStudentItems : LiveData<List<Student>>
    val allStatusItem : LiveData<List<Status>>
    private var onlineStudentDao: StudentDao
    private var onlineStatusDao: StatusDao

    init {
        val studentDao = StudentDatabase.getDatabase(application).getStudentDao()
        studentRepository = StudentRepository(studentDao)
        allStudentItems = studentRepository.allStudents

        val statusDao = StatusDatabase.getDatabase(application).getStatusDao()
        statusRepository = StatusRepository(statusDao)
        allStatusItem = statusRepository.allStatusItem

        onlineStudentDao = StudentDao(application)
        onlineStatusDao = StatusDao(application)
    }

    //Methods for inserting student in local database
    fun insertStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        studentRepository.insert(student)
    }

    fun updateStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        studentRepository.update(student)
    }

    fun deleteStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        studentRepository.delete(student)
    }

    fun getClassStudents(cid: Long) : LiveData<List<Student>> {
        return studentRepository.getClassStudent(cid)
    }


    //    Methods for inserting student in online database
    fun insertStudentOnline(student: Student, classMongoId: String) {
        GlobalScope.launch {
            onlineStudentDao.insertStudentOnline(student,classMongoId)
        }
    }

    fun updateStudentOnline(student: Student) {
        GlobalScope.launch {
            onlineStudentDao.updateStudentOnline(student)
        }
    }

    fun deleteStudentOnline(student: Student) {
        GlobalScope.launch {
            onlineStudentDao.deleteStudent(student)
        }
    }



    // status methods for local database
    fun insertStatus(status: Status) = viewModelScope.launch(Dispatchers.IO) {
        statusRepository.insert(status)
    }

    fun deleteStatus(status: Status) = viewModelScope.launch(Dispatchers.IO) {
        statusRepository.delete(status)
    }

    fun getAllStatus(cid: Long) : LiveData<List<Status>> {
        return statusRepository.getAllStatus(cid)
    }

    fun getStatus(cid: Long, sid: Long, dateKey: String) : LiveData<String> {
        return statusRepository.getStatus(cid, sid, dateKey)
    }

    fun getDateStatus(cid: Long, dateKey: String) : LiveData<List<Status>> {
        return statusRepository.getDateStatus(cid, dateKey)
    }

    // Online status methods

    fun insertStatusOnline(status: Status, classMongoId: String, studentMongoId: String) {
        GlobalScope.launch {
            onlineStatusDao.insertStatusOnline(status, classMongoId, studentMongoId)
        }
    }

    fun updateStatusOnline(status: Status) {
        GlobalScope.launch {
            onlineStatusDao.updateStatusOnline(status)
        }
    }
}