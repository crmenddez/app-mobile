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

class ReservoCorrectamenteActivity : BaseDrawerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reservo_correctamente)

        //Segundo metodo para ir a una actividad
        val btn1 = findViewById<Button>(R.id.btnRegresar)
        btn1.setOnClickListener {
            val intent = Intent(
                this,
                ReservationListActivity::class.java
            ) //Creamos la referencia a la actividad
            Toast.makeText(this, "Confirmado Correctamente", Toast.LENGTH_SHORT).show()
            startActivity(intent) //nos vamos a la actividad

        }
    }
}