package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

import android.net.ConnectivityManager
import android.os.Build


private var filename: String = "database.json"
private val serverURL:String = "127.0.0.1:4567"

//TODO NOTES
//1. we're only going to have a single file with all the devices and categories
//2. upon startup, we just need to check whether database.json exists -> make if not
//3. need search helper functions for L1 and L2 and get for L3 (consider a pair<str,str> input)
//4. Taking care of time logging actions

class DatabaseModel(context: Context) {
    lateinit var database : HashMap<String, HashMap<String, MaintenanceRecord>>
    val context: Context = context;
    var cm : ConnectivityManager
    var logs = arrayListOf<Pair<String, String>>()
    init {
        createFromFile(context)
        this.cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /*
    Startup and shutdown file operations
     */

    private fun saveToLocalFile(jsonOutput: String) {
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
            println("DB: $jsonInput")
            val hashMapType: Type = object : TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
            val readDB: HashMap<String, HashMap<String, MaintenanceRecord>> = Gson().fromJson(jsonInput, hashMapType)
            this.database = readDB
            println("DB2: ${this.database.keys}")
            println("DB3: ${this.database["Surgical ICU"]?.get("Surgical Masks")}")
            println("DB4: ${this.database["Surgical ICU"]?.get("Surgical Masks")?.inventoryNum}")

            //return readDB;
        } catch (e: FileNotFoundException) {
            saveToLocalFile("{\n" +
                    "  \"Surgical ICU\": {\n" +
                    "    \"Surgical Masks\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": 0" +
                    "    },\n" +
                    "    \"Syringes\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": 1" +
                    "    },\n" +
                    "    \"Blood Pressure Cuffs\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": 1" +
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
                    "      \"status\": 1" +
                    "    },\n" +
                    "    \"Pulse Oximeters\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": 0" +
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
                    "      \"status\": 2" +
                    "    },\n" +
                    "    \"Defibrillators\": {\n" +
                    "      \"inventoryNum\": \"1234\",\n" +
                    "      \"workOrderNum\": \"64\",\n" +
                    "      \"serviceProvider\": \"MedTech\",\n" +
                    "      \"serviceEngineeringCode\": \"504\",\n" +
                    "      \"faultCode\": \"300\",\n" +
                    "      \"ipmProcedure\": \"Lorem Ipsum sit dolar...\",\n" +
                    "      \"status\": 2" +
                    "    }\n" +
                    "  }\n" +
                    "}")
            //return database
            return createFromFile(context)
        }
    }

    /*
    Getters and Setters for our DatabaseModel class
     */

    fun fragment_get(category: String = "", device: String = ""): Any? {
        if (category != "") {
            if (device != "") {
                val catMap = database.get(category)
                if (catMap != null) {
                    return catMap.get(device)
                } else {
                    error("devices L ")
                }
            } else {
                val catMap = database.get(category)
                if (catMap != null) {
                    // TODO: Modify to return a pairing of Device Name and Status
                    val deviceKeys = catMap.keys
                    val statusAndDevices = ArrayList<Pair<String, String>>()
                    for (key in deviceKeys) {
                        val currentDevice = catMap.get(key)
                        if (currentDevice != null) {
                            statusAndDevices.add(Pair(key, currentDevice.status))
                        }
                    }
                    println("STATUS: $statusAndDevices")
                    return deviceKeys
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
        return null
    }

    fun fragment_set(category: String = "", device: String = "", MR: MaintenanceRecord) {
        val json = Gson().toJson(MR)
        val cat = database.get(category)

        MR.timestamp = (System.currentTimeMillis() / 1000).toInt()

        if (cat != null) {
            cat.put(device, MR)
        }

        val fullDBJson = Gson().toJson(database)
        println("SAVING FILE")
        saveToLocalFile(fullDBJson);

        val url = serverURL + "/DB/" + category + "/" + device
//        post_server(url, json)
        logging(url, json)
    }


//==============================BACKEND FXNS===============================================
    fun logging(url: String, json: String) {
        logs.add(Pair(url, json))
        sync()
    }

    fun sync() {
        while (isOnline() && logs.size > 0) {
            var log = logs.removeAt(0);
            var response = post_server(log.first, log.second)
            if (response != 200) {
                logs.add(0, log)
                break
            }
        }
    }


    fun post_server(url:String, json:String) : Int {
        val mURL = URL(url)

        val reqParam = URLEncoder.encode(json, "UTF-8")
        var code = 0
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream())
            wr.write(reqParam)
            wr.flush()

            println("URL : $url")
            println("Response Code : $responseCode")
            code = responseCode

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
        return code
    }

    fun get_server(url:String): String {
        return URL(url).readText()
    }

    fun isOnline(): Boolean {
        val net = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.cm.getActiveNetwork()
        } else {
            this.cm.getActiveNetworkInfo()
        }
        return net != null
    }

    /**
     * Update DB with backendDB
     */
    fun updateDB() {
        if (isOnline()) {
            val newDB = get_server("$serverURL/DB/")
            val hashMapType: Type =
                object : TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
            val readDB: HashMap<String, HashMap<String, MaintenanceRecord>> =
                Gson().fromJson(newDB, hashMapType)
            this.database = readDB
        }
    }
}

