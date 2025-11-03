package com.example.android_tv_frontend

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Main Activity for Android TV.
 *
 * Provides a modern TV-optimized UI with:
 * - Left-side navigation (Home, Search, Favorites, Settings)
 * - Top search bar with placeholder for voice input
 * - Central content grid using RecyclerView
 * - Ocean Professional theme colors and focus states
 *
 * Navigation and DPAD behavior:
 * - All interactive elements are focusable with visual feedback
 * - DPAD transitions prioritize left-nav -> search -> grid
 */
class MainActivity : FragmentActivity() {

    private lateinit var grid: RecyclerView
    private lateinit var search: EditText
    private lateinit var voiceButton: TextView

    private lateinit var navHome: TextView
    private lateinit var navSearch: TextView
    private lateinit var navLibrary: TextView
    private lateinit var navSettings: TextView

    private val homeData = sampleData("Home")
    private val searchData = sampleData("Search")
    private val libraryData = sampleData("Favorites")
    private val settingsData = sampleData("Settings")

    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        grid = findViewById(R.id.content_grid)
        search = findViewById(R.id.search_bar)
        voiceButton = findViewById(R.id.voice_button)

        navHome = findViewById(R.id.nav_home)
        navSearch = findViewById(R.id.nav_search)
        navLibrary = findViewById(R.id.nav_library)
        navSettings = findViewById(R.id.nav_settings)

        // Setup grid with 5 columns; maintain TV-safe aspect ratios in card item
        val layoutManager = GridLayoutManager(this, 5)
        grid.layoutManager = layoutManager
        adapter = CardAdapter(homeData)
        grid.adapter = adapter

        // Ensure initial focus starts at left nav home
        navHome.isFocusable = true
        navHome.requestFocus()

        // Left-nav click/OK behaviors to swap datasets
        navHome.setOnClickListener { swapData(homeData) }
        navSearch.setOnClickListener { swapData(searchData) }
        navLibrary.setOnClickListener { swapData(libraryData) }
        navSettings.setOnClickListener { swapData(settingsData) }

        // Voice button placeholder
        voiceButton.setOnClickListener {
            // Placeholder for voice search trigger
            // In a full implementation, integrate SpeechRecognizer or Assistant
        }

        // Keyboard action for search
        search.setOnEditorActionListener { _, _, _ ->
            swapData(sampleData("Results"))
            true
        }
    }

    private fun swapData(newData: List<CardItem>) {
        adapter.update(newData)
        // Move focus to grid after swapping to improve flow
        grid.post { grid.requestFocus() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Handle TV remote control inputs
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER -> {
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    // PUBLIC_INTERFACE
    fun sampleData(section: String): List<CardItem> {
        /** Returns sample content data for the given section. */
        val items = mutableListOf<CardItem>()
        for (i in 1..30) {
            items.add(
                CardItem(
                    title = "$section Item $i",
                    subtitle = "Subtitle $i"
                )
            )
        }
        return items
    }
}

/** Data model for a content card. */
data class CardItem(
    val title: String,
    val subtitle: String
)

/**
 * RecyclerView Adapter for TV content cards with focus scaling and outline.
 */
class CardAdapter(private var data: List<CardItem>) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val item = CardItemView(parent.context)
        val params = RecyclerView.LayoutParams(
            (parent.measuredWidth * 0.16).toInt().coerceAtLeast(240),
            (parent.measuredHeight * 0.28).toInt().coerceAtLeast(200)
        )
        item.layoutParams = params
        return CardViewHolder(item)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun update(newData: List<CardItem>) {
        data = newData
        notifyDataSetChanged()
    }
}

class CardViewHolder(private val view: CardItemView) : RecyclerView.ViewHolder(view) {
    fun bind(item: CardItem) {
        view.bind(item)
    }
}

/**
 * Simple TV card view with focus scaling and outline visual.
 */
class CardItemView(context: android.content.Context) : androidx.appcompat.widget.LinearLayoutCompat(context) {

    private val title: TextView
    private val subtitle: TextView

    init {
        orientation = VERTICAL
        setPadding(16, 16, 16, 16)
        background = androidx.core.content.ContextCompat.getDrawable(context, R.drawable.bg_surface_round)
        // Use a background selector to render focus outline for API 21+
        this.setBackgroundResource(R.drawable.bg_card_focusable)
        isFocusable = true
        isFocusableInTouchMode = true
        elevation = 8f

        title = TextView(context).apply {
            setTextColor(Color.parseColor("#111827"))
            textSize = 20f
        }
        subtitle = TextView(context).apply {
            setTextColor(Color.parseColor("#4B5563"))
            textSize = 16f
        }

        addView(title, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        addView(subtitle, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        // Focus scaling animation
        setOnFocusChangeListener { v, hasFocus ->
            v.animate().scaleX(if (hasFocus) 1.06f else 1.0f)
                .scaleY(if (hasFocus) 1.06f else 1.0f)
                .setDuration(120)
                .start()
        }
    }

    fun bind(item: CardItem) {
        title.text = item.title
        subtitle.text = item.subtitle
    }
}
