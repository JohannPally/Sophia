package com.example.myapplication

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "mr_table", foreignKeys = [ForeignKey(entity = LevelSQL::class,
    parentColumns = ["id"],
    childColumns = ["parent"],
    onDelete = CASCADE)])
data class MaintenanceRecordSQL(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "device_name") val deviceName: String,
    @ColumnInfo(name = "work_order_num") val workOrderNum: String,
    @ColumnInfo(name = "service_provider") val serviceProvider: String?,
    @ColumnInfo(name = "service_engineering_code") val serviceEngineeringCode: String?,
    @ColumnInfo(name = "fault_code") val faultCode: String?,
    @ColumnInfo(name = "ipm_procedure") val ipmProcedure: String?,
    @ColumnInfo(name = "status") val status: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Int,
    @ColumnInfo(name = "parent") val parent: Int?
)

@Entity(tableName = "levels_table", foreignKeys = [ForeignKey(entity = LevelSQL::class,
    parentColumns = ["id"],
    childColumns = ["parent"],
    onDelete = CASCADE)])

data class LevelSQL(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "level_name") val levelName: String,
    @ColumnInfo(name = "parent") val parent: Int?,
)

@Entity(tableName = "checklist_table", foreignKeys = [ForeignKey(entity = CheckListSQL::class,
    parentColumns = ["id"],
    childColumns = ["parent"],
    onDelete = CASCADE)])

data class CheckListSQL(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "parent") val parent: Int?, //id of MR
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cycle") val cycle: Int,
    @ColumnInfo(name = "startdate") val startdate: String, //start date
)

@Entity(tableName = "tasks_table", foreignKeys = [ForeignKey(entity = TaskSQL::class,
    parentColumns = ["id"],
    childColumns = ["parent"],
    onDelete = CASCADE)])

data class TaskSQL(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "parent") val parent: Int?, //id of CheckList
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "status") val status: Int,
    @ColumnInfo(name = "updatedate") val updatedate: String, //last updated
)