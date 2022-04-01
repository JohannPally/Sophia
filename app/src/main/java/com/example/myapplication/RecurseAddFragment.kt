package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecurseAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecurseAddFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_recurse_add, container, false)
    }

    private val navMod: NavMod by activityViewModels()
    val args: RecurseFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity
        if (activity is MainActivity) {
            dbCtrl = activity.dbctrl;
        }

        initText(view, args)
        //======================BINDINGS=====================
        // TODO - Click a button, scan qr code and get the ID from there.
        val nameTV: EditText = view.findViewById<EditText>(R.id.nameET_radd)
        val qridvalTV: TextView = view.findViewById<TextView>(R.id.qridvalTV_radd)
        val scanIDButton: FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.scanIDButton_radd)
        scanIDButton.setOnClickListener(){
            navMod.RecurseAddtoRecurseAddQRScan(navController = findNavController(), p = args.parent, rN = nameTV.text.toString(), rI = qridvalTV.text.toString());
//            var id = dbCtrl?.add_mr(devName = nameTV.text.toString(), qrid = Integer.parseInt(qrData), parent = args.parent)
//            if (id != null) {
//                navMod.RecurseAddtoInfo(findNavController(), id.toInt())
//            }
        }

        //val qridTV: EditText = view.findViewById<EditText>(R.id.qridET_radd)


        val addButton: FloatingActionButton = view.findViewById<FloatingActionButton>(R.id.addButton_radd)
        val qridTV: EditText = view.findViewById<EditText>(R.id.qridET_radd)
        addButton.setOnClickListener(){
            var id = dbCtrl?.add_mr(devName = nameTV.text.toString(), qrid = Integer.parseInt(qridTV.text.toString()), parent = args.parent)
            if (id != null) {
                navMod.RecurseAddtoInfo(findNavController(), id.toInt())
            }
        }

    }

    public fun initText(v:View, a:RecurseAddFragmentArgs){
        val nameTV: EditText = v.findViewById<EditText>(R.id.nameET_radd)
        val qridvalTV: TextView = v.findViewById<TextView>(R.id.qridvalTV_radd)
        nameTV.setText(args.radd)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecurseAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecurseAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}