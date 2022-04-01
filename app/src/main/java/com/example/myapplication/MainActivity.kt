package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    public var dbctrl: DBController? = null

    // Wrapping this in companion object so its accessible across all files
    companion object {
        lateinit var testDB : AppDatabase
    }

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testDB = AppDatabase.getInstance(applicationContext)
        dbctrl = DBController(applicationContext);
        //Log.v("???", dbctrl.toString())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // @ Johann TODO This is where we build our mock DB
        //testDB.clearAllTables() // -- Run this to delete all tables and clear the data
        // if you keep re- running the app, it will keep creating duplicates of this dummy data
        val mrDao = testDB.maintenanceRecordDAO()
        val levelsDao = testDB.levelsDAO()
        val NICULevel = LevelSQL(null, "NICU", null);
        val ERLevel = LevelSQL(null, "ER", null);
        val InfantWard = LevelSQL(null, "Infant Ward", null)
        levelsDao.insert(NICULevel)
        levelsDao.insert(ERLevel)
        levelsDao.insert(InfantWard)
//        mrDao.insert(MaintenanceRecordSQL(null,"Oxygen Conc", "1", "TestP", "TestE", "TestF", "TestI", 1, 12, levelsDao.getAll()[0].id))
//        mrDao.insert(MaintenanceRecordSQL(null,"Breath Pump", "2", "TestP2", "TestE2", "TestF2", "TestI2", 1, 123, levelsDao.getAll()[1].id))
//        mrDao.insert(MaintenanceRecordSQL(null, "SP02 Sensor", "3", "TestP3", "TestE3", "TestF3", "TestI3", 1, 1234, levelsDao.getAll()[2].id))


        //TODO can make this a global variable instead of passing into frags
        //but have to make a temp var and then assign the public var = temp var
        //otherwise complains that 'it isn't mutable at this time
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        var db = DatabaseModel(applicationContext)
//
//        val getResult = db.get("category1", "");
//        println("Result: $getResult");

//        device.count--
//        device.saveToLocalFile(applicationContext)
//        var device = createFromFile(applicationContext, "Medical", "Syringe")
//        println("Device Count: " + (device?.count ?: "N/A"));

        //val qrBut = findViewById<View>(R.id.qrButton)
        //qrBut.setOnClickListener(navController.navigate())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}