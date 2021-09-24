package com.example.myapplication

import com.google.gson.JsonObject

class DBController {

    var model: DeviceModel = DeviceModel("Medical", "Syringe", 50);

    //L1
    fun getCats(): JsonObject{
        //model.get()
        return JsonObject()
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
    fun getDevs(cat:String): JsonObject{
        //make JsonObject out of s?
        //model.get(s ?)
        return JsonObject()
    }

    fun addDev(cat:String){
        //might need to be Json input
    }

    //something to move a device from one category to the other? Sounds like an L3 task

    //L3
    fun getInf(dev:Pair<String, String>): JsonObject{
        //first string would be the category
        //TODO: pass through the category name from L2 -> L3 when we figure out Lists
        //make JsonObject out of s?
        //model.get(s ?)
        return JsonObject()
    }

    fun edtInfo(obj: JsonObject) {
        //model.put(obj)
    }


}