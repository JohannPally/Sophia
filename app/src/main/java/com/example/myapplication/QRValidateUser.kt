package com.example.myapplication;

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
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner
import com.example.myapplication.databinding.FragmentQRValidateUserBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class QRValidateUser : Fragment() {
    private var _binding: FragmentQRValidateUserBinding? = null
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

        _binding = FragmentQRValidateUserBinding.inflate(inflater, container, false)
        return binding.root

    }

    //val args: QRFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity as? MainActivity
        if (activity != null) ctrl = activity.dbctrl;
        else {
            println("Failed to cast activity as MainActivity in QR Search Fragment")
        }

        scannerView = view.findViewById(R.id.validate_scanner_view)

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
        codeScanner = CodeScanner(context as Context, scannerView)
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
                    Toast.LENGTH_LONG).show()
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
    private fun saveQRData(text: String) {
        val hashType: Type = object : TypeToken<HashMap<String, String>>() {}.type
        val url_authkey_map: HashMap<String, String> = Gson().fromJson(text, hashType)
        val url: String? = url_authkey_map["URL"]
        val authkey: String? = url_authkey_map["AuthKey"]

        if (url != null) {
            Log.v("URL Key Test", url)
        }
        if (authkey != null) {
            Log.v("URL Test", authkey)
        }

        //TODO shared preferences
            //ligma balls
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

        // Update the sharedPrefs so tha thte user is authenticated
        if (authkey != null) {
            if (url != null) {
                editSharedPreferences(authkey, url)
            }
        }


        val defaultValue = resources.getString(R.string.default_keys)
        val testAK = sharedPref?.getString(R.string.authkey_key.toString(), "h")
        val testUK = sharedPref?.getString(R.string.url_key.toString(), "i")
        Log.v(testAK, "Auth key testing")
        Log.v(testUK, "URL key testing")

        navMod.QRValidatetoL1(findNavController());


    }

    /**
     * This function takes in an authKey and urlKey and modifies the sharedPreference to include it
     */
    private fun editSharedPreferences(authKey : String, urlKey : String) {
        Log.v("authkey input esp", authKey)
        Log.v("urlkey input esp", urlKey)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.authkey_key), authKey)
            putString(getString(R.string.url_key), urlKey)
            apply()
            val testAK = sharedPref?.getString(getString(R.string.authkey_key), "h")
            val testUK = sharedPref?.getString(getString(R.string.url_key), "i")
            Log.v(testAK, "A0 at end of ESP apply")
            Log.v(testUK, "URL0 at end of ESP apply")
        }
        val testAK = sharedPref?.getString(getString(R.string.authkey_key), "h")
        val testUK = sharedPref?.getString(getString(R.string.url_key), "i")
        Log.v(testAK, "A0 at end of ESP")
        Log.v(testUK, "URL0 at end of ESP")

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
