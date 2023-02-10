package com.demir.musicplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.demir.musicplayer.R
import com.demir.musicplayer.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController= Navigation.findNavController(this,R.id.fragmentContainer)

        binding.chipNav.setItemSelected(R.id.musicListFragment,true)

        binding.chipNav.setOnItemSelectedListener {
            when(it){
                R.id.musicListFragment->navController.navigate(R.id.musicListFragment)
                R.id.favoritesFragment->navController.navigate(R.id.favoritesFragment)

            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.playMusicFragment ->binding.chipNav.visibility = View.GONE
                else -> binding.chipNav.visibility = View.VISIBLE
            }
        }


    }
}