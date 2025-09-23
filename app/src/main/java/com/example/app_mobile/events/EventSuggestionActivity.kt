package com.example.app_mobile.events

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_mobile.BaseDrawerActivity
import com.example.app_mobile.databinding.ActivityEventSuggestionBinding
import com.example.app_mobile.R

class EventSuggestionActivity : BaseDrawerActivity() {

    private lateinit var binding: ActivityEventSuggestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventSuggestionBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_event_suggestion)

        //setSupportActionBar(binding.toolbar)
        //supportActionBar?.title = "Real Club de Lima"

        binding.btnSend.setOnClickListener {
            Toast.makeText(this, "Sugerencia enviada (demo)", Toast.LENGTH_SHORT).show()
        }
    }
}
