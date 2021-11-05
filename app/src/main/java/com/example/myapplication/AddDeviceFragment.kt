package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        //========================BINDINGS====================================
        val catPassed = args.categoryPassed
        val saveButton = view.findViewById<Button>(R.id.adsaveButton)
        saveButton.setOnClickListener() {
            val deviceNameText = view.findViewById<EditText>(R.id.adDeviceName).text as String
            if(!deviceNameText.equals("")){
                //TODO call DBCTRL helper function
                navMod.ADtoL3(catPassed, deviceNameText, findNavController())
            }
            else{
                //TODO make a toast and reject going forward
            }
        }

    }

}