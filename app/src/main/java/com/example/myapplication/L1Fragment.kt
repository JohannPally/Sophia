package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentL1Binding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class L1Fragment : Fragment() {

    private var _binding: FragmentL1Binding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Reference to the front end model that handles navigation from screen to screen
    private val feModel: FEModel by activityViewModels()

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

        view.findViewById<Button>(R.id.l1_next_Button).setOnClickListener {
            val catTextVal = view.findViewById<TextView>(R.id.l1categoryEditText)
            val catValString = catTextVal.text.toString()
            feModel.navigateToL2(catValString, findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}