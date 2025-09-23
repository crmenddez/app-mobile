package com.example.app_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_mobile.events.EventSuggestionActivity
import com.example.app_mobile.reservation.ReservationListActivity

open class BaseDrawerActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun setContentView(layoutResID: Int) {
        // Inflar el layout base que contiene el Drawer
        val fullView = LayoutInflater.from(this)
            .inflate(R.layout.activity_base_drawer, null)
        val frame = fullView.findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, frame, true)
        super.setContentView(fullView)

        // Configurar Toolbar y Drawer
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.navigation_view)

        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }


        navView.setNavigationItemSelectedListener { item ->
            // Aquí manejas los clicks del menú
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_misReservas -> {
                    val intent = Intent(this, ReservationListActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_reservar -> {
                    if (isLoggedIn()) {
                        startActivity(Intent(this, ReservarActivity::class.java))
                    } else {
                        // Pide login y recuerda a dónde querías ir
                        val i = Intent(this, LoginActivity::class.java)
                            .putExtra("redirect_to", "reservar")
                        startActivity(i)
                        Toast.makeText(this, "Inicia sesión para reservar", Toast.LENGTH_SHORT).show()
                    }
                    drawerLayout.closeDrawers()
                     }

                R.id.nav_eventos -> {
                    val intent = Intent(this, EventSuggestionActivity::class.java)
                    startActivity(intent) }

                R.id.nav_reportes -> {
                    val intent = Intent(this, ReportesActivity::class.java)
                    startActivity(intent) }

                R.id.nav_cerrar_sesion -> {
                    logout()   // ← cerrar sesión desde el menú
                    return@setNavigationItemSelectedListener true }
            }
            drawerLayout.close()
            true
        }

    }

    private fun isLoggedIn(): Boolean =
        getSharedPreferences("auth_prefs", MODE_PRIVATE).getBoolean("logged_in", false)

    private fun logout() {
        // Limpia la sesión
        getSharedPreferences("auth_prefs", MODE_PRIVATE).edit().clear().apply()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        // Vuelve al Login y limpia el back stack
        val i = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("reason", "menu_logout")
        }
        startActivity(i)
        finish()
    }
}