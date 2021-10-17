package com.example.myapplication

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


class L3Fragment : Fragment() {

    private var _binding: FragmentL3Binding? = null
    private val binding get() = _binding!!

    private var ctrl: DBController ? = null

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

        fillText(view);
        showStatus(view)

        //========================BINDINGS====================================

        val makeWCButton = view.findViewById<Button>(R.id.l3makeWorkCodeButton)
        makeWCButton.setOnClickListener() {
            generateWorkCode(view);
        }

    }

    fun fillText(view: View){
        val devArg = args.devicePassed
        view.findViewById<TextView>(R.id.l3deviceText).text = devArg

        //TODO: have to fill out these functions in the controller for the gets
//        view.findViewById<TextView>(R.id.inventoryNumberText).setText(ctrl.getInventoryNumber())
//        view.findViewById<TextView>(R.id.workOrderNumberText).setText(ctrl.getWorkOrderNumber())
//        view.findViewById<TextView>(R.id.serviceProviderText).setText(ctrl.getServiceProvider())
//        view.findViewById<TextView>(R.id.serviceEngineerCodeText).setText(ctrl.getServiceEngineerCode())
//        view.findViewById<TextView>(R.id.ipmProcedureText).setText(ctrl.getIPMProcedure())
    }

    //TODO: probably need a button or something to clear work orders
    fun generateWorkCode(view: View){
        val wonTV = view.findViewById<TextView>(R.id.l3workOrderNumberText)
        if (wonTV.text.toString().equals("")) {
            //TODO: fill out helper function in controller
//            wonTV.setText(ctrl.makeWON());
        }
    }

    fun showStatus(view: View){
        val stBut = view.findViewById<TextView>(R.id.l3statusButton)
        //TODO: fill the helper function to get the appropriate status color, might be an arg instead?
        //stBut.setBackgroundColor(ctrl.getStatusColor())
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}