package com.example.myapplication

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.placeholder.PlaceholderContent

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /**
     * Variable to set up testing envrionment:
     * AppContext
     * Mock Data = testContent
     * Mock DataBase model = testDBModel
     */
    var appContext = InstrumentationRegistry.getInstrumentation().targetContext
    var testContent = PlaceholderContent
    var testDBModel = DatabaseModel(appContext)
    var testDBController = DBController(appContext)

    @Test
    fun testReadAndPrintDB() {
        println("hello")
        var cats :  Set<String> =testDBController.getCats()
        println(cats)
        for (cat in cats){
            println(cat)
        }
        //println(testDBModel.database)
    }

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.myapplication", appContext.packageName)
//    }
//
//    @Test
//    fun adding1ItemToDB_isCorrect(){
//        // Setting the database in DB Model to point to our mock data
//        testDBModel.database = testContent.ITEM_MAP as HashMap<Any?, Any?>
//
//        var testItem100 = testContent.createPlaceholderItem(100)
//        testContent.addItem(testItem100)
//        assertTrue(testDBModel.database.contains(testItem100.id))
//    }
//
//    @Test
//    fun ensuringItemNotAddedToDBisNotinDB_isCorrect(){
//        // Setting the database in DB Model to point to our mock data
//        testDBModel.database = testContent.ITEM_MAP as HashMap<Any?, Any?>
//        var testItem200 = testContent.createPlaceholderItem(200)
//        assertFalse(testDBModel.database.contains(testItem200.id))
//    }
//
//    @Test
//    fun adding1ItemToDBAndGetting_isCorrect(){
//        // Setting the database in DB Model to point to our mock data
//        testDBModel.database = testContent.ITEM_MAP as HashMap<Any?, Any?>
//
//        var testItem300 = testContent.createPlaceholderItem(300)
//        testContent.addItem(testItem300)
//        assertEquals(listOf(testItem300),testDBModel.get(listOf(testItem300.id)))
//    }
//
//    @Test
//    fun adding2ItemsToDBAndGetting_isCorrect(){
//
//        // Setting the databse in DB Model to point to our mock data
//        testDBModel.database = testContent.ITEM_MAP as HashMap<Any?, Any?>
//
//        var testItem400 = testContent.createPlaceholderItem(400)
//        var testItem401 = testContent.createPlaceholderItem(401)
//        testContent.addItem(testItem400)
//        testContent.addItem(testItem401)
//        assertEquals(listOf(testItem400, testItem401),testDBModel.get(listOf(testItem400.id, testItem401.id) ))
//    }
}