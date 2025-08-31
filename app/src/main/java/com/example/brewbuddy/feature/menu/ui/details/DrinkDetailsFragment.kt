package com.example.brewbuddy.feature.menu.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.databinding.FragmentDrinkDetailsBinding
import com.example.brewbuddy.service.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DrinkDetailsFragment : Fragment() {

    private var _binding: FragmentDrinkDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DrinkDetailsViewModel by viewModels()
    private val args: DrinkDetailsFragmentArgs by navArgs() // Use safe args

    @Inject
    lateinit var imageLoader: ImageLoader

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
        setupClickListeners()
        observeUiState()
        observeFavoriteState()
        // No need to manually call loadDrinkDetails - ViewModel init does it
    }

    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.addToCart(quantity)
            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp() // Go back instead of navigating to orders
        }

        binding.btnIncrease.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }

        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }

        // ADD FAVORITE BUTTON CLICK LISTENER
        binding.ivFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is DrinkDetailsUiState.Loading -> showLoading(true)
                    is DrinkDetailsUiState.Success -> {
                        showLoading(false)
                        bindDrinkData(state.drink)
                    }
                    is DrinkDetailsUiState.Error -> {
                        showLoading(false)
                        showError(state.message)
                    }
                }
            }
        }
    }

    // ADD THIS METHOD TO OBSERVE FAVORITE STATE
    private fun observeFavoriteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collectLatest { isFavorite ->
                updateFavoriteIcon(isFavorite)
            }
        }
    }

    private fun bindDrinkData(drink: Drink) {
        with(binding) {
            drinkName.text = drink.name
            drinkDescription.text = drink.description
            drinkPrice.text = "Rp ${drink.price.amount.toInt() * 1000}"

            imageLoader.loadImage(
                drinkImage,
                drink.imageUrl,
                com.example.brewbuddy.R.drawable.placeholder_drink,
                com.example.brewbuddy.R.drawable.error_drink
            )
        }
    }

    // ADD THIS METHOD TO UPDATE FAVORITE ICON
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) {
            com.example.brewbuddy.R.drawable.ic_favorite_filled
        } else {
            com.example.brewbuddy.R.drawable.ic_favorite_outline
        }
        binding.ivFavorite.setImageResource(iconRes)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.contentGroup.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}