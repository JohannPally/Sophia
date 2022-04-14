package com.example.myapplication

import androidx.room.*

@Dao
interface CheckListDAO {

    @Query( "SELECT * FROM checklist_table WHERE parent = :pid ")
    fun findByParentID(pid: Int): Array<CheckListSQL>

    @Query( "SELECT * FROM checklist_table WHERE parent is null")
    fun findByParentIDTop(): Array<CheckListSQL>

    @Query("SELECT * FROM checklist_table WHERE id = :id LIMIT 1")
    fun findById(id: Int): CheckListSQL

    @Query("SELECT * FROM checklist_table WHERE name = :name LIMIT 1")
    fun findByName(name: String): CheckListSQL

    @Query("SELECT * FROM checklist_table")
    fun getAll(): Array<CheckListSQL>

    @Insert
    fun insertAll(checkLists: Array<CheckListSQL>)

    @Insert
    fun insert(checkList: CheckListSQL): Long

    @Delete
    fun delete(checkList: CheckListSQL)
}