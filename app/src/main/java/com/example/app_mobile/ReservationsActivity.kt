package com.example.app_mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_mobile.databinding.ActivityReservationsBinding

class ReservationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationsBinding
    private val adapter = ReservationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.rvReservations.adapter = adapter
        binding.rvReservations.setHasFixedSize(true)

        val data = listOf(
            Reservation(
                title = "Cancha de Futbol 12 personas",
                description = "Cancha de Fútbol. Exclusivo para asociados e invitados. Aforo máx. 12",
                start = "09:00",
                end = "10:00",
                imageRes = R.drawable.ic_cancha_futbol   // <-- recurso local
            )
        )
        adapter.submitList(data)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Tus reservas"
    }
}