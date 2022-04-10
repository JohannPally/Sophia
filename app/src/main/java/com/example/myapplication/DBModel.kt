package com.example.myapplication


import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private var dbFilename: String = "database.json"
private var idFilename: String = "id.json"
private var templatesFilename: String = "templates.json"
private val serverURL:String = "http://10.0.2.2:4567"
private val netport =  4567
lateinit var connection: Connection

//TODO NOTES
//1. we're only going to have a single file with all the devices and categories
//2. upon startup, we just need to check whether database.json exists -> make if not
//3. need search helper functions for L1 and L2 and get for L3 (consider a pair<str,str> input)
//4. Taking care of time logging actions

class DatabaseModel(context: Context) {

    lateinit var database : HashMap<String, HashMap<String, MaintenanceRecord>>
    lateinit var idData : HashMap<String, QrCodeIdData>
    lateinit var templates : HashMap<String, DeviceTemplate>

    // Access to the Data Table
    var testDB = MainActivity.testDB

    val context: Context = context
    var cm : ConnectivityManager
    var logs = arrayListOf<Pair<String, String>>()
    init {
        this.cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val dbHashMapType: Type = object : TypeToken<HashMap<String, HashMap<String, MaintenanceRecord>>?>() {}.type
        val idHashMapType: Type = object : TypeToken<HashMap<String, QrCodeIdData>?>() {}.type
        val templateHashMapType: Type = object : TypeToken<HashMap<String, DeviceTemplate>?>() {}.type
        MainActivity.testDB = AppDatabase.getInstance(context)
        try {
            this.database =
                createFromFile(
                    context,
                    dbFilename,
                    dbHashMapType
                ) as HashMap<String, HashMap<String, MaintenanceRecord>>
        } catch (e: FileNotFoundException) {
            // If there isn't a file (Most likely first boot), retrieve DB file from server
            this.database = hashMapOf()
            this.updateDB()
        }
        try {
            this.idData =
                createFromFile(
                    context,
                    idFilename,
                    idHashMapType
                ) as HashMap<String, QrCodeIdData>
        } catch (e: FileNotFoundException) {
            // If there isn't a file (Most likely first boot), retrieve ID file from server
            this.idData = hashMapOf()
            this.updateIDs()
        }
        try {
            this.templates =
                createFromFile(
                    context,
                    templatesFilename,
                    templateHashMapType
                ) as HashMap<String, DeviceTemplate>
        } catch (e: FileNotFoundException) {
            // If there isn't a file (Most likely first boot), retrieve ID file from server
            this.templates = hashMapOf()
            this.updateTemplates()
        }
        println(this.templates)
        getServerConnection()
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

    private fun createFromFile(context: Context, filename: String, hashMapType: Type) : Any {
        try {
            val file = File(context.filesDir, filename)
            val fileReader = FileReader(file)

            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            val allLines: List<String> = bufferedReader.readLines()
            for (line in allLines) {
                stringBuilder.append(line).append("\n")
            }
            bufferedReader.close()

            val jsonInput = stringBuilder.toString()
            return Gson().fromJson(jsonInput, hashMapType)
        } catch (e: FileNotFoundException) {
            saveToLocalFile("{}", dbFilename)
            throw e;
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
    fun fragment_get_id(id: String): QrCodeIdData? {
        val idValue = idData[id]
        if (idValue != null) {
            return idValue
        } else {
            Log.v("Error get data from ID $id", "ERROR")
            return null
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

    fun fragment_get_db(p: Path): Any? {
        if(p is EmptyPath){
            return database.keys
        }
        else if(p is CategoryPath){
            val catMap = database[p.category]
            if (catMap != null) {
                /*
                val statusAndDevices = ArrayList<Pair<String, String>>()
                for (key in deviceKeys) {
                    val currentDevice = catMap[key]
                    if (currentDevice != null) {
                        statusAndDevices.add(Pair(key, currentDevice.status))
                    }
                }
                println("STATUS: $statusAndDevices")
                 */
                return catMap.keys
            }
            else {
                error("Category Map is null (second conditional)")
            }
        }
        else if(p is DevicePath){
            val catMap = database[p.category]
            if (catMap != null) {
                return catMap[p.device]
            } else {
                error("Category Map is null ")
            }
        }
        else
            error("Path is not type Empty, Category, or Device Path")
        /*
        if (category != "") {
            if (device != "") {
                val catMap = database[category]
                if (catMap != null) {
                    //Log.d("is it here?", catMap[device].toString())
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

         */
    }

    fun getLevelTable(parent:Int):Set<LevelSQL> {
        // Behavior: If parent is null, it returns a set of LevelSQLs whose parent ID is null
        // If parent is non-null, it returns a set of LevelSQLs with the given parent ID
        return if (parent != -1) {
            MainActivity.testDB.levelsDAO().findByParentID(parent).toSet()
        } else {
            MainActivity.testDB.levelsDAO().findByParentIDTop().toSet()
        }
    }

    fun getMRTable(parent:Int):Set<MaintenanceRecordSQL>{
        // Behavior: If parent is null, it returns a set of MRs whose parent ID is null
        // If parent is non-null, it returns the a set of MRs with the given parent ID.
        return if (parent != -1) {
            MainActivity.testDB.maintenanceRecordDAO().findByParentID(parent).toSet()
        } else {
            MainActivity.testDB.maintenanceRecordDAO().findByParentIDTop().toSet()
        }
    }

    fun getMR(mrID: Int): MaintenanceRecordSQL {
        return MainActivity.testDB.maintenanceRecordDAO().findById(mrID)
    }

    fun addMaintenanceRecord(qrid: Int, deviceName: String, workOrderNum: String, serviceProvider: String?, serviceEngineeringCode: String?, faultCode: String?, ipmProcedure: String?, status: Int, timeStamp: Int, parent: Int): Long {
        // This function creates an MR SQL object from the inputs and pushes it into the SQL DB
        var mrObject = MaintenanceRecordSQL(
            id = null,
            deviceName = deviceName,
            workOrderNum = workOrderNum,
            serviceProvider = serviceProvider,
            serviceEngineeringCode = serviceEngineeringCode,
            faultCode = faultCode,
            ipmProcedure = ipmProcedure,
            status = status,
            timestamp = timeStamp,
            parent = parent
        )
        Log.v("Added MR Object", "DBModel")
        var pID = MainActivity.testDB.maintenanceRecordDAO().insert(mrObject)
        return pID

    }

    fun getHelper(){
        //isOnline
    }

    fun insertHelper(){
        //sync
    }

    fun getCatsfromDB(): Set<String> {
        var locations =  MainActivity.testDB.levelsDAO().getAll()
        var result = arrayListOf<String>()
        for ( i in locations) {
            result.add(i.levelName)
        }
        return result.toSet()
    }

    fun fragment_set_db(p: DevicePath, MR: MaintenanceRecord) {
        var json = Gson().toJson(MR)
        // TODO: Unhardcode the auth string
        json = "{\"Key\": \"SAHTesting449496\", \"Data\": $json}";

        MR.timestamp = (System.currentTimeMillis() / 1000)

        database[p.category]?.put(p.device, MR)
        //Log.d("db test?", database[category]?.get(device).toString())

        val fullDBJson = Gson().toJson(database)
        println("SAVING DB FILE")
        saveToLocalFile(fullDBJson, dbFilename)

        val url = "$serverURL/DB/${p.category}/${p.device}/"
//        post_server(url, json)
        logging(url, json)
    }

    /*
    fun fragment_delete(category: String = "", device: String = "") {

        database[category]?.remove(device)

        delete_server("$serverURL/DB/$category/$device")

        val fullDBJson = Gson().toJson(database)
        Log.d("SAVING FILE", "fragment_delete")
        saveToLocalFile(fullDBJson, dbFilename)
    }
     */


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
        //val url2 = url.replace(" ", "_")
        Log.i("postServer0:",url)
        val mURL = URL(url)
        Log.i("postServer0.5:",mURL.toString())
        //val reqParam = URLEncoder.encode(json, "ascii")
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
        //val url2 = url.replace(" ", "_")
        Log.i("Delete Server0:",url)
        val mURL = URL(url)
        Log.i("Delete Server0.5:",mURL.toString())
        //val reqParam = URLEncoder.encode(json, "ascii")
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
        //val url2 = url.replace(" ", "_")
        Log.i("Get Server0:",url)
        val mURL = URL(url)
        Log.i("Get Server0.5:",mURL.toString())
        //val reqParam = URLEncoder.encode(json, "ascii")
        val out = StringBuffer()
        var code:Int

        var urlc:HttpURLConnection = mURL.openConnection() as HttpURLConnection
        try {
            urlc.connectTimeout = (10*1000)
            urlc.requestMethod = "GET"
            urlc.connect()
            Log.i("Get Server1.5:",urlc.responseCode.toString())
            if (urlc.responseCode == 404) {
                return "";
            }

        } catch (e: IOException) {
            Log.i("Get Server1.5:","404")
            Log.i("Get Server1.6:", e.toString())
            return ""
        }
        with(urlc) {

            println("URL : $url")
            println("Response Code : $responseCode")
            code = responseCode
            if (code == 404) {
                return ""
            }

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
        Log.i("url", url)
        val URLm = URL(url)

        Log.i("URLm", URLm.toString())
        val text1 = URLm.readText()
        Log.i("text1", text1)
        //var text = StringBuffer()
        //with(URLm.openConnection() as HttpURLConnection) {
        //    requestMethod = "GET"  // optional default is GET
//
        //    Log.i("test1","\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
        //    inputStream.bufferedReader().use {
        //        val response = StringBuffer()
//
        //        var inputLine = it.readLine()
        //        while (inputLine != null) {
        //            println(inputLine)
        //            response.append(inputLine)
        //            inputLine = it.readLine()
        //        }
        //        text = response
        //    }
        //}
        Log.i("response:", text1)
        return text1
    }

    fun isOnline(): Boolean {
        val net = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.cm.activeNetwork
        } else {
            this.cm.getActiveNetworkInfo()
        }
        return net != null
        //iterate through logs and clear logs, update local
        //maybe sync?
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

    /**
     * Update templates with backend templates
     */
    fun updateTemplates() {
        if (isOnline()) {
            Thread {
                val newDB = get_server("$serverURL/templates/")
                if (newDB.isNotEmpty()) {
                    val hashMapType: Type =
                        object :
                            TypeToken<HashMap<String, DeviceTemplate>?>() {}.type
                    val readDB: HashMap<String, DeviceTemplate> =
                        Gson().fromJson(newDB, hashMapType)
                    this.templates = readDB

                    val templateJSON = Gson().toJson(templates)
                    Log.d("SAVING Web to FILE", "updatedTemplates")
                    saveToLocalFile(templateJSON, templatesFilename)
                }
            }.start()
        }
    }

    fun getServerConnection() {
        println("SAH:TESTING:getServerConnection Start")
        Thread {
            while (isOnline()) {
                println("SAH:TESTING:getServerConnection 1")
                val serverURLLocal = "spencers_2nd_pc.dyndns.rice.edu"
                val serverPort = 1433
                val Classes = "net.sourceforge.jtds.jdbc.Driver"
                val database = "master"
                val username = "Testing"
                val password = "~n2+LK(QmDv=Wv,X"
                val url = "jdbc:jtds:sqlserver://$serverURLLocal:$serverPort/$database"
                println("SAH:TESTING:getServerConnection 2")
                val address: InetAddress = InetAddress.getByName(serverURLLocal)
                println("SAH:TESTING:getServerConnection 2.1")
                val reached = address.isReachable(1000)
                val bytes = address.getHostAddress()
                println("SAH:TESTING:getServerConnection 2.5:$reached:$bytes")
                try {
                    Class.forName(Classes)
                    println("SAH:TESTING:getServerConnection 3")
                    connection = DriverManager.getConnection(url, username, password)
                    println("SAH:TESTING:getServerConnection 4")
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                println("SAH:TESTING:getServerConnection 5")
            }
        }
        println("SAH:TESTING:getServerConnection")
    }
}

