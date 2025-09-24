package com.example.app_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_mobile.reservation.ReservationListActivity

class cancelar_reserva : BaseDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelar_reserva)

        //Segundo metodo para ir a una actividad
        val btn1 = findViewById<Button>(R.id.btnSi)
        btn1.setOnClickListener {
            val intent = Intent(this, ReservationListActivity::class.java) //Creamos la referencia a la actividad
            Toast.makeText(this, "Cancelado Correctamente", Toast.LENGTH_SHORT).show()
            startActivity(intent) //nos vamos a la actividad
        }
    }
}