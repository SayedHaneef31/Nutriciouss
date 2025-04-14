package com.example.nutriciouss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.nutriciouss.Data.RecipieSuggestion
import com.example.nutriciouss.Retrofit.RetrofitInstance.api
import com.example.nutriciouss.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: ArrayAdapter<String>
    private val suggestions = mutableListOf<RecipieSuggestion>() // Store the full objects
    private val suggestionTitles = mutableListOf<String>() // Store just the titles for display
    private var lastQuery: String? = null

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
        binding.bottomNavigation.selectedItemId = R.id.nav_search  //this will highlight that we in this activity
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_search -> {
                    // Already on search page
                    true
                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_logs -> {
                    startActivity(Intent(this, LogActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }

        //Search bar configuration
        setupSearchBar()
        
    }

    private fun setupSearchBar() {
        // Initialize the adapter with an empty list first
        adapter = ArrayAdapter(this, R.layout.dropdown_item_layout, ArrayList<String>())

        // Configure the AutoCompleteTextView
        binding.searchBarIDDDD.setAdapter(adapter)
        binding.searchBarIDDDD.threshold = 1

        try {
            // These properties may not be supported on all devices/implementations
            binding.searchBarIDDDD.setDropDownBackgroundResource(android.R.color.white)
        } catch (e: Exception) {
            Log.e("SearchBar", "Error setting dropdown background: ${e.message}")
        }

        // Set up item click listener
        binding.searchBarIDDDD.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedSuggestion = suggestions[position]
            Toast.makeText(this, "Selected: ${selectedSuggestion.title}", Toast.LENGTH_SHORT).show()

            // Clear search
            binding.searchBarIDDDD.setText("")

            // Navigate
            navigateToFoodDetail(selectedSuggestion)
        }

        // Focus the search bar and show dropdown
        binding.searchBarIDDDD.requestFocus()

        // Set up text change listener with debounce
        binding.searchBarIDDDD.doOnTextChanged { text, _, _, _ ->
            Log.e("Checkkkkkkk", "Text changing in the search")
            val query = text.toString().trim()

            if (query.length >= 1 && query != lastQuery) {
                lastQuery = query
                Log.e("Checkkkkkkk", "Query length reached >2")

                lifecycleScope.launch {
                    delay(300) // debounce delay
                    val queryResult = fetchSuggestions(query)
                    Log.e("Checkkkkkkk", queryResult.toString())

                    withContext(Dispatchers.Main) {
                        updateSuggestions(queryResult)
                    }
                }
            } else if (query.isEmpty()) {
                // Clear suggestions when query is empty
                clearSuggestions()
            }
        }
    }

    private fun handleItemSelection(selectedTitle: String) {

        // Find the corresponding suggestion object by matching title
        val selectedSuggestion = suggestions.find { it.title == selectedTitle }

        if (selectedSuggestion != null) {
            // Show a toast with the selected item
            Toast.makeText(this, "Selected: ${selectedSuggestion.title}", Toast.LENGTH_SHORT).show()

            // Clear the search text after selection
            binding.searchBarIDDDD.setText("")

            // Navigate to recipe detail with the ID
            navigateToFoodDetail(selectedSuggestion)
        } else {
            Toast.makeText(this, "Could not find recipe details", Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigateToFoodDetail(recipeWithId: RecipieSuggestion) {
        lifecycleScope.launch {
            try {
                Log.e("Recipe", "Fetching details for ID: ${recipeWithId.id}")

                val recipeDetails = api.getRecipeInformation(
                    recipeWithId.id,
                    "0a1d7e19d1f047028d945a2c42bf4a6a",
                    true
                )

                Log.d("Recipe", "Received details: $recipeDetails")

                val intent = Intent(this@SearchActivity, RecipieDetailActivity::class.java).apply {
                    putExtra("image_url", recipeDetails.image)
                    putExtra("title", recipeDetails.title)
                    putExtra("readyInMinutes", recipeDetails.readyInMinutes)
                    putExtra("healthScore", recipeDetails.healthScore)
                    putExtra("summary", recipeDetails.summary)
                }
                startActivity(intent)

            } catch (e: Exception) {
                Log.e("Recipe", "Error fetching recipe details: ${e.message}")
                e.printStackTrace()
                Toast.makeText(
                    this@SearchActivity,
                    "Failed to load details for ${recipeWithId.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun clearSuggestions() {
        adapter.clear()
        adapter.notifyDataSetChanged()
    }

    private suspend fun fetchSuggestions(query: String): List<RecipieSuggestion> {
        Log.e("Checkkkkkkk", "Going to fetch suggestions")
        return try {
            val response = api.autoCompleteSearch("0a1d7e19d1f047028d945a2c42bf4a6a", query)
            Log.e("Checkkkkkkk", "Response fetched")
            response   // Return the full response objects
        } catch (e: Exception) {
            Log.e("Search", "Error fetching suggestions: ${e.message}")
            emptyList()
        }
    }

    private fun updateSuggestions(newSuggestions: List<RecipieSuggestion>) {
        Log.e("Checkkkkkkk", "Updating suggestions: ${newSuggestions.size} items")

        // Store full suggestion objects
        suggestions.clear()
        suggestions.addAll(newSuggestions)

        // Extract titles for display
        suggestionTitles.clear()
        suggestionTitles.addAll(newSuggestions.map { it.title })

        // Update the adapter with new suggestions
        adapter.clear()
        adapter.addAll(suggestionTitles)
        adapter.notifyDataSetChanged()

        // Log suggestion details
        newSuggestions.forEachIndexed { index, item ->
            Log.d("Suggestion", "[$index] $item")
        }

        // Only show dropdown if we have suggestions and searchbar has focus
        if (binding.searchBarIDDDD.hasFocus() && newSuggestions.isNotEmpty()) {
            binding.searchBarIDDDD.post {
                binding.searchBarIDDDD.showDropDown()
            }
            Log.e("Checkkkkkkk", "Showing dropdown with ${newSuggestions.size} suggestions")
        } else {
            Log.e("Checkkkkkkk", "Not showing dropdown - hasFocus: ${binding.searchBarIDDDD.hasFocus()}, suggestions: ${newSuggestions.size}")
        }
    }
}