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

        // Static demo clicks (optional)
        binding.cvBestSeller.setOnClickListener {
            Toast.makeText(requireContext(), "Best seller clicked", Toast.LENGTH_SHORT).show()
        }
        binding.cvNewMenu.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
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
                if (state.isLoading) {
                    // TODO: show loading indicator
                    return@collectLatest
                }

                state.bestSeller?.let { drink ->
                    binding.tvCoffeeTitle.text = drink.name
                    binding.tvMoreInfo.setOnClickListener {
                        val bundle = Bundle().apply { putInt("drinkId", drink.id) }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_drinkDetailsFragment,
                            bundle
                        )
                    }
                }

                adapter.updateData(state.recommendations)

                state.error?.let {
                    Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
