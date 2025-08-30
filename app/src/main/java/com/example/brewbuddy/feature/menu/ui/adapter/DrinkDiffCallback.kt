package com.example.brewbuddy.feature.menu.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.brewbuddy.core.model.Drink

class DrinkDiffCallback : DiffUtil.ItemCallback<Drink>() {
    override fun areItemsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Drink,
        newItem: Drink
    ): Boolean = oldItem == newItem
}