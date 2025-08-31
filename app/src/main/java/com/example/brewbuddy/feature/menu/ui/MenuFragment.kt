package com.example.brewbuddy.feature.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.brewbuddy.databinding.FragmentMenuBinding
import com.example.brewbuddy.feature.menu.MenuUiState
import com.example.brewbuddy.feature.menu.MenuViewModel
import com.example.brewbuddy.feature.menu.ui.adapter.DrinkAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()
    private lateinit var adapter: DrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupSearch()
        setupCategoryChips()
        observeUiState()

        viewModel.loadMenu()
    }

    private fun setupRecyclerView() {
        adapter = DrinkAdapter { drink ->
            val action = MenuFragmentDirections.actionMenuFragmentToDrinkDetailsFragment(drink.id)
            findNavController().navigate(action)
        }
        binding.rvMenu.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onQueryChanged(text.toString())
        }
    }

    private fun setupCategoryChips() {
        binding.chipHot.setOnClickListener {
            viewModel.filterByCategory(MenuViewModel.Category.HOT)
        }
        binding.chipIced.setOnClickListener {
            viewModel.filterByCategory(MenuViewModel.Category.COLD)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progress.isVisible = state is MenuUiState.Loading


                    when (state) {
                        is MenuUiState.Success -> {
                            adapter.submitList(state.drinks)
                        }

                        is MenuUiState.Error -> {
                            adapter.submitList(emptyList())
                        }

                        MenuUiState.Loading,
                        MenuUiState.Empty -> {
                            adapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
