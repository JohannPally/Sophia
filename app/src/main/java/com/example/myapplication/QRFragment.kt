package com.example.myapplication;

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
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
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner
import com.example.myapplication.databinding.FragmentQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private var ctrl: DBController ? = null

    private lateinit var codeScanner : CodeScanner
    private lateinit var scannerView: CodeScannerView

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root

    }

    //val args: QRFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity as? MainActivity
        if (activity != null) ctrl = activity.dbctrl;
        else {
            println("Failed to cast activity as MainActivity in QR Fragment")
        }

        scannerView = view.findViewById(R.id.scanner_view)

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(context as Context , android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(android.Manifest.permission.CAMERA), 123)
        } else {
            startScanning()
        }


        //========================BINDINGS====================================



    }

    private fun startScanning() {
        // Parameters (default values)
        codeScanner = CodeScanner(context as Context, scannerView) // TODO sus
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                saveQRData(it.text)
                Toast.makeText(context as Context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(context as Context , "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show() // TODO sus context as context
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    /**
     * This function reads in the data received from a QR input scan,
     * parses it for the location and device name and then navigates to that device's
     * particular maintenance record
     */
    fun saveQRData(text: String) {
        Log.d("QR Data Received", text)
        val paramList = text.split(":")
        val catQR : String = paramList[0]
        val devQR : String = paramList[1]
        Log.d("Location = " , catQR)
        Log.d("Device = ", devQR )
        val deviceMR : MaintenanceRecord? = ctrl?.getInf(Pair(catQR, devQR))
        Log.d("Device Acquired" , deviceMR.toString())
        val action = QRFragmentDirections.actionQRFragmentToL3Fragment(catQR, devQR)
        findNavController().navigate(action)

    }

    /**
     * This function generates a QR code bitmap given the Location and Device information
     * passed in as a string.
     */
    fun getQrCodeBitmap(Location: String, Device: String): Bitmap {
        val qrCodeContent = "$Location : $Device"
        val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 } // Make the QR code buffer border narrower
        val size = 512 //pixels
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }

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
