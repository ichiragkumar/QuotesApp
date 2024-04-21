package com.example.quotesapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth


class HomeScreen : AppCompatActivity() {

    private val imageUrls = listOf(
        "https://img.freepik.com/free-vector/quote-design_1319-116.jpg?t=st=1713614706~exp=1713618306~hmac=7ee9b333d64e373320aa9539653b4938132dc427480f412a881e0fb1d37ed732&w=740",
        "https://img.freepik.com/premium-vector/be-who-you-are-poster-tshirt-quotes-template-design_496281-815.jpg?w=740",
        "https://img.freepik.com/premium-photo/inspirational-motivational-quotes-count-day-make-day-count_698447-1252.jpg?w=360",
        "https://img.freepik.com/free-photo/creative-inspirational-resource_23-2149144196.jpg?t=st=1713619004~exp=1713622604~hmac=28d72f3a3e6defeea519d7b33860fda22dae1e5a1f4f1eef8146f786c4521e61&w=996",
        "https://img.freepik.com/free-vector/love-lettering-concept_23-2148446766.jpg?t=st=1713619031~exp=1713622631~hmac=cfe480864da7de702403affb9e775f818591960145967f3c44fd83ba6da593e9&w=740",
        "https://img.freepik.com/premium-vector/feel-good-positive-typographic-inspirational-poster-with-life-motivation-tshirt-design_485063-104.jpg?w=740",
        "https://img.freepik.com/premium-vector/never-give-up-typography-t-shirt-design-etc_384468-1564.jpg?w=996",
        "https://img.freepik.com/premium-vector/your-only-limit-vector-illustration-quote-life_555057-1432.jpg?w=740",
        "https://img.freepik.com/premium-vector/nothing-impossible-motivational-quotes-t-shirt-design-graphic-vector_555057-569.jpg?w=740",
        )

    private var currentImageIndex = 0


    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home_screen)

        imageView = findViewById(R.id.imageView)



        val gotoallQuotes = findViewById<ImageButton>(R.id.imageButton)
        gotoallQuotes.setOnClickListener {
            val intent = Intent(applicationContext, AllQuotesScreen::class.java)
            startActivity(intent)
        }

        val  gotoProfileScreen = findViewById<ImageButton>(R.id.imageButton3)
        gotoProfileScreen.setOnClickListener {
            val intent = Intent(applicationContext, ProfileScreen::class.java)
            startActivity(intent)
        }

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            showNextImage()

        }

        val previousButton = findViewById<Button>(R.id.previousButton)
        previousButton.setOnClickListener {
            showPreviousImage()

        }

        showImage(currentImageIndex)

    }
    private fun showNextImage() {
        currentImageIndex = (currentImageIndex + 1) % imageUrls.size
        showImage(currentImageIndex)
    }

    private fun showPreviousImage() {
        currentImageIndex = if (currentImageIndex > 0) currentImageIndex - 1 else imageUrls.size - 1
        showImage(currentImageIndex)
    }


    private fun showImage(index: Int) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE // Show progress bar

        Glide.with(this)
            .load(imageUrls[index])
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.INVISIBLE
                    showToast("Failed to load image")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.INVISIBLE
                    return false
                }
            })
            .into(imageView)
    }


    private fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    }


}