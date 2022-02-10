package com.example.myapplication

import android.content.Context
import android.util.Log
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
        return model.fragment_get_db(EmptyPath()) as Set<String>
    }

    /*
    fun addCat(cat:String){
        //might need to be Json input
    }

    fun dltCat(cat:String){
        //might need to be Json input
    }

    fun edtCat(cat:String){
        //might need to be Json input
    }
     */


    //L2
    // TODO: Return a set of pairs containing status and name rather than just names
    fun getDevs(cat: String): Set<String> {
        return model.fragment_get_db(CategoryPath(cat))as Set<String>
    }

    /*
    fun addDev(cat:String){
        //might need to be Json input
    }
     */

    //something to move a device from one category to the other? Sounds like an L3 task
    //L3
    fun getInf(p: Path): MaintenanceRecord {
        //Log.d("pair", dev.toString());
        return model.fragment_get_db(p) as MaintenanceRecord;
    }

    /*
    fun getInfFromQRId(id:String): MaintenanceRecord {
        var deviceInfo : QrCodeIdData  = model.fragment_get_id(id)
        return getInf(Path(deviceInfo.category, deviceInfo.device))
       // return getInf(Pair(deviceInfo.category, deviceInfo.device));
    }
     */

    fun getPathFromQRId(id:String): DevicePath {
        var deviceInfo : QrCodeIdData  = model.fragment_get_id(id)
        return DevicePath(deviceInfo.category, deviceInfo.device)
    }

    private fun addNewDeviceQR(qrCodeId:String, p: DevicePath) {
        //TODO set p.category, p.device -> Path
        model.fragment_set_id(qrCodeId, p.category, p.device)
    }

    fun editInfo(p: DevicePath, mr: MaintenanceRecord) {
        model.fragment_set_db(p, mr)
    }

    /*
    fun setDeviceAsInactive(p: DevicePath) {
        var currentMR : MaintenanceRecord = getInf(p)
        currentMR.status = "-1"
        model.fragment_set_db(p, currentMR)
    }

     */

    fun addNewDevice(p: DevicePath, mr: MaintenanceRecord) {
        if (mr.id != ""){
            //TODO change != to !.equals
            addNewDeviceQR(qrCodeId = mr.id, p)
        }
        //TODO change frament set db to use path p
        model.fragment_set_db(p, mr);
    }

    /*
    fun removeDevice(dev:Pair<String, String>) {
        model.fragment_delete(category = dev.first, device = dev.second);
    }
     */

    fun sync_updateDB(){
        model.sync()
        model.updateDB()
    }

}