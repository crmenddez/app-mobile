package com.example.app_mobile.reservation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_mobile.databinding.ActivityReservationDetailBinding

class ReservationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val title = intent.getStringExtra("title").orEmpty()
        val desc  = intent.getStringExtra("desc").orEmpty()
        val start = intent.getStringExtra("start").orEmpty()
        val end   = intent.getStringExtra("end").orEmpty()
        val img   = intent.getIntExtra("image", 0)

        if (title.isEmpty()) {
            Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        if (img != 0) binding.img.setImageResource(img)
        binding.tvTitle.text = title
        binding.tvSubtitle.text = desc
        binding.tvHour.text = "$start - $end"

        binding.tvDate.text = ""
        binding.tvModality.text = ""

        binding.btnConfirm.setOnClickListener {
            Toast.makeText(this, "Reservation confirmed", Toast.LENGTH_SHORT).show()
        }
        binding.btnCancel.setOnClickListener {
            Toast.makeText(this, "Reservation canceled", Toast.LENGTH_SHORT).show()
        }
    }
}
