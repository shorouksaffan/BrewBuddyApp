# BrewBuddy 
is a mobile application that simulates a modern coffee shop ordering experience. It integrates online data (fetched dynamically from the [Fake Coffee API]) with local storage features to create a hybrid app that works both online and offline.
---
## Branch Strategy

We are following a **Git branching strategy** to manage teamwork:

- **main** → Final stable code  
- **dev** → Shared development branch (where features are merged)  
- **feature/*** → Each team member’s task branch  

---
# Project Structure – BrewBuddy Core 

## core/data/local  
Handles **Room Database** (local storage).  

- **dao/**  
  - `DrinkCacheDao.kt` → Access cached drinks in DB.  
  - `FavoriteDao.kt` → Access favorites in DB.  
  - `OrderDao.kt` → Access orders in DB.  

- **entity/**  
  - `DrinkCacheEntity.kt` → Entity for cached drinks.  
  - `FavoriteEntity.kt` → Entity for saved favorites.  
  - `OrderEntity.kt` → Entity for an order (ID, date, etc.).  
  - `OrderItemEntity.kt` → Entity for items inside an order.  

- `brewbuddyDatabase.kt` → Room database setup (connects DAOs + entities).  
- `LocalTypeConverters.kt` → Converts custom types (lists, objects) for Room.  

---

## core/data/remote  
Handles **API calls** (online data).  

- **dto/**  
  - `CoffeeDto.kt` → Data model for coffee items from API.  
  - `DtoMappers.kt` → Maps API DTOs → app models (`Drink`).  

- `CoffeeApiService.kt` → Retrofit API endpoints (hot/iced coffee).  
- `NetworkModule.kt` → Provides Retrofit instance + API service.  

---

## core/repository  
The **bridge** between Data (API/DB) and UI.  

- **impl/**  
  - `DrinkRepositoryImpl.kt` → Drinks logic (API + cache).  
  - `FavoritesRepositoryImpl.kt` → Favorites logic (local DB).  
  - `OrdersRepositoryImpl.kt` → Orders logic (local DB).  

- `Result.kt` → Wrapper for success/error results.  
- `DrinkRepository.kt` → Contract for drinks data.  
- `FavoritesRepository.kt` → Contract for favorites data.  
- `OrdersRepository.kt` → Contract for orders data.  
- `RepoMappers.kt` → Maps Entities ↔ Models (DB ↔ domain).  

---

## di (Dependency Injection)  
- `AppModule.kt` → Provides app-wide dependencies (Room, Retrofit, etc.).  
- `RepositoryModule.kt` → Provides repositories (DrinkRepo, FavoritesRepo, OrdersRepo).  

---

## model (Domain Models)  
- `Drink.kt` → Domain model for a drink.  
- `Money.kt` → Handles prices/currency.  
- `Order.kt` → Domain model for an order.  
- `OrderItem.kt` → Domain model for an item inside an order.  

---

## prefs (User Preferences)  
- `UserPrefs.kt` → Saves and retrieves user name (SharedPreferences).  

---

## util (Helpers & Utilities)  
- `DateFormatters.kt` → Formats dates/times for orders.  
- `DispatchersProvider.kt` → Provides coroutine dispatchers (IO/Main).  
- `Extensions.kt` → Kotlin extension functions (helper methods).  
- `PriceCatalog.kt` → Static price list for drinks (since API has no price).  
---

## Tasks Division
---
## Project Coordination

- Repository setup, README, branch strategy (`dev` / `feature/*` / `master`), and task distribution were handled by **Shorouk Saffan**.
---
### Task 1: Core -> eslam
- `core/`

---

### Task 2: Base Layouts by ***Emad Rabie***
- `activity_main.xml`  
- `fragment_home.xml`  
- `fragment_menu.xml`  
- `fragment_orders.xml`  
- `fragment_favorites.xml`
- `item_best_seller.xml`

---

### Task 3: RecyclerView Items by ***Mohamed Ashraf***
- `item_coffee.xml`  
- `item_drink.xml`  
- `item_favorite.xml`  
- `item_order.xml`  
- `item_recommendation.xml`  

---

### Task 4: Extra Layouts->Mariam Mohamed
- `dialog_user_name.xml`  
- `fragment_drink_details.xml`  
- `fragment_item_detail.xml`  
- `layout_empty_state.xml`  
- `layout_progress.xml`  

---

### Task 5: Navigation + Main "Ahmed Hashem"
- `menu/bottom_nav_menu.xml`  
- `navigation/nav_graph.xml`  
- `MainActivity.kt`  

---

### Bahy Mohy - Task 6: Features – Dialog + Menu
- `feature/dialog/`  
- `feature/menu/`  

---

### Task 7: Features – Favorites + Home -> Mohamed Mostafa
- `feature/favourites/`  
- `feature/home/`  

---
### Task 8: Features – Orders + Service
- `feature/order/`  
- `service/`  
- `BrewBuddy.kt`  

