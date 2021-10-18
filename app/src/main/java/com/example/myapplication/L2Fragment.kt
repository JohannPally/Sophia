package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentL2Binding
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class L2Fragment : Fragment() {

    private var _binding: FragmentL2Binding? = null
    private val binding get() = _binding!!

    private var dbCtrl: DBController ? = null

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentL2Binding.inflate(inflater, container, false)
        return binding.root

    }

    val args: L2FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity;
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        //========================BINDINGS====================================

        val catArg = args.categoryPassed
        view.findViewById<TextView>(R.id.l2categoryText).text = catArg

        val rvDevs: RecyclerView = view.findViewById<RecyclerView>(R.id.rvDevices)
        //TODO: replace with real getter for categories
        val Devs = dbCtrl?.getDevs(catArg)
        val testDevs = setOf("Stethascope", "Deflibrilator")
        val adapter = Devs?.let { Device_Item_Adapter(catArg, it, navMod, findNavController()) }
        rvDevs.adapter = adapter
        rvDevs.layoutManager = LinearLayoutManager(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}