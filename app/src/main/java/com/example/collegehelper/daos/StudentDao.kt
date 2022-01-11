package com.example.collegehelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("Select * from student_table order by sid ASC")
    fun getAllStudentsItems() : LiveData<List<Student>>

    @Query("Select * from student_table where cid = :cid order by roll ASC")
    fun getClassStudents(cid: Long) : LiveData<List<Student>>
}