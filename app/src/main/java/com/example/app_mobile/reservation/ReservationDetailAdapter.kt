package com.example.app_mobile.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_mobile.databinding.ItemReservationDetailBinding

class ReservationDetailAdapter(
    private val onClick: (ReservationDetail) -> Unit
) : ListAdapter<ReservationDetail, ReservationDetailAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<ReservationDetail>() {
        override fun areItemsTheSame(a: ReservationDetail, b: ReservationDetail) = a.id == b.id
        override fun areContentsTheSame(a: ReservationDetail, b: ReservationDetail) = a == b
    }

    inner class VH(private val binding: ItemReservationDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReservationDetail) = with(binding) {
            tvTitle.text = item.title
            tvSubtitle.text = item.description
            tvTime.text = "${item.startTime} - ${item.endTime}"
            img.setImageResource(item.imageRes)
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemReservationDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
