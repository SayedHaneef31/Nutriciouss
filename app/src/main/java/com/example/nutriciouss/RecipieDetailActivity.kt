package com.example.nutriciouss

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class RecipieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipie_detail)

        val recipeImage = findViewById<ImageView>(R.id.imageView4)
        val recipeTitle = findViewById<TextView>(R.id.textView8)
        val readyInMin = findViewById<TextView>(R.id.readyinminuteeiddddddddd)
        val healthScore = findViewById<TextView>(R.id.healthhhhhhhhscoreidd)
        val summary = findViewById<TextView>(R.id.summaryiddddddddddddddddd)

        val imageUrl = intent.getStringExtra("image_url")
        val title = intent.getStringExtra("title")
        val readyInMinutes = intent.getIntExtra("readyInMinutes", 0)
        val health = intent.getIntExtra("healthScore", 0)
        val summaryHtml = intent.getStringExtra("summary")

        Glide.with(this).load(imageUrl).into(recipeImage)
        recipeTitle.text = title
        readyInMin.text = "Ready in $readyInMinutes min"
        healthScore.text = "Health Score: $health"

        //summary.text = summaryHtml  //for plain text
        // If summary contains HTML tags, convert them to styled text
        summary.text = Html.fromHtml(summaryHtml ?: "No summary available", Html.FROM_HTML_MODE_LEGACY)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}