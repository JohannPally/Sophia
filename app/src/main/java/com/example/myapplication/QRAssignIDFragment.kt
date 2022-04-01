package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner
import com.example.myapplication.databinding.FragmentQrBinding

class QRAssignIDFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private var ctrl: DBController ? = null

    private lateinit var codeScanner : CodeScanner
    private lateinit var scannerView: CodeScannerView

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()
    val args: QRAssignIDFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
        Log.v("qrassign","am i here")
    }

    //val args: QRFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity as? MainActivity
        if (activity != null) ctrl = activity.dbctrl;
        else {
            println("Failed to cast activity as MainActivity in QR Assign ID Fragment")
        }

        scannerView = view.findViewById(R.id.search_scanner_view)

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(context as Context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(android.Manifest.permission.CAMERA), 123)
        } else {
            startScanning()
        }
    }

     fun startScanning(): String {
        // Parameters (default values)
        codeScanner = CodeScanner(context as Context, scannerView) // TODO sus
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
         var qrData = "NULL"

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                qrData = it.text
                //assignQRData(it.text)
//                Toast.makeText(context as Context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(context as Context, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show() // TODO sus context as context
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

         return qrData
    }

    /**
     * This function reads in the data received from a QR input scan,
     * parses it for the device's ID and then returns that back to the object
     */
    fun assignQRData(text: String) {
        Log.d("QR Data Received", text)
        val deviceUUID : String = text
        Log.d("device UUID = " , deviceUUID)
        // TODO send UUID and required info back to add info page
        Log.d("Device Acquired" , deviceUUID)
        navMod.QRAssigntoAD(cat = args.categoryPassed, dev = args.devText, inv = deviceUUID,
            work = args.workText, servProv = args.servProvText, servEng = args.servEngText,
            fault = args.faultText, imp = args.ipmText, navController = findNavController())
    }

    /**
     * This function generates a QR code bitmap given the Location and Device information
     * passed in as a string.
     */


    override fun onResume() {
        super.onResume()
        if(::codeScanner.isInitialized) {
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        if(::codeScanner.isInitialized) {
            codeScanner?.releaseResources()
        }
        super.onPause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}