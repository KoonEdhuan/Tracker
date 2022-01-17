package com.example.tracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.tracker.R

/**
 * A simple [Fragment] subclass.
 * Use the [SetupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetupFragment : Fragment() {

    lateinit var tvContinue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvContinue = requireView().findViewById(R.id.tvContinue)

        tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_runFragment )
        }
    }
}