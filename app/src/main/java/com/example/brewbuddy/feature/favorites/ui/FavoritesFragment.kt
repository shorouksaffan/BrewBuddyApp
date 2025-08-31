package com.example.brewbuddy.feature.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.brewbuddy.R
import com.example.brewbuddy.databinding.FragmentFavoritesBinding
import com.example.brewbuddy.feature.favorites.FavoritesViewModel
import com.example.brewbuddy.feature.favorites.ui.adapter.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

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

        adapter = FavoritesAdapter(
            mutableListOf(),
            onRemoveClick = { drink ->
                viewModel.removeFromFavorites(drink.id)
                Toast.makeText(requireContext(), "${drink.name} removed", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { drink ->
                val bundle = Bundle().apply { putInt("drinkId", drink.id) }
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_drinkDetailsFragment,
                    bundle
                )
            }
        )


        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorites.adapter = adapter

        collectFavorites()
    }

    private fun collectFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collectLatest { drinks ->
                if (drinks.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                }
                adapter.updateData(drinks)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
