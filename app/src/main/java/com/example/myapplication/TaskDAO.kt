package com.example.myapplication

import androidx.room.*

@Dao
interface TaskDAO {

    @Query( "SELECT * FROM tasks_table WHERE parent = :pid ")
    fun findByParentID(pid: Int): Array<TaskSQL>

    @Query( "SELECT * FROM tasks_table WHERE parent is null")
    fun findByParentIDTop(): Array<TaskSQL>

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    fun findById(id: Int): Array<TaskSQL>

    @Query("SELECT * FROM tasks_table WHERE name = :name LIMIT 1")
    fun findByName(name: String): TaskSQL

    @Query("SELECT * FROM tasks_table")
    fun getAll(): Array<TaskSQL>

    @Insert
    fun insertAll(tasks: Array<TaskSQL>)

    @Insert
    fun insert(task: TaskSQL): Long

    @Update
    fun pushUpdate(task: TaskSQL)

    @Delete
    fun delete(task: TaskSQL)
}