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

    fun WtoQRValidate(navController: NavController) {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToQRValidateUser()
        navController.navigate(action)
    }

    fun WtoRecurse(navController: NavController) {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRecurseFragment(-1)
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L1 fragment to L2 fragment
     * Inputs: param - The data to pass onto L2 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

    fun QRValidatetoW(navController: NavController) {
        val action = QRValidateUserDirections.actionQRValidateUserToWelcomeFragment()
        navController.navigate(action)
    }

    fun QRValidatetoL1(navController: NavController) {
        val action = QRValidateUserDirections.actionQRValidateUserToL1Fragment()
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

    fun L1toL3(param: String, device: String, navController: NavController) {
        println("NavMod: " + param + " / " + device)
        val action = L1FragmentDirections.actionL1FragmentToL3Fragment(param, device)
        navController.navigate(action)
    }

    fun L1toQRSearch(navController: NavController) {
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

    fun L2toQRSearch(navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToQRFragment()
        navController.navigate(action)
    }

    fun L2toAddDev(category: String, navController: NavController) {
        val action =
            L2FragmentDirections.actionL2FragmentToAddDeviceFragment(categoryPassed = category)
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

//    @Composable
//    fun ADtoL3(category: String, device: String, navController: NavController) {
//        navController.navigate("com.example.myapplication.L3Fragment") {
//            popUpTo("com.example.myapplication.L2Fragment")
//        }
//    }

    fun ADtoL3(category: String, device: String, navController: NavController) {
        val action = L2FragmentDirections.actionL2FragmentToL3Fragment(category, device)
        navController.popBackStack(R.id.L2Fragment, false)
        navController.navigate(action)
    }

    fun ADtoQRAssign(
        cat: String,
        dev: String,
        work: String,
        servProv: String,
        servEng: String,
        fault: String,
        ipm: String,
        navController: NavController
    ) {
        val action = AddDeviceFragmentDirections.actionAddDeviceFragmentToQRAssignID(
            categoryPassed = cat,
            devText = dev,
            workText = work,
            servProvText = servProv,
            servEngText = servEng,
            faultText = fault,
            ipmText = ipm
        )
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

    fun QRSearchtoL3(category: String, device: String, navController: NavController) {
        val action = QRSearchFragmentDirections.actionQRFragmentToL3Fragment(category, device)
        navController.navigate(action)
    }

    fun QRSearchtoL1(navController: NavController) {
        val action = QRSearchFragmentDirections.actionQRSearchFragmentToL1Fragment()
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from L2 fragment to L3 fragment
     * Inputs: param - The data to pass onto L3 fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

    //TODO make sure this is right
    fun QRAssigntoL3(category: String, device: String, navController: NavController) {
        val action = QRAssignIDFragmentDirections.actionQRAssignIDToAddDeviceFragment()
        navController.navigate(action)
    }

    fun QRAssigntoAD(
        cat: String,
        dev: String,
        inv: String,
        work: String,
        servProv: String,
        servEng: String,
        fault: String,
        imp: String,
        navController: NavController
    ) {
        val action = QRAssignIDFragmentDirections.actionQRAssignIDToAddDeviceFragment(
            categoryPassed = cat,
            devText = dev,
            invText = inv,
            workText = work,
            servProvText = servProv,
            servEngText = servEng,
            faultText = fault,
            impText = imp
        )
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from Recurse fragment to others
     * Inputs: param - The data to pass onto Recurse fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */
    fun RecursetoRecurse(navController: NavController, parent:Int?) {
        val action = RecurseFragmentDirections.actionRecurseFragmentSelf(parent?:-1)
        navController.navigate(action)
    }

    fun RecursetoInfo(navController: NavController, id: Int) {
        val action = RecurseFragmentDirections.actionRecurseFragmentToInfoFragment(id)
        navController.navigate(action)
    }

    fun RecurseAddtoInfo(navController: NavController) {
        val action = InfoFragmentDirections.actionInfoFragmentToRecurseFragment()
        navController.navigate(action);
    }

    fun RecursetoRecurseAdd(navController: NavController, parent: Int) {
        val action = RecurseFragmentDirections.actionRecurseFragmentToRecurseAddFragment(parent = parent)
        navController.navigate(action);
    }

    fun RecurseToSearch(navController: NavController) {
        val action = RecurseFragmentDirections.actionRecurseFragmentToSearchFragment()
        navController.navigate(action);

    }


//    fun RecursetoQR(navController: NavController, parent: Int) {
//        val action = RecurseFragmentDirections.actio
//        navController.navigate(action);
//    }

//    fun RecurseAddtoQRScan(navController: NavController) {
//        val action = RecurseAddFragmentDirections.actionRecurseAddFragmentToQRSearchFragment()
//        navController.navigate(action)
//    }


    /**
     * Function that handles the navigation from Add fragment to others
     * Inputs: param - The data to pass onto Recurse fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

    fun RecurseAddtoInfo(navController: NavController, id: Int) {
        val action = RecurseAddFragmentDirections.actionRecurseAddFragmentToInfoFragment(id = id)
        navController.navigate(action)
    }

    fun RecurseAddtoRecurseAddQRScan(navController: NavController, p:Int, rN:String, rI:String) {
        val action = RecurseAddFragmentDirections.actionRecurseAddFragmentToRAddQRScan(parent = p, passedName = rN, passedID = rI)
        navController.navigate(action)
    }

    /**
     * Function that handles the navigation from Add fragment to others
     * Inputs: param - The data to pass onto Recurse fragment
     *         navController - The reference to the navigation controller present in the fragment
     * Outputs: None
     */

    fun RecurseAddQRScantoRecurseAdd(navController: NavController, p:Int, rN:String, rI:String) {
        val action = RAddQRScanDirections.actionRAddQRScanToRecurseAddFragment(parent = p, rAddName = rN, rAddId = rI);
        navController.navigate(action)
    }
}