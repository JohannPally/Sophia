package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import java.io.*
import java.lang.StringBuilder

data class DeviceModel(var category: String, var deviceName: String, var count: Int) {
    fun saveToLocalFile(context: Context) {
        var jsonOutput = Gson().toJson(this)
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, category + "_" + deviceName + ".json")
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
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

fun getTest() : String {
    return "test"
}