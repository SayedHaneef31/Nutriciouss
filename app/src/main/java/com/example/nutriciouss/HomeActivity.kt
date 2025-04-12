package com.example.nutriciouss

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutriciouss.databinding.ActivityHomeBinding
import com.example.nutriciouss.databinding.CalorieCardBinding
import com.example.nutriciouss.databinding.NutrientCardBinding
import com.example.nutriciouss.databinding.WaterCardBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Setting up the view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Accessing the caloriecard components using view binding
        val calorieCardView = findViewById<View>(R.id.calorieCard)
        val calorieCardBinding = CalorieCardBinding.bind(calorieCardView)
        var calorieCal = calorieCardBinding.calorieLeftidddd
        calorieCal.text = "600 kcal left"

        // Accessing the nutrientcard components using view binding
        val nutrientCardView = findViewById<View>(R.id.nutrientCard)
        val nutrientCardBinding = NutrientCardBinding.bind(nutrientCardView)
        var nutrientCal = nutrientCardBinding.nutrientinfoidddd
        nutrientCal.text = "Eat more Protien"

        // Accessing the watercard components using view binding
        val waterCardView = findViewById<View>(R.id.waterCard)
        val waterCardBinding = WaterCardBinding.bind(waterCardView)
        var waterCal = waterCardBinding.waterLeftidddd
        waterCal.text = "8 out of 8 glasses!!"

        // Enabling bottom navigation bar functionality
        binding.bottomNavigation.selectedItemId = R.id.nav_home  //this will highlight that we in this activity
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home icon click
                    true
                }

                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    finish()
//                    overridePendingTransition(0, 0) // No animation, better experience though depreciated now
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
    }
}