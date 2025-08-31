package com.example.brewbuddy.feature.orders.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewbuddy.databinding.FragmentOrdersBinding
import com.example.brewbuddy.feature.orders.OrdersViewModel
import com.example.brewbuddy.feature.orders.ui.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToggleButtons()
        observeOrders()
    }

    private fun setupRecyclerView() {
        adapter = OrderAdapter(
            onOrderClick = { order ->
                // Navigate to order details if needed
            },
            onDeleteClick = { order ->
                viewModel.deleteOrder(order.orderId)
            }
        )

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@OrdersFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupToggleButtons() {
        binding.toggle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.chipRecently.id -> viewModel.filterOrders(recentOnly = true)
                binding.chipPastOrders.id -> viewModel.filterOrders(recentOnly = false)
            }
        }
    }

    private fun observeOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.filteredOrders)

                binding.tvEmpty.visibility =
                    if (state.filteredOrders.isEmpty()) View.VISIBLE else View.GONE

                // Show/hide loading progress bar
                binding.progressBar.visibility =
                    if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
