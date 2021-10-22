package com.example.myapplication

import android.content.Context
import com.google.gson.JsonObject

class DBController(context: Context) {

    // Instantiate a DB
    var model:DatabaseModel = DatabaseModel(context)

    //L1
    fun getCats(): Set<String>{
        var categorySet : Set<String>  = model.fragment_get() as Set<String>
        return categorySet
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
    fun getDevs(cat:String): Set<String> {
        var deviceSet : Set<String>  = model.fragment_get(category = cat) as Set<String>
        return deviceSet
    }

    fun addDev(cat:String){
        //might need to be Json input
    }

    //something to move a device from one category to the other? Sounds like an L3 task

    //L3
    // TODO: Change return type for this function to a DataClass
    fun getInf(dev:Pair<String, String>): MaintenanceRecord {
        var deviceInfo : MaintenanceRecord  = model.fragment_get(category = dev.first, device = dev.second) as MaintenanceRecord
        return deviceInfo;
    }

    fun editInfo(dev:Pair<String, String>, newObj: MaintenanceRecord) {
        model.fragment_set(category = dev.first, device = dev.second, newObj);
    }


}