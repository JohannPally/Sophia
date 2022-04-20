package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MaintenanceRecordSQL::class, LevelSQL::class, CheckListSQL::class, TaskSQL::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun maintenanceRecordDAO(): MaintenanceRecordDAO
    abstract fun levelsDAO(): LevelsDAO
    abstract fun CheckListDAO(): CheckListDAO
    abstract fun TaskSQLDAO(): TaskDAO

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,
                    "cmms_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                println("HERE")
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: AppDatabase) {
//            println("POPULATING")
//            val mrDao = db.maintenanceRecordDAO()
//            val levelsDao = db.levelsDAO()
//            val topLevel = LevelSQL(0, "first level", null);
//            levelsDao.insert(topLevel)
//            mrDao.insert(MaintenanceRecordSQL(0,"first sql device", "1", null, null, null, null, 1, 12, topLevel.id))
//            mrDao.insert(MaintenanceRecordSQL(0,"second sql device", "1", null, null, null, null, 1, 12, topLevel.id))
//            println(mrDao.getAll());
//            println(levelsDao.getTopLevel());
        }
    }



}