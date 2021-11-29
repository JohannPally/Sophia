package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentWelcomeScreenBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WelcomeFragment: Fragment() {
    private var _binding: FragmentWelcomeScreenBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    //TODO hook up to controller
    private var dbCtrl: DBController ? = null

    // Reference to the front end model that handles navigation from screen to screen
    //TODO: should we be passing this to the RV adapters or can we just instiate this there
    //Does this refer to the one we made in the Main Activity?
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {

        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity;
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        //========================BINDINGS====================================

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener() {
            val loginText = view.findViewById<EditText>(R.id.loginEditPassword).text.toString()
            if(loginText.equals("1234")){
                navMod.WtoL1(findNavController())
            }
            else{
                Toast.makeText(context as Context, "Incorrect password: ${loginText}", Toast.LENGTH_LONG).show()
            }
        }

        val syncButton = view.findViewById<FloatingActionButton>(R.id.syncButton)
        syncButton.setOnClickListener() {
            dbCtrl?.sync_updateDB()
        }

    }
}