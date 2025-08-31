package com.example.brewbuddy.feature.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.brewbuddy.databinding.FragmentFavoritesBinding
import com.example.brewbuddy.feature.favorites.ui.adapter.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns grid
        binding.rvFavorites.adapter = favoritesAdapter

        // Set click listener for removing favorites
        favoritesAdapter.onRemoveFavorite = { drink ->
            viewModel.removeFromFavorites(drink.id)
        }

        // Set click listener for item click (navigate to details)
        favoritesAdapter.onItemClick = { drink ->
            // Navigate to drink details
            val action = FavoritesFragmentDirections.actionFavoritesFragmentToDrinkDetailsFragment(drink.id)
            findNavController().navigate(action)
        }
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collectLatest { favorites ->
                if (favorites.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvFavorites.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvFavorites.visibility = View.VISIBLE
                    favoritesAdapter.submitList(favorites)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}