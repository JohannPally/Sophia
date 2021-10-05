package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentL1Binding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class L1Fragment : Fragment() {

    private var _binding: FragmentL1Binding? = null

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

        _binding = FragmentL1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity;
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        //========================BINDINGS====================================

        val rvCats: RecyclerView = view.findViewById<RecyclerView>(R.id.rvCategories)
        //TODO: replace with real getter for categories
        val Cats = dbCtrl?.getCats()
        Log.v("Cats",dbCtrl.toString())
        val testCats = setOf("Emergency Room", "Operating Room", "Intensive Care Unit", "Cardiac Care Unit")
        val adapter = Cats?.let{Category_Item_Adapter(it, navMod, findNavController())}
        //TODO: Maybe instead of changing how the RV fits in the view, space the textview and button themselves in item layout
        rvCats.adapter = adapter
        rvCats.layoutManager = LinearLayoutManager(activity)

//        OLD BUTTONS
    //        view.findViewById<Button>(R.id.l1_next_Button).setOnClickListener {
//            val catTextVal = view.findViewById<TextView>(R.id.l1categoryEditText)
//            val catValString = catTextVal.text.toString()
//            navMod.navigateToL2(catValString, findNavController())
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}