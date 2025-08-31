package com.example.brewbuddy.feature.favorites.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brewbuddy.databinding.ItemFavoriteBinding
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.service.ImageLoader
import javax.inject.Inject

class FavoritesAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : ListAdapter<Drink, FavoritesAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    var onRemoveFavorite: ((Drink) -> Unit)? = null
    var onItemClick: ((Drink) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drink: Drink) {
            with(binding) {
                favName.text = drink.name
                favPrice.text = "Rp ${drink.price.amount.toInt() * 1000}"

                // Load drink image
                imageLoader.loadImage(
                    favImage,
                    drink.imageUrl,
                    com.example.brewbuddy.R.drawable.placeholder_drink,
                    com.example.brewbuddy.R.drawable.error_drink
                )

                // Set click listener for remove button
                removeFavorite.setOnClickListener {
                    onRemoveFavorite?.invoke(drink)
                }

                // Set click listener for entire item
                root.setOnClickListener {
                    onItemClick?.invoke(drink)
                }
            }
        }
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }
}