package com.example.app_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mobile.databinding.ItemReservationBinding

class ReservationAdapter :
    ListAdapter<Reservation, ReservationAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(a: Reservation, b: Reservation) =
            a.title == b.title && a.start == b.start && a.end == b.end
        override fun areContentsTheSame(a: Reservation, b: Reservation) = a == b
    }

    inner class VH(private val binding: ItemReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Reservation) = with(binding) {
            tvTitle.text = item.title
            tvSubtitle.text = item.description
            tvTime.text = "${item.start} - ${item.end}"
            img.setImageResource(item.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
