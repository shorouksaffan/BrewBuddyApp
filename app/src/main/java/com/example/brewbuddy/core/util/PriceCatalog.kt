package com.example.brewbuddy.core.util

import com.example.brewbuddy.core.model.Money

object PriceCatalog {

    private val drinkPrices = mapOf(
        // Hot Drinks Base Prices
        "espresso" to 2.50,
        "americano" to 3.00,
        "cappuccino" to 3.50,
        "latte" to 4.00,
        "macchiato" to 3.75,
        "mocha" to 4.25,
        "flat white" to 3.80,
        "cortado" to 3.60,
        "gibraltar" to 3.60,
        "breve" to 4.10,
        "affogato" to 4.50,
        "con panna" to 3.25,
        "romano" to 2.75,
        "ristretto" to 2.60,
        "lungo" to 2.80,
        "doppio" to 3.20,
        "red eye" to 3.40,
        "black eye" to 3.60,
        "drip coffee" to 2.25,
        "french press" to 3.10,
        "pour over" to 3.30,
        "turkish coffee" to 3.80,
        "vienna coffee" to 4.20,
        "irish coffee" to 5.50,
        "cafe au lait" to 3.70,
        "cafe bombón" to 4.00,
        "cafe con leche" to 3.90,
        "cafe noisette" to 3.60,
        "cafe zorro" to 3.40,

        // Cold Drinks Base Prices
        "iced coffee" to 3.20,
        "iced americano" to 3.20,
        "iced latte" to 4.20,
        "iced cappuccino" to 3.70,
        "iced mocha" to 4.45,
        "iced macchiato" to 3.95,
        "cold brew" to 3.50,
        "nitro cold brew" to 4.00,
        "iced flat white" to 4.00,
        "iced cortado" to 3.80,
        "iced caramel macchiato" to 4.30,
        "iced vanilla latte" to 4.40,
        "iced hazelnut latte" to 4.40,
        "frappé" to 4.60,
        "iced espresso" to 2.70,
        "iced long shot" to 3.00,
        "iced red eye" to 3.60,
        "iced black eye" to 3.80,
        "vietnamese iced coffee" to 4.80,
        "mazagran" to 4.20,
        "shakerato" to 4.50,
        "iced gibraltar" to 3.80,
        "iced breve" to 4.30,
        "iced con panna" to 3.45,
        "affogato float" to 5.20,
        "coffee smoothie" to 5.00,
        "coffee milkshake" to 5.50,
        "iced turkish coffee" to 4.00,
        "cold drip coffee" to 3.80,
        "japanese iced coffee" to 3.60
    )

    fun getPriceForDrink(drinkName: String): Money {
        val cleanName = drinkName.lowercase().trim()

        // Try exact match first
        val exactPrice = drinkPrices[cleanName]
        if (exactPrice != null) {
            return Money(exactPrice)
        }

        // Try partial match
        val partialPrice = drinkPrices.entries.find { (key, _) ->
            cleanName.contains(key) || key.contains(cleanName)
        }?.value

        if (partialPrice != null) {
            return Money(partialPrice)
        }

        // Fallback: generate price based on drink name characteristics
        return Money(generateFallbackPrice(cleanName))
    }

    private fun generateFallbackPrice(drinkName: String): Double {
        val basePrice = when {
            drinkName.contains("espresso") -> 2.50
            drinkName.contains("americano") -> 3.00
            drinkName.contains("latte") -> 4.00
            drinkName.contains("cappuccino") -> 3.50
            drinkName.contains("mocha") -> 4.25
            drinkName.contains("macchiato") -> 3.75
            drinkName.contains("iced") -> 3.50
            drinkName.contains("cold") -> 3.80
            drinkName.contains("brew") -> 3.30
            drinkName.contains("frappé") || drinkName.contains("frappe") -> 4.60
            drinkName.contains("smoothie") -> 5.00
            drinkName.contains("shake") -> 5.50
            else -> 3.50 // Default price
        }

        // Add some variation based on name length and complexity
        val variation = (drinkName.length % 5) * 0.10
        val complexity = if (drinkName.split(" ").size > 2) 0.25 else 0.0

        return basePrice + variation + complexity
    }

    // Get a random selection of drinks for recommendations
    fun getRecommendedDrinks(count: Int = 6): List<String> {
        return drinkPrices.keys.shuffled().take(count)
    }

    // Get best seller (highest priced item as a simple heuristic)
    fun getBestSeller(): String {
        return drinkPrices.maxByOrNull { it.value }?.key ?: "latte"
    }
}