package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentWelcomeScreenBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AddDeviceFragment : Fragment() {

    private var _binding: FragmentWelcomeScreenBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var dbCtrl: DBController ? = null
    private val navMod: NavMod by activityViewModels()
    private val args: AddDeviceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity;
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        else {
            Log.d("can't cast activity", "AddDeviceFragment.kt")
        }

        val catPassed = args.categoryPassed
        val invPassed = args.invText
        val devPassed = args.devText
        val workPassed = args.workText
        val servProvPassed = args.servProvText
        val servEngPassed = args.servEngText
        val faultPassed = args.faultText
        val impPassed = args.impText

        val invNumTV = view.findViewById<TextView>(R.id.adinvNumTextView)
        val devNameET = view.findViewById<EditText>(R.id.adDeviceName)
        val servProvET = view.findViewById<EditText>(R.id.adServiceProvider)
        val servEngET = view.findViewById<EditText>(R.id.adServiceEngineerCode)
        val faultET = view.findViewById<EditText>(R.id.adFaultCode)

        invNumTV.text = invPassed
        devNameET.setText(devPassed)
        servProvET.setText(servProvPassed)
        servEngET.setText(servEngPassed)
        faultET.setText(faultPassed)


        //========================BINDINGS====================================
        val toQRAssignButton = view.findViewById<FloatingActionButton>(R.id.adToQRAssignButton)
        toQRAssignButton.setOnClickListener() {
            val devNameText = devNameET.text.toString()

            val workText = "testwork"
            val servProvText = servProvET.text.toString()
            val servEngText = servEngET.text.toString()
            val faultText = faultET.text.toString()

            val ipmText = "testIPM"
            Log.v("QRAssignButton test?","is this working")
            navMod.ADtoQRAssign(cat = catPassed, dev = devNameText, work = workText, servProv = servProvText,
            servEng = servEngText, fault = faultText, ipm = ipmText, navController = findNavController())
        }

        val saveButton = view.findViewById<Button>(R.id.adsaveButton)
        saveButton.setOnClickListener() {
            val bdevName = devNameET.text.toString()
            val binvNumText = invNumTV.text.toString()

            if(bdevName != ""){
                val bdevNameText = devNameET.text.toString()
                val bworkText = "testwork"
                val bservProvText = servProvET.text.toString()
                val bservEngText = servEngET.text.toString()
                val bfaultText = faultET.text.toString()
                val bipmText = "testIMP"

                // This is required because the Kotlin getDate requires API > 26 and we need to
                // support API 21 +
                // Getting today's date
                val date = Calendar.getInstance().time
                val formatter = SimpleDateFormat.getDateInstance()
                val formatedDate = formatter.format(date)

                val mr = MaintenanceRecord(id = binvNumText, workOrderNum = bworkText, serviceProvider = bservProvText, serviceEngineeringCode = bservEngText,
                faultCode = bfaultText, ipmProcedure = bipmText, status = "0", timestamp = System.currentTimeMillis()/1000)
//                    weeklyMaintenance = formatedDate, monthlyMaintenance = formatedDate, yearlyMaintenance = formatedDate)
                dbCtrl?.addNewDevice(p = DevicePath(catPassed, bdevName), mr)
                navMod.ADtoL3(catPassed, bdevName, findNavController())
            }
            else{
                //TODO make a toast and reject going forward make sure UUID is not empty
            }
        }

    }

}