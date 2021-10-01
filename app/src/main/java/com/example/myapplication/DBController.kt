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