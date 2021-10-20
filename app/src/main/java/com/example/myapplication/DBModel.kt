package com.example.myapplication

import android.companion.CompanionDeviceManager
import android.content.Context
import com.google.gson.Gson
import java.io.*
import java.lang.StringBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private var filename: String = "database.json"
private val serverURL:String = "localhost:4567";

//TODO NOTES
//1. we're only going to have a single file with all the devices and categories
//2. upon startup, we just need to check whether database.json exists -> make if not
//3. need search helper functions for L1 and L2 and get for L3 (consider a pair<str,str> input)
//4. Taking care of time logging actions

class DatabaseModel(context: Context) {
    lateinit var database : HashMap<String, HashMap<String, MaintenanceRecord>>

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
            val file = File(context.filesDir, "database.json")
//            val file = File( "app/java/com/example/myapplication/exDB.json")
            val fileReader = FileReader(file)

            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            val allLines: List<String> = bufferedReader.readLines()
            for (line in allLines) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()

            val jsonInput = stringBuilder.toString()
            println("DB: $jsonInput");
            val hashMapType: Type = object : TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
            val readDB: HashMap<String, HashMap<String, MaintenanceRecord>> = Gson().fromJson(jsonInput, hashMapType)
            this.database = readDB;
            println("DB2: ${this.database.keys}");
            println("DB3: ${this.database["Surgical ICU"]?.get("Surgical Masks")}");
            println("DB4: ${this.database["Surgical ICU"]?.get("Surgical Masks")?.inventoryNum}");

            //return readDB;
        } catch (e: FileNotFoundException) {
            saveToLocalFile(context, "{\n" +
                    "  \"Surgical ICU\": {\n" +
                    "    \"Surgical Masks\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    },\n" +
                    "    \"Syringes\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    },\n" +
                    "    \"Blood Pressure Cuffs\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Neonatal Ward\": {\n" +
                    "    \"Incubators\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    },\n" +
                    "    \"Pulse Oximeters\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Cardiology Ward\": {\n" +
                    "    \"EKG Machines\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    },\n" +
                    "    \"Defibrillators\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": \"Caution\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            //return database
            return createFromFile(context);
        }
    }

    /*
    Getters and Setters for our DatabaseModel class
     */

    fun fragment_get(category: String = "", device: String = ""): Any? {
        if (category != "") {
            if (device != "") {
                var catMap = database.get(category)
                if (catMap != null) {
                    return catMap.get(device);
                } else {
                    error("devices L ")
                }
            } else {
                val catMap = database.get(category)
                if (catMap != null) {
                    // TODO: Modify to return a pairing of Device Name and Status
                    val deviceKeys = catMap.keys;
                    val statusAndDevices = ArrayList<Pair<String, String>>();
                    for (key in deviceKeys) {
                        var currentDevice = catMap.get(key);
                        if (currentDevice != null) {
                            statusAndDevices.add(Pair(key, currentDevice.status))
                        }
                    }
                    println("STATUS: $statusAndDevices");
                    return deviceKeys;
                }
                else {
                    error("categories L")
                }
            }
        } else if (category == "" && device == "") {
            return database.keys
        } else {
            error("device info L")
        }
        return null;
    }

    fun fragment_set(category: String = "", device: String = "", MR: MaintenanceRecord) {
        val json = Gson().toJson(MR)
        database.get(category)
        val url = serverURL + "/DB/" + category + "/" + device;
        post_server(url, json)
    }


//==============================BACKEND FXNS===============================================

    fun post_server(url:String, json:String) {
        val mURL = URL(url)

        var reqParam = URLEncoder.encode(json, "UTF-8")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $response")
            }
        }
    }

    fun get_server(url:String): String {
        return URL(url).readText();
    }

}
