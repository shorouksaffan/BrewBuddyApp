package com.example.brewbuddy.feature.favorites.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private val items: MutableList<Drink>,
    private val onRemoveClick: (Drink) -> Unit,
    private val onItemClick: (Drink) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drink: Drink) {
            binding.favName.text = drink.name
            binding.favPrice.text = "$${drink.price}"

            Glide.with(binding.favImage.context)
                .load(drink.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.favImage)

            binding.removeFavorite.setOnClickListener {
                onRemoveClick(drink)
            }

            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("drinkId", drink.id)
                }

                onItemClick(drink)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Drink>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
