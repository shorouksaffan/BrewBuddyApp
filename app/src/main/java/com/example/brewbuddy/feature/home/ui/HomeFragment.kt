package com.example.brewbuddy.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.brewbuddy.R
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.databinding.FragmentHomeBinding
import com.example.brewbuddy.feature.home.HomeViewModel
import com.example.brewbuddy.feature.home.ui.adapter.RecommendationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RecommendationAdapter

    // Hold current best seller
    private var currentBestSeller: Drink? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectUiState()
        setupClicks()
    }

    private fun setupRecyclerView() {
        adapter = RecommendationAdapter(mutableListOf()) { drink ->
            val bundle = Bundle().apply { putInt("drinkId", drink.id) }
            findNavController().navigate(
                R.id.action_homeFragment_to_drinkDetailsFragment,
                bundle
            )
        }

        binding.rvRecommendations.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = this@HomeFragment.adapter
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                if (state.isLoading) return@collectLatest

                state.bestSeller?.let { drink ->
                    currentBestSeller = drink
                    binding.tvCoffeeTitle.text = drink.name

                    // Load Best Seller image
                    Glide.with(binding.ivBestImage.context)
                        .load(drink.imageUrl)
                        .placeholder(R.drawable.img_recommended) // placeholder if loading fails
                        .error(R.drawable.img_recommended)      // fallback if URL invalid
                        .into(binding.ivBestImage)

                    // Set More Info click dynamically
                    binding.tvMoreInfo.setOnClickListener {
                        val bundle = Bundle().apply { putInt("drinkId", drink.id) }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_drinkDetailsFragment,
                            bundle
                        )
                    }
                }

                // Update recommendations
                adapter.updateData(state.recommendations)

                // Show error
                state.error?.let {
                    Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClicks() {
        // Navigate to drink details when card is clicked
        binding.cvBestSeller.setOnClickListener {
            currentBestSeller?.let { drink ->
                val bundle = Bundle().apply { putInt("drinkId", drink.id) }
                findNavController().navigate(
                    R.id.action_homeFragment_to_drinkDetailsFragment,
                    bundle
                )
            } ?: run {
                Toast.makeText(requireContext(), "Best seller not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cvNewMenu.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
