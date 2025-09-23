package com.example.app_mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class HorarioAdapter(
    private val horarios: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder>() {

    class HorarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnHorario: Button = view.findViewById(R.id.btnHorarioItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.btnHorario.text = horario
        holder.btnHorario.setOnClickListener {
            onClick(horario)
        }
    }

    override fun getItemCount(): Int = horarios.size
}
