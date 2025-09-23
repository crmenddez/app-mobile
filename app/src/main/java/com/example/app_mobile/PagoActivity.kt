package com.example.app_mobile

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PagoActivity : AppCompatActivity() {

    private lateinit var rgMetodosPago: RadioGroup
    private lateinit var tilNumeroTarjeta: TextInputLayout
    private lateinit var btnConfirmarPago: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        // Empuja TODO el contenido (incluida la Toolbar) por debajo de la barra de estado
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, sb.top, v.paddingRight, v.paddingBottom)
            insets
        }

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }


        // Toolbar (si existe en tu XML)
        findViewById<MaterialToolbar?>(R.id.toolbar)?.apply {
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        rgMetodosPago = findViewById(R.id.rgMetodosPago)
        tilNumeroTarjeta = findViewById(R.id.tilNumeroTarjeta)
        btnConfirmarPago = findViewById(R.id.btnConfirmarPago)

        // Datos recibidos
        val fechaIni = intent.getStringExtra("FECHA_INICIO").orEmpty()
        val fechaFin = intent.getStringExtra("FECHA_FIN").orEmpty()
        val invitados = intent.getStringExtra("INVITADOS").orEmpty()
        val horario = intent.getStringExtra("HORARIO").orEmpty()

        // Resumen si existe TextView en el layout
        findViewById<TextView?>(R.id.tvResumen)?.text =
            "Del $fechaIni al $fechaFin\n$horario · $invitados"

        // Mostrar/ocultar campo tarjeta según método
        rgMetodosPago.setOnCheckedChangeListener { _, checkedId ->
            tilNumeroTarjeta.visibility = if (checkedId == R.id.rbTarjeta) View.VISIBLE else View.GONE
            tilNumeroTarjeta.error = null
            updateConfirmEnabled()
        }

        // Formateo #### #### #### #### y validación simple
        tilNumeroTarjeta.editText?.addTextChangedListener(object : TextWatcher {
            private var editing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editing) return
                editing = true
                val digits = onlyDigits(s?.toString().orEmpty())
                val grouped = digits.chunked(4).joinToString(" ").take(19)
                if (grouped != tilNumeroTarjeta.editText?.text?.toString()) {
                    tilNumeroTarjeta.editText?.setText(grouped)
                    tilNumeroTarjeta.editText?.setSelection(grouped.length)
                }
                // Marca error si no está vacío y aún no llega a 16
                tilNumeroTarjeta.error = when {
                    digits.isEmpty() -> null
                    digits.length < 16 -> "Ingresa 16 dígitos"
                    else -> null
                }
                editing = false
                updateConfirmEnabled()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Estado inicial del campo tarjeta según lo seleccionado por defecto
        tilNumeroTarjeta.visibility =
            if (rgMetodosPago.checkedRadioButtonId == R.id.rbTarjeta) View.VISIBLE else View.GONE

        // Botón confirmar
        btnConfirmarPago.setOnClickListener {
            when (rgMetodosPago.checkedRadioButtonId) {
                R.id.rbTarjeta -> {
                    val digits = onlyDigits(tilNumeroTarjeta.editText?.text?.toString().orEmpty())
                    if (digits.length != 16) {
                        tilNumeroTarjeta.error = "Ingresa 16 dígitos"
                        return@setOnClickListener
                    }
                    if (!isValidLuhn(digits)) {
                        tilNumeroTarjeta.error = "Tarjeta inválida"
                        return@setOnClickListener
                    }
                    tilNumeroTarjeta.error = null
                    Toast.makeText(this, "Pago con tarjeta procesado", Toast.LENGTH_SHORT).show()
                    // TODO: integrar pasarela aquí
                }
                R.id.rbYape -> {
                    Toast.makeText(this, "Pago con Yape/Plin procesado", Toast.LENGTH_SHORT).show()
                }
                R.id.rbEfectivo -> {
                    Toast.makeText(this, "Reserva registrada, paga en el club", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Selecciona un método de pago", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Deshabilita el botón hasta que la selección sea válida
        updateConfirmEnabled()
    }

    private fun updateConfirmEnabled() {
        btnConfirmarPago.isEnabled = when (rgMetodosPago.checkedRadioButtonId) {
            R.id.rbTarjeta -> onlyDigits(tilNumeroTarjeta.editText?.text?.toString().orEmpty()).length == 16
            R.id.rbYape, R.id.rbEfectivo -> true
            else -> false
        }
    }

    private fun onlyDigits(s: String): String = s.filter { it.isDigit() }

    // Validación Luhn básica
    private fun isValidLuhn(number: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in number.length - 1 downTo 0) {
            var n = number[i] - '0'
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
}
