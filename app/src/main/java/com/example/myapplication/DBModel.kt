package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

import android.net.ConnectivityManager
import android.os.Build
import android.util.Log

private var dbFilename: String = "database.json"
private var idFilename: String = "id.json"
private val serverURL:String = "http://10.0.2.2:4567"

class DatabaseModel(context: Context) {

    lateinit var database : HashMap<String, HashMap<String, MaintenanceRecord>>
    lateinit var idData : HashMap<String, QrCodeIdData>
    val context: Context = context
    var cm : ConnectivityManager
    var logs = arrayListOf<Pair<String, String>>()
    init {
        this.cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        createBothFromFile(context)
    }

    /*
    Startup and shutdown file operations
     */
    private fun saveToLocalFile(jsonOutput: String, filename: String) {
        println("DIR: " + context.filesDir)
        val file = File(context.filesDir, filename)
        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonOutput)
        bufferedWriter.close()
    }

    private fun createBothFromFile(context: Context) {
        try {
            val file = File(context.filesDir, dbFilename)
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
            val hashMapType: Type = object : TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
            val readDB: HashMap<String, HashMap<String, MaintenanceRecord>> = Gson().fromJson(jsonInput, hashMapType)
            this.database = readDB
        } catch (e: FileNotFoundException) {
            saveToLocalFile("{}", dbFilename)
            this.database = hashMapOf();
            updateDB();
        }
        try {
            val idFile = File(context.filesDir, idFilename)
            val idFileReader = FileReader(idFile)

            val idBufferedReader = BufferedReader(idFileReader)
            val idStringBuilder = StringBuilder()
            val idAllLines: List<String> = idBufferedReader.readLines()
            for (line in idAllLines) {
                idStringBuilder.append(line).append("\n")
            }
            idBufferedReader.close()

            val idJsonInput = idStringBuilder.toString()
            val idHashMapType: Type = object : TypeToken<HashMap<String, QrCodeIdData>?>() {}.type
            val readIds: HashMap<String, QrCodeIdData> = Gson().fromJson(idJsonInput, idHashMapType)
            Log.v("HIT THIS", "TAP")
            this.idData = readIds

            //return readDB;
        } catch (e: FileNotFoundException) {
            saveToLocalFile("{}", idFilename)
            this.idData = hashMapOf();
            updateDB();
        }
    }

    /*
    Getters and Setters for SharedPreferences
     */
    fun getAuthKey(): String {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        return prefs.getString(context.getString(R.string.authkey_key), "Error Retrieving AuthKey")!!
    }

    fun setAuthKey(newAuthKey: String) {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        var editor = prefs.edit()
        editor.putString(context.getString(R.string.authkey_key), newAuthKey)
        editor.apply()
    }

    fun getBackendUrl(): String {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        return prefs.getString(context.getString(R.string.url_key), "Error Retrieving URL")!!
    }

    fun setBackendUrl(newURL: String) {
        val prefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        var editor = prefs.edit()
        editor.putString(context.getString(R.string.url_key), newURL)
        editor.apply()
    }

    /*
    Getters and Setters for our DatabaseModel class relating to the ID databasae
     */

    fun fragment_get_id(id: String): QrCodeIdData {
        val idValue = idData[id]
        if (idValue != null) {
            return idValue
        } else {
            error("Error fetching data from ID $id")
        }
    }


    fun fragment_set_id(id: String, category: String = "", device: String = "") {
        val qrIdData = QrCodeIdData(category, device)

        idData[id] = qrIdData

        val fullIdJson = Gson().toJson(idData)
        println("SAVING ID DATA FILE")
        saveToLocalFile(fullIdJson, idFilename)
    }



    /*
    Getters and Setters for our DatabaseModel class relating to the main Database
     */

    fun fragment_get_db(category: String = "", device: String = ""): Any? {
        if (category != "") {
            if (device != "") {
                val catMap = database[category]
                if (catMap != null) {
                    return catMap[device]
                } else {
                    error("Category Map is null ")
                }
            } else {
                val catMap = database[category]
                if (catMap != null) {
                    // TODO: Modify to return a pairing of Device Name and Status
                    val deviceKeys = catMap.keys
                    val statusAndDevices = ArrayList<Pair<String, String>>()
                    for (key in deviceKeys) {
                        val currentDevice = catMap[key]
                        if (currentDevice != null) {
                            statusAndDevices.add(Pair(key, currentDevice.status))
                        }
                    }
                    println("STATUS: $statusAndDevices")
                    return deviceKeys
                }
                else {
                    error("Category Map is null (second conditional)")
                }
            }
        } else if (category == "" && device == "") {
            return database.keys
        } else {
            error("Device Info is null")
        }
    }

    fun fragment_set_db(category: String = "", device: String = "", MR: MaintenanceRecord) {
        var json = Gson().toJson(MR)
        // TODO: Unhardcode the auth string
        json = "{\"Key\": \"SAHTesting449496\", \"Data\": $json}";

        MR.timestamp = (System.currentTimeMillis() / 1000).toInt()

        database[category]?.put(device, MR)

        val fullDBJson = Gson().toJson(database)
        println("SAVING DB FILE")
        saveToLocalFile(fullDBJson, dbFilename)

        val url = "$serverURL/DB/$category/$device/"
//        post_server(url, json)
        logging(url, json)
    }

    fun fragment_delete(category: String = "", device: String = "") {

        database[category]?.remove(device)

        delete_server("$serverURL/DB/$category/$device")

        val fullDBJson = Gson().toJson(database)
        Log.d("SAVING FILE", "fragment_delete")
        saveToLocalFile(fullDBJson, dbFilename)
    }


