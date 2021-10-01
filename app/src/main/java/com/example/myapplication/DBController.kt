package com.example.myapplication

import android.content.Context
import com.google.gson.JsonObject
import java.security.AccessController.getContext

class DBController(context: Context) {

    // Instantiate a DB
    var model:DatabaseModel = DatabaseModel(context)

    //L1
    fun getCats(): Set<String>{
        var categorySet : Set<String>  = model.get() as Set<String>
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
    fun getDevs(cat:String): Set<String> {
        var deviceSet : Set<String>  = model.get(category = cat) as Set<String>
        return deviceSet
    }

    fun addDev(cat:String){
        //might need to be Json input
    }

    //something to move a device from one category to the other? Sounds like an L3 task

    //L3
    fun getInf(dev:Pair<String, String>): HashMap<String, String> {
        var deviceInfo : HashMap<String, String>  = model.get(category = dev.first, device = dev.second) as HashMap<String, String>
        return deviceInfo;
    }

    fun edtInfo(obj: JsonObject) {
        //model.put(obj)
    }


}