package com.example.app_mobile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class ReservarActivity : BaseDrawerActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnFechaInicio: Button
    private lateinit var btnFechaFinal: Button
    private lateinit var btnBuscar: Button
    private lateinit var rvHorarios: RecyclerView
    private lateinit var spInvitados: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservar)



        tvTimer = findViewById(R.id.tvTimer)
        btnFechaInicio = findViewById(R.id.btnFechaInicio)
        btnFechaFinal = findViewById(R.id.btnFechaFinal)
        btnBuscar = findViewById(R.id.btnHorario)
        rvHorarios = findViewById(R.id.rvHorarios)
        spInvitados = findViewById(R.id.spInvitados)

        // RecyclerView listo (¡esto era clave!)
        rvHorarios.layoutManager = LinearLayoutManager(this)
        rvHorarios.visibility = View.GONE

        // Timer 5 min
        startTimer(5 * 60 * 1000)

        // DatePickers
        btnFechaInicio.setOnClickListener { showDatePicker(btnFechaInicio) }
        btnFechaFinal.setOnClickListener { showDatePicker(btnFechaFinal) }

        // Spinner de invitados
        val invitados = listOf("0 invitados","1 invitado","2 invitados","3 invitados","4 invitados","5 invitados")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, invitados).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spInvitados.adapter = adapter

        // Buscar horarios
        btnBuscar.setOnClickListener {
            val horarios = generarHorariosDisponibles()
            if (horarios.isEmpty()) {
                rvHorarios.visibility = View.GONE
                Toast.makeText(this, "No hay horarios disponibles", Toast.LENGTH_SHORT).show()
            } else {
                rvHorarios.visibility = View.VISIBLE
                val horarioAdapter = HorarioAdapter(horarios) { slot ->
                    // Navegar a Pago con datos básicos
                    val i = Intent(this, PagoActivity::class.java).apply {
                        putExtra("FECHA_INICIO", btnFechaInicio.text?.toString() ?: "")
                        putExtra("FECHA_FIN", btnFechaFinal.text?.toString() ?: "")
                        putExtra("INVITADOS", spInvitados.selectedItem?.toString() ?: "0 invitados")
                        putExtra("HORARIO", slot)
                    }
                    startActivity(i)
                }
                rvHorarios.adapter = horarioAdapter
            }
        }
    }

    private fun startTimer(timeInMillis: Long) {
        object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(ms: Long) {
                val m = (ms / 1000) / 60
                val s = (ms / 1000) % 60
                tvTimer.text = "Tiempo restante para reservar: %02d:%02d".format(m, s)
            }
            override fun onFinish() { tvTimer.text = "Tiempo agotado" }
        }.start()
    }

    private fun showDatePicker(button: Button) {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            val fecha = "$d/${m + 1}/$y"
            button.text = fecha
            if (button.id == R.id.btnFechaInicio) btnFechaFinal.text = fecha
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun generarHorariosDisponibles(): List<String> {
        val lista = mutableListOf<String>()
        var hIni = 7
        val hFin = 22
        while (hIni < hFin) {
            val desde = String.format("%02d:00", hIni)
            val hasta = String.format("%02d:00", hIni + 1)
            lista.add("$desde - $hasta")
            hIni++
        }
        return lista
    }
}
