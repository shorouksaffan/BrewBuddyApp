package com.example.brewbuddy.feature.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.brewbuddy.R
import com.example.brewbuddy.databinding.FragmentDrinkDetailsBinding

class DrinkDetailsFragment : Fragment() {

    private var _binding: FragmentDrinkDetailsBinding? = null
    private val binding get() = _binding!!

    private var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrinkDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Example: Load image with Glide
        Glide.with(this)
            .load(R.drawable.details) // Replace with URL if needed
            .into(binding.drinkImage)

        // Quantity buttons
        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }

        binding.btnIncrease.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }

        // Buy Now button
        binding.btnContinue.setOnClickListener {
            Toast.makeText(requireContext(), "Buying $quantity item(s)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.contentGroup.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
