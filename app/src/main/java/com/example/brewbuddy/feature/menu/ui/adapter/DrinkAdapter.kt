package com.example.brewbuddy.feature.menu.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.util.PriceCatalog
import com.example.brewbuddy.databinding.ItemDrinkBinding

class DrinkAdapter(
    private val onClick: (Drink) -> Unit
) : ListAdapter<Drink, DrinkAdapter.DrinkViewHolder>(DrinkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val binding = ItemDrinkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DrinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DrinkViewHolder(private val binding: ItemDrinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: Drink) {
            binding.drinkName.text = drink.name
            binding.drinkPrice.text =
                PriceCatalog.getPriceForDrink(drink.id.toString()).toString()

            Glide.with(binding.root.context)
                .load(drink.imageUrl)
                .into(binding.drinkImage)

            binding.root.setOnClickListener { onClick(drink) }
        }
    }
}
