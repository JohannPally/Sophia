package com.example.myapplication

import androidx.room.*
import java.util.logging.Level

@Dao
interface LevelsDAO {

    @Query( "SELECT * FROM levels_table WHERE parent = :pid ")
    fun findByParentID(pid: Int): Array<LevelSQL>

    @Query("SELECT * FROM levels_table WHERE parent is null")
    fun findByParentIDTop(): Array<LevelSQL>

    @Query("SELECT * FROM levels_table")
    fun getAll(): Array<LevelSQL>

    @Query("SELECT * FROM levels_table WHERE parent = :parentLevelId")
    fun getSubLevel(parentLevelId: Int): Array<LevelSQL>

    @Insert
    fun insertAll(levels: Array<LevelSQL>)

    @Insert
    fun insert(levels: LevelSQL)

    @Delete
    fun delete(level: LevelSQL)
}