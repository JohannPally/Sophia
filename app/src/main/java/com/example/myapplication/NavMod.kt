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
    fun WtoL1(navController: NavController) {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToL1Fragment()
        navController.navigate(action)
    }

    fun WtoQR(navController: NavController) {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToQRFragment()
        navController.navigate(action)
    }

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

    fun L1toQR(navController: NavController) {
        val action = L1FragmentDirections.actionL1FragmentToQRFragment()
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */
    fun L2toL3(category: String, device: String, navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToL3Fragment(category, device)
        navController.navigate(action)
    }

    fun L2toQR(navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToQRFragment()
        navController.navigate(action)
    }

    fun L2toAddDev(navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToAddDeviceFragment()
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */
    fun ADtoL3(category: String, device: String, navController: NavController) {
        val action = AddDeviceFragmentDirections.actionAddDeviceFragmentToL3Fragment(category, device)
        navController.navigate(action)
    }

}