//==============================BACKEND Functions ===============================================
    fun logging(url: String, json: String) {
        logs.add(Pair(url, json))
        sync()
    }

    fun sync() {
        Thread {
            while (isOnline() && logs.size > 0) {
                val log = logs.removeAt(0)
                val response = post_server(log.first, log.second)
                if (response != 200) {
                    logs.add(0, log)
                    break
                }
            }
        }.start()
    }


    fun post_server(url:String, json:String) : Int {
        Log.i("postServer0:",url)
        val mURL = URL(url)
        Log.i("postServer0.5:",mURL.toString())
        val reqParam = json
        var code:Int
        Log.i("postServer1:",json)

        var urlc:HttpURLConnection = mURL.openConnection() as HttpURLConnection
        try {
            urlc.connectTimeout = (10*1000)
            urlc.requestMethod = "POST"
            val wr = OutputStreamWriter(urlc.outputStream)
            wr.write(reqParam)
            wr.flush()
            urlc.connect()
            Log.i("postServer1.5:",urlc.responseCode.toString())

        } catch (e: IOException) {
            Log.i("postServer1.5:","404")
            return 404
        }
        with(urlc) {
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


    fun delete_server(url:String) : String {
        Log.i("Delete Server0:",url)
        val mURL = URL(url)
        Log.i("Delete Server0.5:",mURL.toString())
        val out = StringBuffer()
        var code:Int

        var urlc:HttpURLConnection = mURL.openConnection() as HttpURLConnection
        try {
            urlc.connectTimeout = (10*1000)
            urlc.requestMethod = "DELETE"
            urlc.connect()
            Log.i("Delete Server1.5:",urlc.responseCode.toString())

        } catch (e: IOException) {
            Log.i("Delete Server1.5:","404")
            return ""
        }
        with(urlc) {

            println("URL : $url")
            println("Response Code : $responseCode")
            code = responseCode

            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    out.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $out")
            }
        }
        return out.toString();
    }

    fun get2_server(url:String) : String {
        Log.i("Get Server0:",url)
        val mURL = URL(url)
        Log.i("Get Server0.5:",mURL.toString())
        val out = StringBuffer()
        var code:Int

        var urlc:HttpURLConnection = mURL.openConnection() as HttpURLConnection
        try {
            urlc.connectTimeout = (10*1000)
            urlc.requestMethod = "GET"
            urlc.connect()
            Log.i("Get Server1.5:",urlc.responseCode.toString())

        } catch (e: IOException) {
            Log.i("Get Server1.5:","404")
            return ""
        }
        with(urlc) {

            println("URL : $url")
            println("Response Code : $responseCode")
            code = responseCode

            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    out.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $out")
            }
        }
        return out.toString();
    }

    fun get_server(url:String): String {
        return get2_server(url);
    }

    fun isOnline(): Boolean {
        val net = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.cm.activeNetwork
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
            Thread {
                val newDB = get_server("$serverURL/DB/")
                if (newDB.isNotEmpty()) {
                    val hashMapType: Type =
                        object :
                            TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
                    val readDB: HashMap<String, HashMap<String, MaintenanceRecord>> =
                        Gson().fromJson(newDB, hashMapType)
                    this.database = readDB

                    val fullDBJson = Gson().toJson(database)
                    Log.d("SAVING Web to FILE", "updateDB")
                    saveToLocalFile(fullDBJson, dbFilename)
                }
            }.start()
        }

    }

    /**
     * Update ID Data with backend IDs
     */
    fun updateIDs() {
        if (isOnline()) {
            Thread {
                val newDB = get_server("$serverURL/ID/")
                if (newDB.isNotEmpty()) {
                    val hashMapType: Type =
                        object :
                            TypeToken<HashMap<String, QrCodeIdData>?>() {}.type
                    val readDB: HashMap<String, QrCodeIdData> =
                        Gson().fromJson(newDB, hashMapType)
                    this.idData = readDB

                    val fullIDJson = Gson().toJson(idData)
                    Log.d("SAVING Web to FILE", "updatedIDs")
                    saveToLocalFile(fullIDJson, idFilename)
                }
            }.start()
        }
    }
}

