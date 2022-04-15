package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecurseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecurseFragment : Fragment() {
    private var dbCtrl: DBController ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurse, container, false)
    }

    private val navMod: NavMod by activityViewModels()
    val args: RecurseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var activity = activity
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl
        }

        //========================BINDINGS====================================
        val recurse_rv: RecyclerView = view.findViewById<RecyclerView>(R.id.recurse_rv)
        val both = dbCtrl?.get_all(args.parent)
        val levels = both?.first
        val mrs = both?.second

        println(both?.first.toString()+"\n"+both?.second.toString())
        if(levels!=null && mrs!=null){
            var adapter = Recurse_Item_Adapter(levels, mrs, navMod, findNavController(), 0)
            recurse_rv.adapter = adapter
            recurse_rv.layoutManager = LinearLayoutManager(activity)
        }

        val toAddButton: FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.toRecurseAddButton)
        toAddButton.setOnClickListener(){
           navMod.RecursetoRecurseAdd(findNavController(), args.parent)
        }

        val toSearchButton: FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.searchBtn)
        toSearchButton.setOnClickListener(){
            navMod.RecurseToSearch(findNavController())
        }

        val toQRSearchButton: FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.toQRSearchButton)
        toQRSearchButton.setOnClickListener(){
            navMod.RecursetoRecurseQRSearch(findNavController())
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecurseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecurseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
