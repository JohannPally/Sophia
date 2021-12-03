package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    //TODO: should we be passing this to the RV adapters or can we just instantiate this there
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
            // TODO do we still need a password?
            if(loginText == "6969"){
                // TODO remove this and edit shared preferences correctly
                Log.v("Entered 69 case", "2")
                defaultSharedPreference(R.string.default_keys.toString(), R.string.default_keys.toString())
            }
            else{
                Toast.makeText(context as Context, "Cleared UP: $loginText", Toast.LENGTH_LONG).show()
            }



            // TODO check if user is authenticated
            val sharedPref = activity?.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            val defaultValue = resources.getString(R.string.default_keys)
            val testAK = sharedPref?.getString(R.string.authkey_key.toString(), defaultValue)
            val testUK = sharedPref?.getString(R.string.url_key.toString(), defaultValue)
            Log.v(testAK, "Auth key debug")
            Log.v(testUK, "URL key debug")

            Log.v("Defailt key debug", defaultValue)
            if (testAK.equals(resources.getString(R.string.default_keys)) and testUK.equals(resources.getString(R.string.default_keys))) {
                navMod.WtoQRValidate(findNavController())
            }

            else {
                Log.v("Authenticate Successful", "Success")
                navMod.WtoL1(findNavController())
            }

        }

        val syncButton = view.findViewById<FloatingActionButton>(R.id.syncButton)
        syncButton.setOnClickListener() {
            dbCtrl?.sync_updateDB()
        }

    }

    /**
     * This function takes in an authKey and urlKey and modifies the sharedPreference to include it
     */
    private fun defaultSharedPreference(authKey : String, urlKey : String) {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.authkey_key), authKey)
            putString(getString(R.string.url_key), urlKey)
            apply()
        }
        Log.v("Set UP back to default","1")
        val defaultValue = resources.getString(R.string.default_keys)
        val testAK = sharedPref?.getString(R.string.authkey_key.toString(), defaultValue)
        val testUK = sharedPref?.getString(R.string.url_key.toString(), defaultValue)
        Log.v(testAK, "Auth key def")
        Log.v(testUK, "URL key def")

    }

}