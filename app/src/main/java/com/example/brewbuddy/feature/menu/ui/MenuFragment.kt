package com.example.brewbuddy.feature.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewbuddy.R
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.databinding.FragmentMenuBinding
import com.example.brewbuddy.feature.menu.Category
import com.example.brewbuddy.feature.menu.MenuEvent
import com.example.brewbuddy.feature.menu.MenuUiState
import com.example.brewbuddy.feature.menu.MenuViewModel
import com.example.brewbuddy.feature.menu.ui.adapter.DrinkAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()
    private lateinit var drinkAdapter: DrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        setupCategoryButtons()
        observeUiState()
        observeEvents()
    }

    private fun setupRecyclerView() {
        drinkAdapter = DrinkAdapter { drink ->
            navigateToDetails(drink)
        }

        binding.rvMenu.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = drinkAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchBar() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.search(text?.toString() ?: "")
        }

        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.search("")
        }
    }

    private fun setupCategoryButtons() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipHot -> viewModel.filterByCategory(Category.HOT)
                R.id.chipIced -> viewModel.filterByCategory(Category.COLD)
                else -> viewModel.filterByCategory(Category.ALL)
            }
        }

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is MenuUiState.Loading -> showLoading()
                    is MenuUiState.Error -> showError(state.message)
                    is MenuUiState.Empty -> showEmpty()
                    is MenuUiState.Success -> showSuccess(state.drinks)
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progress.isVisible = true
            rvMenu.isVisible = false
            tvError.isVisible = false
            tvEmptyState.isVisible = false
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progress.isVisible = false
            rvMenu.isVisible = false
            tvError.isVisible = true
            tvEmptyState.isVisible = false
            tvError.text = message
        }
    }

    private fun showEmpty() {
        binding.apply {
            progress.isVisible = false
            rvMenu.isVisible = false
            tvError.isVisible = false
            tvEmptyState.isVisible = true
            tvEmptyState.text = "No drinks found"
        }
    }

    private fun showSuccess(drinks: List<Drink>) {
        binding.apply {
            progress.isVisible = false
            rvMenu.isVisible = true
            tvError.isVisible = false
            tvEmptyState.isVisible = false
            drinkAdapter.submitList(drinks)
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is MenuEvent.ShowSnackBar -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                    is MenuEvent.NavigateToDetails -> {
                        navigateToDetailsById(event.drinkId)
                    }
                }
            }
        }
    }

    private fun navigateToDetails(drink: Drink) {
        val action = MenuFragmentDirections.actionMenuFragmentToDrinkDetailsFragment(drink.id)
        findNavController().navigate(action)
    }

    private fun navigateToDetailsById(drinkId: Int) {
        val action = MenuFragmentDirections.actionMenuFragmentToDrinkDetailsFragment(drinkId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
