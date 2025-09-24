package com.example.app_mobile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mobile.data.Reservation
import com.example.app_mobile.data.ReservationStore
import com.example.app_mobile.reservation.ReservationListActivity
import java.util.Calendar

class ReservarActivity : BaseDrawerActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnFechaInicio: Button
    private lateinit var btnFechaFinal: Button
    private lateinit var btnBuscar: Button
    private lateinit var rvHorarios: RecyclerView
    private lateinit var spInvitados: Spinner
    private lateinit var rgAreas: RadioGroup
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservar)

        tvTimer        = findViewById(R.id.tvTimer)
        btnFechaInicio = findViewById(R.id.btnFechaInicio)
        btnFechaFinal  = findViewById(R.id.btnFechaFinal)
        btnBuscar      = findViewById(R.id.btnHorario)
        rvHorarios     = findViewById(R.id.rvHorarios)
        spInvitados    = findViewById(R.id.spInvitados)
        rgAreas        = findViewById(R.id.rgAreas)

        // RecyclerView listo
        rvHorarios.layoutManager = LinearLayoutManager(this)
        rvHorarios.visibility = View.GONE

        // Timer 5 min
        startTimer(5 * 60 * 1000)

        // DatePickers
        btnFechaInicio.setOnClickListener { showDatePicker(btnFechaInicio) }
        btnFechaFinal.setOnClickListener { showDatePicker(btnFechaFinal) }

        // Spinner de invitados
        val invitados = listOf("0 invitados","1 invitado","2 invitados","3 invitados","4 invitados","5 invitados")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, invitados).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spInvitados.adapter = spinnerAdapter

        // Buscar horarios
        btnBuscar.setOnClickListener {
            val horarios = generarHorariosDisponibles()
            if (horarios.isEmpty()) {
                rvHorarios.visibility = View.GONE
                Toast.makeText(this, "No hay horarios disponibles", Toast.LENGTH_SHORT).show()
            } else {
                rvHorarios.visibility = View.VISIBLE
                val horarioAdapter = HorarioAdapter(horarios) { slot ->
                    // ← Al seleccionar un horario:
                    val areaSeleccionada = getSelectedAreaText()
                    val fechaIni = btnFechaInicio.text?.toString().orEmpty()
                    val fechaFin = btnFechaFinal.text?.toString().orEmpty()
                    val invitadosTxt = spInvitados.selectedItem?.toString().orEmpty()

                    // 1) Guardar la reserva
                    val reserva = Reservation(
                        area = areaSeleccionada,
                        fechaInicio = fechaIni,
                        fechaFin = fechaFin,
                        horario = slot,
                        invitados = invitadosTxt
                    )
                    ReservationStore.addReservation(this, reserva)

                    // 2) Aviso y navegar a la lista
                    Toast.makeText(this, "Reserva registrada correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ReservationListActivity::class.java))
                    // finish() // opcional
                }
                rvHorarios.adapter = horarioAdapter
            }
        }
    }

    private fun startTimer(timeInMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(ms: Long) {
                val m = (ms / 1000) / 60
                val s = (ms / 1000) % 60
                tvTimer.text = "Tiempo restante para reservar: %02d:%02d".format(m, s)
            }
            override fun onFinish() {
                tvTimer.text = "Tiempo agotado"
                logout() // cierra sesión automáticamente
            }
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

    private fun logout() {
        getSharedPreferences("auth_prefs", MODE_PRIVATE).edit().clear().apply()
        val i = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reason", "timeout")
        }
        startActivity(i)
        Toast.makeText(this, "Sesión cerrada por inactividad", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val logged = getSharedPreferences("auth_prefs", MODE_PRIVATE)
            .getBoolean("logged_in", false)
        if (!logged) {
            val i = Intent(this, LoginActivity::class.java)
                .putExtra("redirect_to", "reservar")
            startActivity(i)
            finish()
        }
    }

    // Lee el texto del área seleccionada en tu RadioGroup
    private fun getSelectedAreaText(): String {
        for (i in 0 until rgAreas.childCount) {
            val rb = rgAreas.getChildAt(i) as? RadioButton
            if (rb?.isChecked == true) return rb.text.toString()
        }
        return "Área no seleccionada"
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
