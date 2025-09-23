package com.example.app_mobile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var tilCuenta: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etCuenta: EditText      // id: txtCuenta
    private lateinit var etPassword: EditText    // id: txtPasword (tal cual)
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, sb.top, sb.right, sb.bottom)
            insets
        }

        // TextInputLayouts (envoltorios) y EditTexts
        tilCuenta   = findViewById(R.id.tilCuenta)
        tilPassword = findViewById(R.id.tilPassword)
        etCuenta    = findViewById(R.id.txtCuenta)
        etPassword  = findViewById(R.id.txtPasword)
        btnIngresar = findViewById(R.id.btnIngresar)

        // Limpia errores al escribir
        val clearError = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilCuenta.error = null
                tilPassword.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        etCuenta.addTextChangedListener(clearError)
        etPassword.addTextChangedListener(clearError)

        // "Enter" en el password ejecuta ingresar
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { btnIngresar.performClick(); true } else false
        }

        btnIngresar.setOnClickListener {
            if (validarCampos()) {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ReservarActivity::class.java))
                // finish() // opcional
            }
        }
    }

    private fun validarCampos(): Boolean {
        val cuenta = etCuenta.text.toString().trim()
        val pass   = etPassword.text.toString()

        // Cuenta: email válido o código alfanumérico ≥ 4
        if (cuenta.isEmpty()) {
            tilCuenta.error = "Ingresa tu código o correo"
            etCuenta.requestFocus(); return false
        }
        val esEmail  = android.util.Patterns.EMAIL_ADDRESS.matcher(cuenta).matches()
        val esCodigo = cuenta.length >= 4 && cuenta.all { it.isLetterOrDigit() }
        if (!esEmail && !esCodigo) {
            tilCuenta.error = "Correo inválido o código (mín. 4, letras/números)"
            etCuenta.requestFocus(); return false
        }

        // Password: mínimo 6
        if (pass.isEmpty()) {
            tilPassword.error = "Ingresa tu contraseña"
            etPassword.requestFocus(); return false
        }
        if (pass.length < 6) {
            tilPassword.error = "Mínimo 6 caracteres"
            etPassword.requestFocus(); return false
        }

        // Sin errores
        tilCuenta.error = null
        tilPassword.error = null
        return true
    }
}
