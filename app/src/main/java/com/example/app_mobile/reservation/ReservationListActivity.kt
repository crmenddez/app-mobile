package com.example.app_mobile.reservation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app_mobile.BaseDrawerActivity
import com.example.app_mobile.R
import com.example.app_mobile.data.ReservationStore
import com.example.app_mobile.databinding.ActivityReservationsBinding

class ReservationListActivity : BaseDrawerActivity() {

    private lateinit var binding: ActivityReservationsBinding
    private lateinit var adapter: ReservationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(binding.root)

        // usa el ID, así entra al override Int de BaseDrawerActivity
        setContentView(R.layout.activity_reservations)

        // bindea sobre la vista ya inflada dentro de content_frame
        val child = findViewById<android.widget.FrameLayout>(R.id.content_frame).getChildAt(0)
        binding = ActivityReservationsBinding.bind(child)

        //setSupportActionBar(binding.toolbar)
        //supportActionBar?.title = "Tus reservas"

        adapter = ReservationListAdapter { item ->
            startActivity(
                Intent(this, ReservationDetailActivity::class.java).apply {
                    putExtra("title", item.title)
                    putExtra("desc", item.description)
                    putExtra("start", item.start)
                    putExtra("end", item.end)
                    putExtra("image", item.imageRes)
                }
            )
        }

        binding.rvReservations.adapter = adapter
        binding.rvReservations.setHasFixedSize(true)
        val data = listOf(
            Reservation(
                title = "Cancha de Futbol 11 personas",
                description = "Cancha de Fútbol. Exclusivo para asociados e invitados. Aforo máx. 12",
                start = "09:00",
                end = "10:00",
                imageRes = R.drawable.ic_cancha_futbol
            ),
            Reservation(
                title = "Cancha de Futbol 13 personas",
                description = "Cancha de Fútbol. Exclusivo para asociados e invitados. Aforo máx. 12",
                start = "09:00",
                end = "10:00",
                imageRes = R.drawable.ic_cancha_futbol
            )
        )
        adapter.submitList(data)
    }

    override fun onResume() {
        super.onResume()
        val reservas = ReservationStore.getReservations(this)
        // TODO: pásalas a tu RecyclerView/Adapter existente
        // Por ejemplo: adapter.submitList(reservas)
    }
}