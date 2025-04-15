package com.example.nutriciouss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.nutriciouss.Data.MealPlanResponse
import com.example.nutriciouss.Data.Recipie
import com.example.nutriciouss.Retrofit.RetrofitInstance
import com.example.nutriciouss.Retrofit.RetrofitInstance.api
import com.example.nutriciouss.databinding.ActivityHomeBinding
import com.example.nutriciouss.databinding.BreakfastCardBinding
import com.example.nutriciouss.databinding.DinnerCardBinding
import com.example.nutriciouss.databinding.LunchCardBinding
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var randomRecipie: Recipie? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Setting up the view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)




//        ****************************************************************************
//        REMOVE THE BELOW COMMENTS AFTER TESTING
//        ****************************************************************************
//        //to update the random recipie on screen loading
//        generateRandomRecipie()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


//        Enabling the meal planner
        val dietOptions = listOf("ketogenic", "vegetarian", "vegan", "paleo", "primal","whole30")
        binding.btnGenerateMeal.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_target_diet, null)
            val editTextCalories = dialogView.findViewById<EditText>(R.id.editTextCalories)
            val spinnerDiet = dialogView.findViewById<Spinner>(R.id.spinnerDiet)

            // Set up the spinner adapter
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dietOptions)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDiet.adapter = adapter

            AlertDialog.Builder(this)
                .setTitle("Set Your Preferences")
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ ->
                    val targetCalories = editTextCalories.text.toString().toIntOrNull()
                    val selectedDiet = spinnerDiet.selectedItem.toString()

                    if (targetCalories != null) {
                        // Handle the values (e.g., save them, show them, etc.)
                        Toast.makeText(this, "Calories: $targetCalories kcal, Diet: $selectedDiet", Toast.LENGTH_SHORT).show()
                        getMealPlan(targetCalories, selectedDiet)
                    } else {
                        Toast.makeText(this, "Please enter valid calories", Toast.LENGTH_SHORT).show()
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()

        }



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

//
//        ****************************************************************************
//        REMOVE THE BELOW COMMENTS AFTER TESTING
//        ****************************************************************************
//        //Enabling random recipie button  btnRandomMeal healthScoreIDDDDD  randomMealTitleIDDDD
//        binding.btnRandomMeal.setOnClickListener {
//            generateRandomRecipie()
//        }

        //Enabling click listener for get detailed recipie
        binding.randomMealTitleIDDDD.setOnClickListener {
            val intent = Intent(this, RecipieDetailActivity::class.java)
            intent.putExtra("image_url", randomRecipie?.image)
            intent.putExtra("title", randomRecipie?.title)
            intent.putExtra("readyInMinutes", randomRecipie?.readyInMinutes)
            intent.putExtra("healthScore", randomRecipie?.healthScore)
            intent.putExtra("summary", randomRecipie?.summary)
            startActivity(intent)
        }
        binding.healthScoreIDDDDD.setOnClickListener {
            val intent = Intent(this, RecipieDetailActivity::class.java)
            intent.putExtra("image_url", randomRecipie?.image)
            intent.putExtra("title", randomRecipie?.title)
            intent.putExtra("readyInMinutes", randomRecipie?.readyInMinutes)
            intent.putExtra("healthScore", randomRecipie?.healthScore)
            intent.putExtra("summary", randomRecipie?.summary)
            startActivity(intent)
        }

    }

    private fun getMealPlan(targetCalories: Int, diet: String)
    {
        Log.e("MealPlan", "In getMealPlan() ")
        lifecycleScope.launch {
            try {
                val response = api.generateMealPlan(
                    "day",
                    targetCalories,
                    diet,
                    "0a1d7e19d1f047028d945a2c42bf4a6a"
                )
                Log.e("MealPlan", "In getMealPlan():::: Got some response ")
                Log.e("MealPlan", "In getMealPlan():::: ${response.meals[0].title} ")
                Log.e("MealPlan", "In getMealPlan():::: ${response.meals[1].title} ")
                Log.e("MealPlan", "In getMealPlan():::: ${response.meals[2].title} ")


                if(response.meals.isNotEmpty())
                {
                    showMealsOnCards(response)
                }
                 else {
                    Log.e("MealPlan", "Error: couldnt fetch meal plan")
                }
            } catch (e: Exception) {
                Log.e("MealPlan", "Exception: ${e.message}")
            }
        }
    }

    private fun showMealsOnCards(mealPlan: MealPlanResponse) {
        Log.e("MealPlan", "In showMealsOnCards() ")
        if (mealPlan.meals.size >= 3) {
            // Get references to your card bindings
            val breakfastView = findViewById<View>(R.id.breakfastCardddddd)
            val breakfastBinding = BreakfastCardBinding.bind(breakfastView)

            val lunchView = findViewById<View>(R.id.LunchCardddd)
            val lunchBinding = LunchCardBinding.bind(lunchView)

            val dinnerView = findViewById<View>(R.id.DinnerCarddddd)
            val dinnerBinding = DinnerCardBinding.bind(dinnerView)

            Log.e("MealPlan", "Binding done ")

            // Set Breakfast card content using its binding
            breakfastBinding.breakfastTitleIDDDDDD.text = mealPlan.meals[0].title

            // Set Lunch card content using its binding
            lunchBinding.LunchTitleIDDDDD.text = mealPlan.meals[1].title

            // Set Dinner card content using its binding
            dinnerBinding.dinnerTittleIDDDDDDDD.text = mealPlan.meals[2].title

            // Set click listeners to open RecipeDetailActivity
            breakfastView.setOnClickListener {
                navigateToFoodDetail(mealPlan.meals[0].id)
            }
            lunchView.setOnClickListener {
                navigateToFoodDetail(mealPlan.meals[1].id)
            }
            dinnerView.setOnClickListener {
                navigateToFoodDetail(mealPlan.meals[2].id)
            }
        }
    }

    private fun navigateToFoodDetail(id: Int) {
        lifecycleScope.launch {
            try {


                val recipeDetails = api.getRecipeInformation(
                    id,
                    "0a1d7e19d1f047028d945a2c42bf4a6a",
                    true
                )

                Log.d("Recipe", "Received details: $recipeDetails")

                val intent = Intent(this@HomeActivity, RecipieDetailActivity::class.java).apply {
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
                    this@HomeActivity,
                    "Failed to load details for food with id=${id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }




    private fun generateRandomRecipie() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getRandomRecipie("0a1d7e19d1f047028d945a2c42bf4a6a")
                if(response.recipes.isNotEmpty())
                {
                     randomRecipie = response.recipes[0]
                    binding.randomMealTitleIDDDD.text = "🥣 ${randomRecipie?.title}"
                    binding.healthScoreIDDDDD.text = "Ready in : ${randomRecipie?.readyInMinutes} | Health Score : ${randomRecipie?.healthScore}"

                }
                else {
                    Log.e("API_ERROR", "No recipes found in the response")
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}