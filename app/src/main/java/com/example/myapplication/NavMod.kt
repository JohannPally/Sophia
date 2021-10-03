package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class NavMod : ViewModel() {

    /**
     * Function that handles the navigation from L1 fragment to L2 fragment
     * Inputs: param - The data to pass onto L2 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */
    fun L1toL2(param: String, navController: NavController) {
        val action = L1FragmentDirections.actionL1FragmentToL2Fragment(param)
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */
    fun L2toL3(param: String, navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToL3Fragment(param)
        navController.navigate(action)
    }


}