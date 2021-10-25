package com.example.myapplication

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentL3Binding
import androidx.navigation.fragment.navArgs
import android.text.Editable





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

    }

    fun initTextFields(view: View){
        val devArg = args.devicePassed
        val catArg = args.categoryPassed

//      TODO: we need to save the json object
        this.currentMaintenanceRecord = ctrl?.getInf(Pair(catArg, devArg)) as MaintenanceRecord
        Log.d("check object", this.currentMaintenanceRecord.toString())

        view.findViewById<TextView>(R.id.l3deviceText).text = devArg

        //TODO: have to fill out these functions in the controller for the gets
        val inventoryNumTextView = view.findViewById<TextView>(R.id.l3inventoryNumberText)
        val workOrderNumTextView = view.findViewById<TextView>(R.id.l3workOrderNumberText)
        val serviceProviderTextView = view.findViewById<TextView>(R.id.l3serviceProviderText)
        val serviceEngineerCodeTextView = view.findViewById<TextView>(R.id.l3serviceEngineerCodeText)
        val faultCodeTextView = view.findViewById<TextView>(R.id.l3faultCodeText)
        val ipmProcedureTextView = view.findViewById<TextView>(R.id.l3ipmProcedureText)
        var localManRec = this.currentMaintenanceRecord;
        if (localManRec != null) {
            inventoryNumTextView.setText(localManRec.inventoryNum)
            workOrderNumTextView.setText(localManRec.workOrderNum)
            serviceProviderTextView.setText(localManRec.serviceProvider)
            serviceEngineerCodeTextView.setText(localManRec.serviceEngineeringCode)
            faultCodeTextView.setText(localManRec.faultCode)
            ipmProcedureTextView.setText(localManRec.ipmProcedure)
        }

        inventoryNumTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.inventoryNum != inventoryNumTextView.text.toString()) {
                        localManRec.inventoryNum = inventoryNumTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
                    }
                };
            }

        workOrderNumTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.workOrderNum != workOrderNumTextView.text.toString()) {
                        localManRec.workOrderNum = workOrderNumTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
                    }
                };
            }

        serviceProviderTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.serviceProvider != serviceProviderTextView.text.toString()) {
                        localManRec.serviceProvider = serviceProviderTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
                    }
                };
            }

        serviceEngineerCodeTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.serviceEngineeringCode != serviceEngineerCodeTextView.text.toString()) {
                        localManRec.serviceEngineeringCode = serviceEngineerCodeTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
                    }
                };
            }

        faultCodeTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.faultCode != faultCodeTextView.text.toString()) {
                        localManRec.faultCode = faultCodeTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
                    }
                };
            }

        ipmProcedureTextView.onFocusChangeListener =
            View.OnFocusChangeListener { p0, hasFocus ->
                if (localManRec != null && !hasFocus) {
                    if (localManRec.ipmProcedure != ipmProcedureTextView.text.toString()) {
                        localManRec.ipmProcedure = ipmProcedureTextView.text.toString()
                        ctrl?.editInfo(Pair(catArg, devArg), localManRec);
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
        if (wonTV.text.toString().equals("")) {
            //TODO: fill out helper function in controller
//            wonTV.setText(ctrl.makeWON());
        }
    }

    fun showStatus(view: View, mr: MaintenanceRecord){
        val stBut = view.findViewById<TextView>(R.id.l3statusButton)
        val stat = mr.status.toInt()
        stBut.isEnabled = false
        when(stat){
            0 -> {
                stBut.setText("Active")
                stBut.setBackgroundColor(resources.getColor(R.color.active_green))
            }
            1 -> {
                stBut.setText("Caution")
                stBut.setBackgroundColor(resources.getColor(R.color.caution_yellow))
            }
            2 -> {
                stBut.setText("Hazard")
                stBut.setBackgroundColor(resources.getColor(R.color.hazard_red))
            }
            else -> {
                stBut.setText("OOP")
                stBut.setBackgroundColor(resources.getColor(R.color.purple_200))
            }
        }
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}