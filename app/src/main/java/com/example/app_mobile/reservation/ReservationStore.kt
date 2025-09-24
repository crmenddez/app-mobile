package com.example.app_mobile.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Reservation(
    val area: String,
    val fechaInicio: String,
    val fechaFin: String,
    val horario: String,
    val invitados: String,
    val createdAt: Long = System.currentTimeMillis()
)

object ReservationStore {
    private const val PREFS = "reservations_prefs"
    private const val KEY = "reservations_json"

    fun addReservation(ctx: Context, r: Reservation) {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray(prefs.getString(KEY, "[]"))
        arr.put(toJson(r))
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    fun getReservations(ctx: Context): List<Reservation> {
        val prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray(prefs.getString(KEY, "[]"))
        val list = mutableListOf<Reservation>()
        for (i in 0 until arr.length()) {
            list.add(fromJson(arr.getJSONObject(i)))
        }
        return list
    }

    fun clear(ctx: Context) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(KEY).apply()
    }

    private fun toJson(r: Reservation) = JSONObject().apply {
        put("area", r.area)
        put("fechaInicio", r.fechaInicio)
        put("fechaFin", r.fechaFin)
        put("horario", r.horario)
        put("invitados", r.invitados)
        put("createdAt", r.createdAt)
    }

    private fun fromJson(o: JSONObject) = Reservation(
        area = o.optString("area"),
        fechaInicio = o.optString("fechaInicio"),
        fechaFin = o.optString("fechaFin"),
        horario = o.optString("horario"),
        invitados = o.optString("invitados"),
        createdAt = o.optLong("createdAt")
    )
}
