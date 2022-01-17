package com.example.tracker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tracker.R
import com.example.tracker.databinding.FragmentTrackingBinding
import com.example.tracker.others.Constants
import com.example.tracker.others.Constants.ACTION_PAUSE_SERVICE
import com.example.tracker.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.tracker.others.Constants.ACTION_STOP_SERVICE
import com.example.tracker.others.Constants.MAP_ZOOM
import com.example.tracker.others.Constants.POLYLINE_WIDTH
import com.example.tracker.others.Constants.POlYLINE_COLOR
import com.example.tracker.others.TrackingUtility
import com.example.tracker.services.Polyline
import com.example.tracker.services.Polylines
import com.example.tracker.services.TrackingService
import com.example.tracker.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [TrackingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class TrackingFragment : Fragment() {

    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: MainViewModel by viewModels()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L

    private var map: GoogleMap? = null

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTrackingBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)

        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }

        binding.mapView.getMapAsync{
            map = it
            addAllPolyLines()
        }

        subscribeToObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentTimeInMillis > 0){
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miCancelTracking ->
                showCancelTrackingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme)
            .setTitle("Cancel the run ?")
            .setMessage("Discard Tracking Data ?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Discard") {_,_ ->
                stopRun()
            }
            .setNegativeButton("Cancel") {dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun(){
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }


    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })
        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedTime(currentTimeInMillis,true)
            binding.tvTimer.text = formattedTime
        })
    }

    private fun toggleRun(){
        if (isTracking){
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking){
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                pathPoints.last().last(),MAP_ZOOM
            ))
        } else{
            Toast.makeText(context,"pathPoints is empty for camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addAllPolyLines() {
        for (polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POlYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val secondLastLatLong = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLong = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POlYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(secondLastLatLong)
                .add(lastLatLong)
            map?.addPolyline(polylineOptions)
        } else{
            Toast.makeText(context,"pathPoints is empty for latest Polyline", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(),TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}