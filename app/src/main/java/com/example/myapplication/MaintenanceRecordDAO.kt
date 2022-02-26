package com.example.myapplication

import androidx.room.*

@Dao
interface MaintenanceRecordDAO {
    @Query("SELECT * FROM mr_table WHERE id = :id LIMIT 1")
    fun findById(id: Int): MaintenanceRecordSQL

    @Query("SELECT * FROM mr_table")
    fun getAll(): Array<MaintenanceRecordSQL>

    @Insert
    fun insertAll(maintenanceRecords: Array<MaintenanceRecordSQL>)

    @Insert
    fun insert(maintenanceRecord: MaintenanceRecordSQL)

    @Delete
    fun delete(maintenanceRecord: MaintenanceRecordSQL)
}