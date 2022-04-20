package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    private val navMod: NavMod by activityViewModels()
    val args: InfoFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        //==================BINDINGS=====================
        val tasks_rv: RecyclerView = view.findViewById<RecyclerView>(R.id.tasks_recyclerView)
        val cycle_et: EditText = view.findViewById<EditText>(R.id.cycle_length_info)
        var mr = dbCtrl?.get_mr(args.id)

        //TODO update logic with new getter for tasks and checklist
//        var tasks = mr?.tasks
//        var startdate = mr?.date
//        var cyclelen = mr?.numdays
//
//        if(tasks != null){
//            if(startdate != null && cyclelen !=null){
//                var adapter = TaskItemAdapter(tasks = tasks, cycle = cyclelen, startdate = startdate, navMod = navMod, navCtrl = findNavController())
//                tasks_rv.adapter = adapter
//                tasks_rv.layoutManager = LinearLayoutManager(activity)
//
//            } else {
//                //TODO print error box, but should be unreachable
//            }
//        } else {
//            cyclelen = 7
//            cycle_et.setText("7");
//            tasks = arrayOf(Triple("Type task name here...", Calendar.getInstance(), 0))
//            startdate = Calendar.getInstance()
//            //TODO push change to the MR object
//            var adapter = TaskItemAdapter(tasks = tasks, cycle = cyclelen, startdate = startdate, navMod = navMod, navCtrl = findNavController())
//            tasks_rv.adapter = adapter
//            tasks_rv.layoutManager = LinearLayoutManager(activity)
//        }

        val add_button: Button = view.findViewById<Button>(R.id.addtaskbutton_info)
        add_button.setOnClickListener(){
            //TODO define what additem does (add empty task to the rv set and refresh and push to MR object in db
        }

        var devName = mr?.deviceName
        var qrID = mr?.id
        var fltCode = mr?.faultCode
        var ipmProc = mr?.ipmProcedure
        var servEngCode = mr?.serviceEngineeringCode
        var servProv = mr?.serviceProvider
        var workOrdNum = mr?.workOrderNum
        var status = mr?.status
        var timeStmp = mr?.timestamp



        val devName_ET = view.findViewById<EditText>(R.id.devname_et_info)
        val qrCode_TV = view.findViewById<TextView>(R.id.qrcode_tv_info)
        val engCode_ET = view.findViewById<EditText>(R.id.servengcode_et_info)
        var servProv_ET = view.findViewById<EditText>(R.id.servprov_et_info)
        var workOrdNum_ET = view.findViewById<EditText>(R.id.workorder_et_info)


        devName_ET.setText(devName)
        qrCode_TV.text = qrID.toString()
        engCode_ET.setText(servEngCode)
        servProv_ET.setText(servProv)
        workOrdNum_ET.setText(workOrdNum)
        if (status != null) {
            setButton(view = view, status = status)
        }

    }

    public fun setButton(view: View, status:Int){
        val status_button = view.findViewById<Button>(R.id.status_button_info)
        when(status){
            0 -> {
                status_button.setText("Active")
                status_button.setBackgroundResource(R.color.active_green)
            }
            1 -> {
                status_button.setText("Caution")
                status_button.setBackgroundResource(R.color.caution_yellow)
            }
            2 -> {
                status_button.setText("Hazard")
                status_button.setBackgroundResource(R.color.hazard_red)
            }
            3 -> {
                status_button.setText("Offline")
                status_button.setBackgroundResource(R.color.black)
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
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}