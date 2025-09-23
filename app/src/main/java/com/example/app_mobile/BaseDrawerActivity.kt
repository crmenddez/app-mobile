package com.example.app_mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_mobile.events.EventSuggestionActivity
import com.example.app_mobile.reservation.ReservationListActivity

open class BaseDrawerActivity : AppCompatActivity() {
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
                    val intent = Intent(this, ReservationListActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_reservar -> {
                     }

                R.id.nav_eventos -> {
                    val intent = Intent(this, EventSuggestionActivity::class.java)
                    startActivity(intent) }

                R.id.nav_reportes -> {
                    val intent = Intent(this, ReportesActivity::class.java)
                    startActivity(intent) }

                R.id.nav_cerrar_sesion -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent) }
            }
            drawerLayout.close()
            true
        }

    }
}