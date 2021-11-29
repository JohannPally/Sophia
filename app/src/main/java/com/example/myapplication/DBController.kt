package com.example.myapplication

import android.content.Context
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

    //L1
    fun getCats(): Set<String> {
        return model.fragment_get_db() as Set<String>
    }

    fun addCat(cat:String){
        //might need to be Json input
    }

    fun dltCat(cat:String){
        //might need to be Json input
    }

    fun edtCat(cat:String){
        //might need to be Json input
    }


    //L2
    // TODO: Return a set of pairs containing status and name rather than just names
    fun getDevs(cat: String): Set<String> {
        return model.fragment_get_db(category = cat) as Set<String>
    }

    fun addDev(cat:String){
        //might need to be Json input
    }

    //something to move a device from one category to the other? Sounds like an L3 task

    //L3
    fun getInf(dev: Pair<String, String>): MaintenanceRecord {
        return model.fragment_get_db(
            category = dev.first,
            device = dev.second
        ) as MaintenanceRecord;
    }

    fun getInfFromQRId(id:String): MaintenanceRecord {
        var deviceInfo : QrCodeIdData  = model.fragment_get_id(id)
        return getInf(Pair(deviceInfo.category, deviceInfo.device));
    }

    fun getPathFromQRId(id:String): Pair<String, String> {
        var deviceInfo : QrCodeIdData  = model.fragment_get_id(id)
        return Pair(deviceInfo.category, deviceInfo.device);
    }

    private fun addNewDeviceQR(qrCodeId:String, dev:Pair<String, String>) {
        model.fragment_set_id(qrCodeId, dev.first, dev.second);
    }

    fun editInfo(dev:Pair<String, String>, newObj: MaintenanceRecord) {
        model.fragment_set_db(category = dev.first, device = dev.second, newObj);
    }

    fun setDeviceAsInactive(dev:Pair<String, String>) {
        var currentMR : MaintenanceRecord = getInf(dev)
        currentMR.status = "-1"
        model.fragment_set_db(category = dev.first, device = dev.second, currentMR);
    }

    fun addNewDevice(dev:Pair<String, String>, inventoryNum: String, workOrderNum: String, serviceProvider: String, serviceEngineeringCode: String, faultCode: String, ipmProcedure: String, status: String) {
        //val inventoryNum = dev.first.substring(0, 2) + dev.second.substring(0, 2) + String.format("%04d", (1..9999).random());
        if (inventoryNum != ""){
            addNewDeviceQR(qrCodeId = inventoryNum, dev = dev)
        }
        // Timestamp will be overwritten in fragment_set
        var newObj = MaintenanceRecord(inventoryNum, workOrderNum, serviceProvider, serviceEngineeringCode, faultCode, ipmProcedure, status, timestamp = 0);
        model.fragment_set_db(category = dev.first, device = dev.second, newObj);
    }

    fun removeDevice(dev:Pair<String, String>) {
        model.fragment_delete(category = dev.first, device = dev.second);
    }

    fun sync_updateDB(){
        model.sync()
        model.updateDB()
    }

}