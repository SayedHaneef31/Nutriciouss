package com.example.nutriciouss

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutriciouss.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Enabling bottom navigation bar functionality
        binding.bottomNavigation.selectedItemId =
            R.id.nav_search  //this will highlight that we in this activity
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_search -> {
                    // Handle profile icon click
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    // Handle profile icon click

                    true
                }

                R.id.nav_logs -> {
                    startActivity(Intent(this, LogActivity::class.java))
                    finish()
                    // Handle profile icon click
                    true

                }

                else -> false
            }
        }

        //Enabling toggle button in the search screen
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.foodRadio -> {


                    //Selected button config
                    binding.foodRadio.setBackgroundColor(ContextCompat.getColor(this, R.color.text_primary))
                    //binding.foodRadio.setTextColor(getResources().getColor(R.color.white))     /getColor() is depriciated now instead use this
                    binding.foodRadio.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.foodRadio.setTypeface(null, Typeface.BOLD)

                    //unselected button config
                    binding.nutrientRadio.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.nutrientRadio.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.nutrientRadio.setTypeface(null, Typeface.NORMAL)

                }

                R.id.nutrientRadio -> {

                    //Selected button config
                    binding.nutrientRadio.setBackgroundColor(ContextCompat.getColor(this, R.color.text_primary))
                    binding.nutrientRadio.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.nutrientRadio.setTypeface(null, Typeface.BOLD)

                    //unselected button config
                    binding.foodRadio.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.foodRadio.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.foodRadio.setTypeface(null, Typeface.NORMAL)

                }

            }
        }
        // Manually trigger default selection style update
        binding.radioGroup.check(R.id.foodRadio)  // programmatically selects the food radio button and triggers the setOnCheckedChangeListener block.
        // AND YES ORDERING MATTERS
        //IF I ADD THIS ABOVE THE LISTENER THAN  it WILL HAVE no idea it was supposed to change colors and fonts — no listener = no action.
    }
}
