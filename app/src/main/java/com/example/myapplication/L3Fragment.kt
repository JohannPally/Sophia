package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentL3Binding
import androidx.navigation.fragment.navArgs


class L3Fragment : Fragment() {

    private var _binding: FragmentL3Binding? = null
    private val binding get() = _binding!!

    private var ctrl: DBController ? = null

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentL3Binding.inflate(inflater, container, false)
        return binding.root

    }

    val args: L3FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity;
        if (activity is MainActivity) {
            ctrl = activity.dbctrl;
        }

        //========================BINDINGS====================================

        binding.l3backButton.setOnClickListener {
            findNavController().navigate(R.id.action_L3Fragment_to_L2Fragment)
        }

        binding.l3minusButton.setOnClickListener {
//            val cntTextVal = view.findViewById<TextView>(R.id.l3countText)
//            val teststr = dev?.getTest()
//            cntTextVal.setText(teststr)
//            val cntTextVal = view.findViewById<TextView>(R.id.l3countText)
//            val cntInt = cntTextVal.text.toString().toInt()
//            cntTextVal.setText((cntInt-1).toString())
//
//            val myToast = Toast.makeText(context, "Decremented "+args.deviceName+" count by 1!", Toast.LENGTH_SHORT)
//            myToast.show()
        }

//        val devArg = "Device, " + args.deviceName + ":"
//        view.findViewById<TextView>(R.id.l3deviceText).text = devArg
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}