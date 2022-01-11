package com.example.collegehelper.repositories

import androidx.lifecycle.LiveData
import com.example.collegehelper.daos.StudentDao
import com.example.collegehelper.room.student.Student

class StudentRepository(private val studentDao: StudentDao) {

    val allStudents = studentDao.getAllStudentsItems()


    suspend fun insert(student: Student) {
        studentDao.insert(student)
    }

    suspend fun update(student: Student) {
        studentDao.update(student)
    }

    suspend fun delete(student: Student) {
        studentDao.delete(student)
    }

    fun getClassStudent(cid: Long) : LiveData<List<Student>> {
        return studentDao.getClassStudents(cid)
    }
}