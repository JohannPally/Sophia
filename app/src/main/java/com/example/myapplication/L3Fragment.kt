package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentL3Binding
import androidx.navigation.fragment.navArgs
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class L3Fragment : Fragment() {

    private var _binding: FragmentL3Binding? = null
    private val binding get() = _binding!!

    private var ctrl: DBController ? = null
    private var currentMaintenanceRecord: MaintenanceRecord ? = null

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentL3Binding.inflate(inflater, container, false)
        return binding.root

    }

    val args: L3FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity as? MainActivity
        if (activity != null) ctrl = activity.dbctrl;
        else {
            println("Failed to cast activity as MainActivity in L3 Fragment")
        }

        initTextFields(view)

        //========================BINDINGS====================================

        val makeWCButton = view.findViewById<Button>(R.id.l3makeWorkCodeButton)
        makeWCButton.setOnClickListener() {
            generateWorkCode(view);
        }

        val stBut = view.findViewById<TextView>(R.id.l3statusButton)
        stBut.setOnClickListener() {
            var stat = this.currentMaintenanceRecord?.status?.toInt()
            if (stat!=null)
                stat = (stat+1)%4
                this.currentMaintenanceRecord?.status = (stat).toString()
            val devArg = args.devicePassed
            val catArg = args.categoryPassed
            this.currentMaintenanceRecord?.let { it1 -> ctrl?.editInfo(DevicePath(catArg, devArg), it1) }
            when(stat){
                0 -> {
                    stBut.text = "Active"
                    stBut.setBackgroundColor(resources.getColor(R.color.active_green))
                }
                1 -> {
                    stBut.text = "Caution"
                    stBut.setBackgroundColor(resources.getColor(R.color.caution_yellow))
                }
                2 -> {
                    stBut.text = "Hazard"
                    stBut.setBackgroundColor(resources.getColor(R.color.hazard_red))
                }
                3 -> {
                    stBut.text = "Offline"
                    stBut.setBackgroundColor(resources.getColor(R.color.black))
                }
                else -> {
                    stBut.text = "Fatal Error"
                    stBut.setBackgroundColor(resources.getColor(R.color.purple_200))
                }
            }
        }
    }

    fun initTextFields(view: View){
        val devArg = args.devicePassed
        val catArg = args.categoryPassed

//      TODO: we need to save the json object
        this.currentMaintenanceRecord = ctrl?.getInf(DevicePath(catArg, devArg)) as MaintenanceRecord
        //Log.d("test getInf", this.currentMaintenanceRecord.toString())
        //Log.d("check object", this.currentMaintenanceRecord.toString())

        view.findViewById<TextView>(R.id.l3deviceText).text = devArg

        //TODO: have to fill out these functions in the controller for the gets
        val inventoryNumTextView = view.findViewById<TextView>(R.id.l3inventoryNumberText)
        val workOrderNumTextView = view.findViewById<TextView>(R.id.l3workOrderNumberText)
        val serviceProviderTextView = view.findViewById<TextView>(R.id.l3serviceProviderText)
        val serviceEngineerCodeTextView = view.findViewById<TextView>(R.id.l3serviceEngineerCodeText)
        val faultCodeTextView = view.findViewById<TextView>(R.id.l3faultCodeText)
        val ipmProcedureTextView = view.findViewById<TextView>(R.id.l3ipmProcedureText)
        val weeklyMaintenanceDate = view.findViewById<TextView>(R.id.l3weeklymaintenanceText)
        val monthlyMaintenanceDate = view.findViewById<TextView>(R.id.l3monthlymaintenanceText)
        val yearlyMaintenanceDate = view.findViewById<TextView>(R.id.l3yearlymaintenanceText)

        var localManRec = this.currentMaintenanceRecord;
        if (localManRec != null) {
            inventoryNumTextView.text = localManRec.id
            workOrderNumTextView.text = localManRec.workOrderNum
            serviceProviderTextView.text = localManRec.serviceProvider
            serviceEngineerCodeTextView.text = localManRec.serviceEngineeringCode
            faultCodeTextView.text = localManRec.faultCode
            ipmProcedureTextView.text = localManRec.ipmProcedure
            weeklyMaintenanceDate.text = localManRec.weeklyMaintenance
            monthlyMaintenanceDate.text = localManRec.monthlyMaintenance
            yearlyMaintenanceDate.text = localManRec.yearlyMaintenance
        }

        inventoryNumTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.id != inventoryNumTextView.text.toString()) {
                        localManRec.id = inventoryNumTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        workOrderNumTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.workOrderNum != workOrderNumTextView.text.toString()) {
                        localManRec.workOrderNum = workOrderNumTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        serviceProviderTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.serviceProvider != serviceProviderTextView.text.toString()) {
                        localManRec.serviceProvider = serviceProviderTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        serviceEngineerCodeTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.serviceEngineeringCode != serviceEngineerCodeTextView.text.toString()) {
                        localManRec.serviceEngineeringCode = serviceEngineerCodeTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        faultCodeTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.faultCode != faultCodeTextView.text.toString()) {
                        localManRec.faultCode = faultCodeTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        ipmProcedureTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.ipmProcedure != ipmProcedureTextView.text.toString()) {
                        localManRec.ipmProcedure = ipmProcedureTextView.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        weeklyMaintenanceDate.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.weeklyMaintenance != weeklyMaintenanceDate.text.toString()) {
                        localManRec.weeklyMaintenance = weeklyMaintenanceDate.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        monthlyMaintenanceDate.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.monthlyMaintenance != monthlyMaintenanceDate.text.toString()) {
                        localManRec.monthlyMaintenance = monthlyMaintenanceDate.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        yearlyMaintenanceDate.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.yearlyMaintenance != yearlyMaintenanceDate.text.toString()) {
                        localManRec.yearlyMaintenance = yearlyMaintenanceDate.text.toString()
                        ctrl?.editInfo(DevicePath(catArg, devArg), localManRec);
                    }
                };
            }

        if (localManRec != null) {
            showStatus(view, localManRec)
        }
    }

    //TODO: probably need a button or something to clear work orders
    fun generateWorkCode(view: View){
        val wonTV = view.findViewById<TextView>(R.id.l3workOrderNumberText)
        if (wonTV.text.toString() == "") {
            //TODO: fill out helper function in controller
//            wonTV.setText(ctrl.makeWON());
        }
    }

    // This function genetrates a bitmap based on the data provided
    fun getQrCodeBitmap(Location: String, Device: String): File? {
        val qrCodeContent = "$Location : $Device"
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1
        } // Make the QR code buffer border narrower
        val size = 512 //pixels
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
        val myQRCode = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }

        return bitmapToFile(myQRCode, "testQR")
    }

    // This function saves the bitmap to local file storage
    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {

            val file = File(context?.filesDir, fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    fun showStatus(view: View, mr: MaintenanceRecord){
        //Log.d("v", mr.toString());
        val stBut = view.findViewById<TextView>(R.id.l3statusButton)
        val stat = mr.status.toInt()
        when(stat){
            0 -> {
                stBut.text = "Active"
                stBut.setBackgroundColor(resources.getColor(R.color.active_green))
            }
            1 -> {
                stBut.text = "Caution"
                stBut.setBackgroundColor(resources.getColor(R.color.caution_yellow))
            }
            2 -> {
                stBut.text = "Hazard"
                stBut.setBackgroundColor(resources.getColor(R.color.hazard_red))
            }
            3 -> {
                stBut.text = "Offline"
                stBut.setBackgroundColor(resources.getColor(R.color.black))
            }
            else -> {
                stBut.text = "OOP"
                stBut.setBackgroundColor(resources.getColor(R.color.purple_200))
            }
        }
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}