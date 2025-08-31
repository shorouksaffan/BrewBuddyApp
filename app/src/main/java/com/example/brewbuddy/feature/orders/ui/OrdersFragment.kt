package com.example.brewbuddy.feature.orders.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewbuddy.databinding.FragmentOrdersBinding
import com.example.brewbuddy.feature.orders.OrdersViewModel
import com.example.brewbuddy.feature.orders.ui.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint

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
                // Navigate to order details (if needed)
            },
            onDeleteClick = { order ->
                viewModel.deleteOrder(order.orderId)
            }
        )

        // Match your layout ID: rvOrders instead of ordersRecyclerView
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
    }

    private fun setupToggleButtons() {
        // Handle toggle selection for Recently/Past Orders
        binding.toggle.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.chipRecently.id -> {
                    // Show recent orders (last 30 days)
                    viewModel.filterOrders(recentOnly = true)
                }
                binding.chipPastOrders.id -> {
                    // Show all past orders
                    viewModel.filterOrders(recentOnly = false)
                }
            }
        }
    }

    private fun observeOrders() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.submitList(orders)
            // Match your layout ID: tvEmpty instead of emptyState
            binding.tvEmpty.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // If you have a progress bar in your layout, add it here
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}