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

class AddDeviceFragment : Fragment() {
    private var _binding: FragmentWelcomeScreenBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var dbCtrl: DBController ? = null
    private val navMod: NavMod by activityViewModels()
    val args: AddDeviceFragmentArgs by navArgs()

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
        //TODO fill text, add button to jump between

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
        //TODO work
        val servProvET = view.findViewById<EditText>(R.id.adServiceProvider)
        val servEngET = view.findViewById<EditText>(R.id.adServiceEngineerCode)
        val faultET = view.findViewById<EditText>(R.id.adFaultCode)
        //TODO IMP

        invNumTV.setText(invPassed)
        devNameET.setText(devPassed)
        //TODO work
        servProvET.setText(servProvPassed)
        servEngET.setText(servEngPassed)
        faultET.setText(faultPassed)
        //TODO IMP


        //========================BINDINGS====================================
        val toQRAssignButton = view.findViewById<FloatingActionButton>(R.id.adToQRAssignButton)
        toQRAssignButton.setOnClickListener() {
            val devNameText = devNameET.text.toString()
            //TODO work
            val workText = "testwork"
            val servProvText = servProvET.text.toString()
            val servEngText = servEngET.text.toString()
            val faultText = faultET.text.toString()
            //TODO IMP
            val impText = "testIMP"
            Log.v("hmm","is this working")
            navMod.ADtoQRAssign(cat = catPassed, dev = devNameText, work = workText, servProv = servProvText,
            servEng = servEngText, fault = faultText, imp = impText, navController = findNavController())
        }

        val saveButton = view.findViewById<Button>(R.id.adsaveButton)
        saveButton.setOnClickListener() {
            val devName = devNameET.text.toString()
            //TODO check UUID too
            if(!devName.equals("")){
                //TODO finish out the controller call
                //dbCtrl?.addNewDevice(Pair(catPassed, deviceNameText), )
                navMod.ADtoL3(catPassed, devName, findNavController())
            }
            else{
                //TODO make a toast and reject going forward make sure UUID is not empty
            }
        }

    }

}