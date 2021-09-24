package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import java.io.*
import java.lang.StringBuilder

private var filename: String = "database.json"

//TODO NOTES
//1. we're only going to have a single file with all the devices and categories
//2. upon startup, we just need to check whether database.json exists -> make if not
//3. need search helper functions for L1 and L2 and get for L3 (consider a pair<str,str> input)

data class DeviceModel(var category: String, var deviceName: String, var count: Int) {
    //TODO: Make Device Model with no arguments
    //Essentially DeviceModel needs to be able to check whether a database.json file already exists
    //and otherwise create one.
    fun saveToLocalFile(context: Context) {
        var jsonOutput = Gson().toJson(this)
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, category + "_" + deviceName + ".json")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
    }

    fun getTest() : String {
        return "test"
    }
}

fun createFromFile(context: Context, category: String, deviceName: String): DeviceModel? {
    val file: File = File(context.filesDir, category + "_" + deviceName + ".json")
    val fileReader = FileReader(file)
    val bufferedReader = BufferedReader(fileReader)
    val stringBuilder = StringBuilder()
    val allLines: List<String> = bufferedReader.readLines()
    for (line in allLines) {
        stringBuilder.append(line).append("\n")
    }
    bufferedReader.close()
    val jsonInput = stringBuilder.toString()
    return Gson().fromJson(jsonInput, DeviceModel::class.java);
}