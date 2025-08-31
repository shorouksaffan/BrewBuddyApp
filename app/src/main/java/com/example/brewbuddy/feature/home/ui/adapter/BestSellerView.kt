package com.example.brewbuddy.feature.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.databinding.ItemBestSellerBinding

class BestSellerViewHolder(
    private val binding: ItemBestSellerBinding,
    private val onItemClick: (Drink) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(drink: Drink) {
        binding.bestSellerName.text = drink.name
        binding.bestSellerDescription.text = drink.description
        binding.bestSellerPrice.text = "$${drink.price.amount}"

        Glide.with(binding.bestSellerImage.context)
            .load(drink.imageUrl)
            .into(binding.bestSellerImage)

        binding.root.setOnClickListener { onItemClick(drink) }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (Drink) -> Unit
        ): BestSellerViewHolder {
            val binding = ItemBestSellerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return BestSellerViewHolder(binding, onItemClick)
        }
    }
}