package com.mabdelhamid.kiwetask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mabdelhamid.kiwetask.data.models.Venue
import com.mabdelhamid.kiwetask.databinding.ItemVenueBinding
import com.mabdelhamid.kiwetask.utils.makeGone
import com.mabdelhamid.kiwetask.utils.makeVisible

class VenuesAdapter : RecyclerView.Adapter<VenuesAdapter.ViewHolder>() {

    var items: List<Venue> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemVenueBinding =
            ItemVenueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemVenueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(venue: Venue) {
            with(venue.name) {
                if (this.isNullOrBlank()) binding.tvName.makeGone()
                else {
                    binding.tvName.makeVisible()
                    binding.tvName.text = this
                }
            }

            with(venue.location?.address) {
                if (this.isNullOrBlank()) binding.tvAddress.makeGone()
                else {
                    binding.tvAddress.makeVisible()
                    binding.tvAddress.text = this
                }
            }

            venue.categories?.let {
                if (it.isNotEmpty()) {

                    Glide.with(binding.root.context)
                        .load("${it[0].icon!!.prefix}bg_32${it[0].icon!!.suffix}")
                        .into(binding.ivCategoryImage)

                    with(it[0].name) {
                        if (this.isNullOrBlank()) binding.tvCategoryName.makeGone()
                        else {
                            binding.tvCategoryName.makeVisible()
                            binding.tvCategoryName.text = this
                        }
                    }
                }
            }
        }
    }
}