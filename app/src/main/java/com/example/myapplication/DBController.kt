package com.example.myapplication

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class DBController(context: Context) {

    // Instantiate a DB
    var model:DatabaseModel = DatabaseModel(context)

    init {
        val secondsPerUpdate:Long = 60
        val countdown: Long = 1000 * secondsPerUpdate;
        val timer: Timer = Timer("Auto_Sync")
        timer.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    sync_updateDB()
                }

            },0, countdown
        )
    }

    //L1 Call Using SQL
    fun getCats(): Set<String> {
        return model.getCatsfromDB()
    }


    //L2
    // TODO: Return a set of pairs containing status and name rather than just names
    fun getDevs(cat: String): Set<String> {
        return model.fragment_get_db(CategoryPath(cat))as Set<String>
    }

//    // L2 call using SQL
//    fun SQLgetDevices(category: String): Set<String> {
//        return model.getDevicesfromDB()
//    }

    //L3
    fun getInf(p: Path): MaintenanceRecord {
        return model.fragment_get_db(p) as MaintenanceRecord
    }

    fun get_level_table(parent:Int): Set<LevelSQL>{
        return model.getLevelTable(parent)
    }

    fun get_mr_table(parent:Int): Set<MaintenanceRecordSQL>{
        return model.getMRTable(parent)
    }

    fun get_search_qr(qrID: Int): Int{
        return model.getMRIDfromQRID(qrID)
    }

    fun get_all(parent:Int): Pair<Set<LevelSQL>, Set<MaintenanceRecordSQL>>{
        //Log.v("Get all parent", parent.toString())
        return Pair(get_level_table(parent), get_mr_table(parent))
    }

    fun get_mr(id: Int):MaintenanceRecordSQL{
        return model.getMR(id)
    }

    fun getCheckList(id: Int): CheckListSQL {
        return model.getCheckList(id)
    }

    fun getTasks(id: Int): Array<TaskSQL> {
        return model.getTask(id)
    }

    fun forceCheckList(id: Int){
        model.initializeDefaultChecklists(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun add_mr(qrid:Int, devName:String, workOrderNum: String, serviceProvider: String, status: Int, parent:Int): Int {
        var pID = model.addMaintenanceRecord(
            qrid = qrid,
            deviceName = devName,
            workOrderNum = workOrderNum,
            serviceProvider = serviceProvider,
            serviceEngineeringCode = "",
            faultCode = "",
            ipmProcedure = "",
            status = status,
            timeStamp = 0,
            parent = parent
        )
        model.getNewMRsHelper()
        return pID
    }

    fun updateMR(columnName: String, newValue: Any, mr_id: Int) {
        model.updateMRFieldInServer(columnName = columnName, newValue = newValue, mr_id = mr_id)
    }

//    // L3 call using SQl
//    fun getMaintenanceRecord(category: String, deviceName: String) : MaintenanceRecord {
//        return model.getMaintenanceRecord(category, deviceName)
//    }


    fun getPathFromQRId(id:String): DevicePath? {
        var deviceInfo : QrCodeIdData? = model.fragment_get_id(id)
        if (deviceInfo != null) {
            return DevicePath(deviceInfo.category, deviceInfo.device)
        }
        return null
    }

    private fun addNewDeviceQR(qrCodeId:String, p: DevicePath) {
        model.fragment_set_id(qrCodeId, p.category, p.device)
    }

    fun editInfo(p: DevicePath, mr: MaintenanceRecord) {
        model.fragment_set_db(p, mr)
    }

    fun addNewDevice(p: DevicePath, mr: MaintenanceRecord) {
        if (mr.id != ""){
            //TODO change != to !.equals
            addNewDeviceQR(qrCodeId = mr.id, p)
        }
        //TODO change frament set db to use path p
        model.fragment_set_db(p, mr);
    }


    public fun sync_updateDB(){
//        model.sync()
//        model.updateDB()
        model.syncSQLLogsToServer()
        model.getNewMRsHelper()
    }

}