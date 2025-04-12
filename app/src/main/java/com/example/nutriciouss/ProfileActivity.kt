package com.example.nutriciouss

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutriciouss.databinding.ActivityHomeBinding
import com.example.nutriciouss.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Enabling bottom navigation bar functionality
        binding.bottomNavigation.selectedItemId = R.id.nav_profile  //this will highlight that we in this activity
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    finish()
                    // Handle profile icon click
                    true
                }

                R.id.nav_profile -> {
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