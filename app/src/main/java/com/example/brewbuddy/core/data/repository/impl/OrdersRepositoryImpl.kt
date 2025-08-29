package com.example.brewbuddy.core.data.repository.impl

import com.example.brewbuddy.core.data.local.dao.OrderDao
import com.example.brewbuddy.core.data.local.entity.OrderEntity
import com.example.brewbuddy.core.data.repository.OrdersRepository
import com.example.brewbuddy.core.data.repository.toOrder
import com.example.brewbuddy.core.data.repository.toOrderItem
import com.example.brewbuddy.core.data.repository.toOrderItemEntity
import com.example.brewbuddy.core.model.Order
import com.example.brewbuddy.core.model.OrderItem
import com.example.brewbuddy.core.util.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class OrdersRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val dispatchersProvider: DispatchersProvider
) : OrdersRepository {

    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders()
            .map { orderEntities ->
                orderEntities.map { orderEntity ->
                    val items = orderDao.getOrderItems(orderEntity.orderId)
                    orderEntity.toOrder(items)
                }
            }
            .flowOn(dispatchersProvider.io)
    }

    override suspend fun placeOrder(items: List<OrderItem>): String {
        return withContext(dispatchersProvider.io) {
            val orderId = UUID.randomUUID().toString()
            val totalAmount = items.sumOf { it.price.amount * it.quantity }

            val orderEntity = OrderEntity(
                orderId = orderId,
                placedAt = System.currentTimeMillis(),
                totalAmount = totalAmount
            )

            val orderItemEntities = items.map { it.toOrderItemEntity(orderId) }

            orderDao.insertOrderWithItems(orderEntity, orderItemEntities)
            orderId
        }
    }

    override suspend fun deleteOrder(orderId: String) {
        withContext(dispatchersProvider.io) {
            orderDao.deleteOrder(orderId)
        }
    }
}