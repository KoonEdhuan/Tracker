package com.example.tracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tracker.R
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToTrackingFragment(intent)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment



        setSupportActionBar(binding.toolbar)
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.settingsFragment, R.id.statisticsFragment, R.id.runFragment ->
                        binding.bottomNavigationView.visibility = View.VISIBLE
                    else -> binding.bottomNavigationView.visibility = View.GONE
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

    private fun navigateToTrackingFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }
}