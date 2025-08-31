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
import com.example.brewbuddy.R
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

    @Inject
    lateinit var imageLoader: ImageLoader

    private var quantity = 1
    private val drinkId: Int by lazy {
        arguments?.getInt("drinkId") ?: 0
    }

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
        viewModel.loadDrinkDetails(drinkId) // Pass the ID manually
    }

    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            viewModel.addToCart(quantity)
            // Navigate using manual bundle
            val bundle = Bundle().apply {
                putInt("drinkId", drinkId)
            }
            findNavController().navigate(R.id.cartFragment, bundle)
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
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is DrinkDetailsUiState.Loading -> showLoading(true)
                    is DrinkDetailsUiState.Success -> {
                        showLoading(false)
                        bindDrinkData(state.drink, state.isFavorite)
                    }
                    is DrinkDetailsUiState.Error -> {
                        showLoading(false)
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun bindDrinkData(drink: Drink, isFavorite: Boolean) {
        with(binding) {
            drinkName.text = drink.name
            drinkDescription.text = drink.description
            drinkPrice.text = "Rp ${drink.price.amount.toInt() * 1000}"

            imageLoader.loadImage(
                drinkImage,
                drink.imageUrl,
                R.drawable.placeholder_drink,
                R.drawable.error_drink
            )
        }
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
