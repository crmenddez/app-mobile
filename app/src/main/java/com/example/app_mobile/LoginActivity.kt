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
    private lateinit var etPassword: EditText    // id: txtPasword
    private lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



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

        // Enter en password → intentar login
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { btnIngresar.performClick(); true } else false
        }

        btnIngresar.setOnClickListener {
            val cuenta = etCuenta.text.toString().trim()
            val pass   = etPassword.text.toString()

            if (!validarCampos(cuenta, pass)) return@setOnClickListener

            if (!authenticate(cuenta, pass)) {
                // Error tipo Material (borde y texto rojo)
                tilPassword.error = "Usuario o contraseña incorrectos"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            onLoginSuccess(cuenta)
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        if (prefs.getBoolean(KEY_LOGGED_IN, false)) {
            startActivity(Intent(this, ReservarActivity::class.java))
            finish()
        }
    }

    // ---------- Helpers ----------

    private fun validarCampos(cuenta: String, pass: String): Boolean {
        val esEmail  = android.util.Patterns.EMAIL_ADDRESS.matcher(cuenta).matches()
        val esCodigo = cuenta.length >= 4 && cuenta.all { it.isLetterOrDigit() }

        if (cuenta.isEmpty()) {
            tilCuenta.error = "Ingresa tu código o correo"
            etCuenta.requestFocus(); return false
        }
        if (!esEmail && !esCodigo) {
            tilCuenta.error = "Correo inválido o código (mín. 4, letras/números)"
            etCuenta.requestFocus(); return false
        }
        if (pass.isEmpty()) {
            tilPassword.error = "Ingresa tu contraseña"
            etPassword.requestFocus(); return false
        }
        if (pass.length < 6) {
            tilPassword.error = "Mínimo 6 caracteres"
            etPassword.requestFocus(); return false
        }
        return true
    }

    private fun authenticate(cuenta: String, pass: String): Boolean {
        val input = cuenta.trim()
        return TEST_USERS.any { it.user.equals(input, ignoreCase = true) && it.pass == pass }
    }

    private fun onLoginSuccess(user: String) {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_USER, user)
            .apply()

        val redirect = intent.getStringExtra("redirect_to")
        val dest = if (redirect == "reservar") ReservarActivity::class.java
        else MainActivity::class.java
        Toast.makeText(this, "¡Bienvenido, $user!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ReservarActivity::class.java))
        finish()
    }

    // ---------- Mock data & prefs ----------

    data class TestUser(val user: String, val pass: String)

    companion object {
        private val TEST_USERS = listOf(
            TestUser("3320", "654321"),
            TestUser("0001", "123456")
        )
        private const val PREFS = "auth_prefs"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USER = "user"
    }
}
