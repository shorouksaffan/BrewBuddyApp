package com.example.brewbuddy.core.data.repository

import com.example.brewbuddy.core.data.local.entity.DrinkCacheEntity
import com.example.brewbuddy.core.data.local.entity.OrderEntity
import com.example.brewbuddy.core.data.local.entity.OrderItemEntity
import com.example.brewbuddy.core.model.Drink
import com.example.brewbuddy.core.model.Money
import com.example.brewbuddy.core.model.Order
import com.example.brewbuddy.core.model.OrderItem
import com.example.brewbuddy.core.util.PriceCatalog

fun DrinkCacheEntity.toDrink(): Drink {
    return Drink(
        id = id,
        name = title,
        description = description,
        ingredients = ingredients,
        imageUrl = imageUrl,
        price = PriceCatalog.getPriceForDrink(title),
        isHot = isHot
    )
}

fun OrderEntity.toOrder(items: List<OrderItemEntity>): Order {
    return Order(
        orderId = orderId,
        items = items.map { it.toOrderItem() },
        totalAmount = Money(totalAmount),
        placedAt = placedAt
    )
}

fun OrderItemEntity.toOrderItem(): OrderItem {
    return OrderItem(
        drinkId = drinkId,
        drinkName = drinkName,
        drinkImage = drinkImage,
        price = Money(price),
        quantity = quantity
    )
}

fun OrderItem.toOrderItemEntity(orderId: String): OrderItemEntity {
    return OrderItemEntity(
        orderId = orderId,
        drinkId = drinkId,
        drinkName = drinkName,
        drinkImage = drinkImage,
        price = price.amount,
        quantity = quantity
    )
}