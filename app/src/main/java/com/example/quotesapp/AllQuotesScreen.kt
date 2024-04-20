package com.example.quotesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class AllQuotesScreen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var quoteAdapter: QuoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_quotes_screen)


        val gotoHomeScreen = findViewById<ImageButton>(R.id.imageButton2)
        gotoHomeScreen.setOnClickListener {
            val intent = Intent(applicationContext, HomeScreen::class.java)
            startActivity(intent)
        }
        val  gotoProfileScreen = findViewById<ImageButton>(R.id.imageButton3)
        gotoProfileScreen.setOnClickListener {
            val intent = Intent(applicationContext, ProfileScreen::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        quoteAdapter = QuoteAdapter()
        recyclerView.adapter = quoteAdapter

        fetchQuotes()
    }
    private fun fetchQuotes() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://type.fit/api/quotes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val jsonStr = response.body!!.string()
                    val jsonArray = JSONArray(jsonStr)

                    val quotes = mutableListOf<Quote>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val text = jsonObject.getString("text")
                        val author = jsonObject.getString("author")
                        quotes.add(Quote(text, author))
                    }

                    runOnUiThread {
                        quoteAdapter.submitList(quotes)
                    }
                }
            }
        })
    }
}
data class Quote(val text: String, val author: String)

class QuoteAdapter : ListAdapter<Quote, QuoteViewHolder>(QuoteDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }
}

class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textTextView: MaterialTextView = itemView.findViewById(R.id.textTextView)
    private val authorTextView: MaterialTextView = itemView.findViewById(R.id.authorTextView)
    private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)

    fun bind(quote: Quote) {
        textTextView.text = quote.text
        authorTextView.text = quote.author
    }
}

class QuoteDiffCallback : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.text == newItem.text && oldItem.author == newItem.author
    }
}

