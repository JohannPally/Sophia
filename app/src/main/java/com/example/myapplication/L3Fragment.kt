package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentL3Binding
import androidx.navigation.fragment.navArgs


class L3Fragment : Fragment() {

private var _binding: FragmentL3Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentL3Binding.inflate(inflater, container, false)
        return binding.root

    }

    val args: L3FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("--------------------L3 Created----------------------------------")
        super.onViewCreated(view, savedInstanceState)

        binding.l3backButton.setOnClickListener {
            findNavController().navigate(R.id.action_L3Fragment_to_L2Fragment)
        }

        binding.l3minusButton.setOnClickListener {
            val cntTextVal = view.findViewById<TextView>(R.id.l3countText)
            val cntInt = cntTextVal.text.toString().toInt()
            cntTextVal.setText((cntInt-1).toString())

            val myToast = Toast.makeText(context, "Decremented "+args.deviceName+" count by 1!", Toast.LENGTH_SHORT)
            myToast.show()
        }

        val devArg = "Device, " + args.deviceName + ":"
        view.findViewById<TextView>(R.id.l3deviceText).text = devArg
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

    /*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment L3Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            L3Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
     */
}