package com.example.brewbuddy.feature.orders.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brewbuddy.core.model.Order
import com.example.brewbuddy.core.util.DateFormatters
import com.example.brewbuddy.databinding.ItemOrderBinding

class OrderAdapter(
    private val onOrderClick: (Order) -> Unit,
    private val onDeleteClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orders: List<Order> = emptyList()

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            // Match your item_order.xml IDs
            binding.orderId.text = "Order #${order.orderId.takeLast(6)}"
            binding.orderDate.text = DateFormatters.formatOrderDate(order.placedAt)
            binding.orderTotal.text = order.totalAmount.toString()
            binding.itemCount.text = "${order.itemCount} items"

            binding.root.setOnClickListener { onOrderClick(order) }
            binding.deleteButton.setOnClickListener { onDeleteClick(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun submitList(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}