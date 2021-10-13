package com.example.myapplication;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentQrBinding
import androidx.navigation.fragment.navArgs

class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!

    private var ctrl: DBController ? = null

    // Reference to the front end model that handles navigation from screen to screen
    private val navMod: NavMod by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root

    }

    //val args: QRFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var activity = activity as? MainActivity
        if (activity != null) ctrl = activity.dbctrl;
        else {
            println("Failed to cast activity as MainActivity in QR Fragment")
        }

        //========================BINDINGS====================================


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
