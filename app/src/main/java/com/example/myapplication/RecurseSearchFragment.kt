package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.HashSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dbCtrl: DBController ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun searchDevice(name: String): Set<MaintenanceRecordSQL> {
        var mrDevice = MainActivity.testDB.maintenanceRecordDAO().findByPartialName(name = name)
//        var levelsDevice = MainActivity.testDB.levelsDAO().findByPartialId(id = name)
        Log.v("Device&Level Query is ", name)

        return mrDevice.toSet()
    }

//    fun displayDevice(mrDevice: MaintenanceRecordSQL) {
//        Log.v("Device Name =", mrDevice.deviceName )
//        mrDevice.serviceProvider?.let { Log.v("Serv Provider =", it) }
//        Log.v("Work Order Num =", mrDevice.workOrderNum )
//        Log.v("Status =", mrDevice.status.toString() )
//    }

    private val navMod: NavMod by activityViewModels()
    val args: RecurseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        val search_rv: RecyclerView = view.findViewById<RecyclerView>(R.id.search_rv)
        val both = dbCtrl?.get_all(args.parent)
        val levels = both?.first
        val mrs = both?.second

        val searchTV: EditText = view.findViewById<EditText>(R.id.searchTV)
        val queryDeviceBtn: MaterialButton = view.findViewById<MaterialButton>(R.id.queryButton)
        queryDeviceBtn.setOnClickListener(){

            val deviceName = searchTV.text.toString()
            var searchedMRs = searchDevice(deviceName)

            if(levels!=null && mrs!=null){
                var adapter = Recurse_Item_Adapter(levels, searchedMRs, navMod, findNavController(), 1)
                search_rv.adapter = adapter
                search_rv.layoutManager = LinearLayoutManager(activity)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}