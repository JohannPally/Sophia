package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
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
    lateinit var database : HashMap<Any?,Any?>

    init {
        createFromFile(context)
    }

    /*
    Startup and shutdown file operations
     */

    fun saveToLocalFile(context: Context, jsonOutput: String) {
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, "database.json")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
    }

    fun createFromFile(context: Context) {
        try {
            val file = File(context.filesDir, "database.json")
            val fileReader = FileReader(file)

            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            val allLines: List<String> = bufferedReader.readLines()
            for (line in allLines) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()

            val jsonInput = stringBuilder.toString()
            val hashMapType: Type = object : TypeToken<HashMap<Any?, Any?>?>() {}.type
            val readDB: HashMap<Any?, Any?> = Gson().fromJson(jsonInput, hashMapType)
            this.database = readDB;
            //return readDB;
        } catch (e: FileNotFoundException) {
            val db = HashMap<Any?, Any?>()
            this.database = db;
            saveToLocalFile(context, Gson().toJson(database).toString());
            //return database
        }
    }

    /*
    Getters and Setters for our DeviceModel class
     */

    fun get(lst: List<String>): Any {
        var tlst = lst
        var out = database
        while(tlst.size > 0){
            var k = tlst.get(0)
            tlst = tlst.drop(1)

            out = out.get(k) as HashMap<Any?, Any?>
        }

        return out
    }



    fun put() {
        //TODO efficient HashMap searching?
        //contains might only do categories? so then would we have to do a contains of each category?
    }

    fun modify(lst: List<String>, key: String, value: Any ?) {
        //TODO double check this is the way we're entering in and pulling
        var tlst = lst
        var out = database
        while(tlst.size > 0){
            var k = tlst.get(0)
            tlst = tlst.drop(1)

            out = out.get(k) as HashMap<Any?, Any?>
        }

        out.set(key, value)
    }
}