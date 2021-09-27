package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import java.io.*
import java.lang.StringBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private var filename: String = "database.json"

//TODO NOTES
//1. we're only going to have a single file with all the devices and categories
//2. upon startup, we just need to check whether database.json exists -> make if not
//3. need search helper functions for L1 and L2 and get for L3 (consider a pair<str,str> input)
//4. Taking care of time logging actions

class DatabaseModel(context: Context) {
    lateinit var database : HashMap<String, HashMap<String, HashMap<String, String>>>

    init {
        createFromFile(context)
    }

    /*
    Startup and shutdown file operations
     */

    private fun saveToLocalFile(context: Context, jsonOutput: String) {
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, "database.json")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
    }

    private fun createFromFile(context: Context) {
        try {
            //val file = File(context.filesDir, "database.json")
            val file = File( "app/java/com/example/myapplication/exDB.json")
            val fileReader = FileReader(file)

            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            val allLines: List<String> = bufferedReader.readLines()
            for (line in allLines) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()

            val jsonInput = stringBuilder.toString()
            val hashMapType: Type = object : TypeToken<HashMap<String, HashMap<String, HashMap<String, String>>>?>() {}.type
            val readDB: HashMap<String, HashMap<String, HashMap<String, String>>> = Gson().fromJson(jsonInput, hashMapType)
            this.database = readDB;
            //return readDB;
        } catch (e: FileNotFoundException) {
            val db = HashMap<String, HashMap<String, HashMap<String, String>>>()
            this.database = db;
            saveToLocalFile(context, Gson().toJson(database).toString());
            println("are we here")
            //return database
        }
    }

    /*
    Getters and Setters for our DatabaseModel class
     */

    fun get(lst: List<String>): Any? {
        val s = lst.size
        when(s) {
            0 -> {
                return database.keys
            }
            1 -> {
                var catName = lst.get(0)
                var catMap = database.get(catName)
                if (catMap != null) {
                    return catMap.keys
                }
                else {
                    error("categories L")
                }
            }
            2 -> {
                var catName = lst.get(0)
                var catMap = database.get(catName)
                if (catMap != null) {
                    var devMap = catMap.get(lst.get(1))
                }
                else {
                    error("devices L ")
                }
            }
            else -> {
                error("device info L")
            }
        }
        return null
    }



    fun put() {
        //TODO efficient HashMap searching?
        //contains might only do categories? so then would we have to do a contains of each category?
    }

    fun modify(lst: List<String>, key: String, value: Any ?) {
//        //TODO double check this is the way we're entering in and pulling
//        var tlst = lst
//        var out = database
//        while(tlst.size > 0){
//            var k = tlst.get(0)
//            tlst = tlst.drop(1)
//            out = out.get(k)
//        }
//
//        out.set(key, value)
    }
}

