package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentL1Binding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class L1Fragment : Fragment() {

    private var _binding: FragmentL1Binding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    //TODO hook up to controller
    private var dbCtrl: DBController ? = null

    // Search Input
    private var searchInput: String? = null

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

        initTextFields(view)

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

        val L1toQRButton = view.findViewById<FloatingActionButton>(R.id.l1toqrbutton)
        L1toQRButton.setOnClickListener() {
            navMod.L1toQRSearch(findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initTextFields(view: View){

        //TODO: have to fill out these functions in the controller for the gets
        val l1TestingsearchTextView = view.findViewById<TextView>(R.id.l1searchTextView)
        val l1TestingsearchButton = view.findViewById<TextView>(R.id.l1searchButton)

        l1TestingsearchButton.setOnClickListener {

            searchInput = l1TestingsearchTextView.text.toString() // this is the search text input from view things

            val rvCats: RecyclerView = view.findViewById<RecyclerView>(R.id.rvCategories)
            //TODO: replace with real getter for categories
            val Cats = dbCtrl?.getCats()

            var catsAndDevs = mutableSetOf<String>()

            if (Cats != null) {
                for (cat in Cats) {
                    catsAndDevs.add(cat)
                }
                for (cat in Cats) {
                    dbCtrl?.getDevs(cat)?.forEach { item -> catsAndDevs.add("$cat.$item") }
                }
            }

            // Filter Set of Categories
            val filterCatsAndDevs = HashSet(catsAndDevs?.filter { catOrDevName -> catOrDevName.contains(searchInput.toString(), ignoreCase = true) })

            val adapter = filterCatsAndDevs?.let{Category_Item_Adapter(it, navMod, findNavController())} // change here from Cats to filterCats
            rvCats.adapter = adapter
        }
    }
}