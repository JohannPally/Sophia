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

class DatabaseModel(context: Context) {
    //TODO: Make Device Model with no arguments
    //Essentially DeviceModel needs to be able to check whether a database.json file already exists
    //and otherwise create one.

    lateinit var database : HashMap<Any?, Any?>

    init {
        createFromFile(context)
    }

    /*
    Getters and Setters for our DeviceModel class
     */

    fun saveToLocalFile(context: Context, jsonOutput: String) {
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, "database.json")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
    }

    fun createFromFile(context: Context): Map<*, *> {
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
            return readDB;
        } catch (e: FileNotFoundException) {
            val database = HashMap<Any?, Any?>()
            this.database = database;
            saveToLocalFile(context, Gson().toJson(database).toString());
            return database
        }
    }
}