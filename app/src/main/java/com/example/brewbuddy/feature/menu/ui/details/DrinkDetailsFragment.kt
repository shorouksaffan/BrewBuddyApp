package com.example.brewbuddy.feature.menu.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.brewbuddy.databinding.FragmentDrinkDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DrinkDetailsFragment : Fragment() {
    private var _binding: FragmentDrinkDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DrinkDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrinkDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.favoriteToggle.setOnClickListener {
//            viewModel.toogleFavorite()
//        }

        observeUiState()

        // Tell the ViewModel to load the data now that the view is ready.
        viewModel.loadDrinkDetails()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
//                binding.progress.visibility = state is DrinkDetailsUiState.Loading
//                binding.content.visibility = state is DrinkDetailsUiState.Success
//                binding.error.visibility = state is DrinkDetailsUiState.Error

                when (state) {
                    is DrinkDetailsUiState.Success -> {
                        val drink = state.drink
//                        binding.drinkName.text = drink.name
//                        binding.drinkDescription.text = drink.description
//                        binding.drinkIngredients.text = "Ingredients: ${drink.ingredientsText}"
//                        binding.drinkPrice.text = drink.price.formatted()
//                        binding.favoriteToggle.isChecked = state.isFavorite
//
//                        Glide.with(requireContext())
//                            .load(drink.imageUrl)
//                            .into(binding.drinkImage)
                    }

                    is DrinkDetailsUiState.Error -> {
//                        binding.tvError.text = state.message
                    }

                    DrinkDetailsUiState.Loading -> {
                        // Handled by visibility toggle
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}