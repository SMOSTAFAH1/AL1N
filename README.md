# AL1N – CryptoTracker App

An Android cryptocurrency tracking application developed in Java for mobile device programming coursework.

---

## 📖 Project Overview

**AL1N** (CryptoTracker) is an Android application developed in **Java** that provides users with the following capabilities:

1. **User Authentication** - Register and login with session persistence option
2. **Real-time Cryptocurrency Data** - View live prices for cryptocurrencies (Bitcoin, Ethereum, etc.) from external API (CoinMarketCap)
3. **Detailed Views** - Access detailed information for each cryptocurrency with sharing functionality
4. **Favorites Management** - Add, remove, and organize favorite cryptocurrencies using SQLite database with swipe gestures
5. **User Profile** - Password management, logout functionality, and system notifications
6. **Navigation** - Seamless navigation between Home, Favorites, and Profile sections using BottomNavigationView
7. **Multi-user Support** - Support for multiple user accounts with isolated data and preferences

The project demonstrates key Android development concepts including Activities, Fragments, RecyclerView, SQLite Database, Notifications, Intent sharing, SharedPreferences, and more.

---

## ✨ Features

### Core Requirements
- **Background API Tasks** - Asynchronous HTTP requests using Retrofit to fetch real-time cryptocurrency data
- **Custom RecyclerView** - Displays cryptocurrency information with icons, names, prices, and 24h changes
- **Detail Activity** - Secondary screen showing detailed cryptocurrency information with sharing capability
- **System Notifications** - Login, logout, and password change notifications
- **Text Sharing** - Share cryptocurrency data as formatted text

### Optional Features
- **Session Persistence** - "Keep me logged in" functionality using SharedPreferences
- **Offline Navigation** - SQLite database for local storage of favorite cryptocurrencies
- **Multimedia Sharing** - Share cryptocurrency icons/images
- **Data Management** - Add, edit, and delete favorite cryptocurrencies with swipe gestures
- **Fragment Architecture** - Modular design using Fragments with BottomNavigationView

---

## 🏗️ Architecture

### Core Components
- **Activities**: Login, Register, Main, CryptoDetail (all in main package)
- **Fragments**: Home, Favorites, Profile
- **Adapters**: CryptoAdapter, CryptoSearchAdapter, FavoriteAdapter
- **Database**: SQLite with FavoritesDbHelper and FavoritesDao
- **Services**: CoinMarketCap API client and service interfaces
- **Models**: Data classes for cryptocurrency items, API responses, and favorite items
- **Utils**: Data mapping, chart utilities, and notification helpers

### Data Flow
1. **Authentication** - User credentials stored in SharedPreferences
2. **API Integration** - Real-time data fetched from CoinMarketCap API
3. **Local Storage** - Favorites persisted in SQLite database
4. **UI Updates** - RecyclerView adapters handle data binding and user interactions

---

## 🚀 Installation and Setup

### Prerequisites
- Android Studio (version ≥ 4.2)
- Android SDK with API Level ≥ 21 (Android 5.0)
- Internet connection for API data

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/SMOSTAFAH1/AL1N.git
   cd AL1N
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned AL1N folder

3. **Configure dependencies**
   The project includes the following dependencies:   ```gradle
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
   implementation 'androidx.recyclerview:recyclerview:1.2.1'
   implementation 'com.google.android.material:material:1.4.0'
   implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
   implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
   ```

4. **Run the application**
   - Create or select an emulator with API Level ≥ 21
   - Or connect a physical device with USB debugging enabled
   - Click **Run ▶** in Android Studio

### API Configuration
- The project is pre-configured with CoinMarketCap API (base URL: `https://pro-api.coinmarketcap.com/`)
- API key is already included in `CoinMarketCapClient.java` but can be updated with your own key if needed

---

## 📱 Usage

### Getting Started
1. **Registration/Login** - Create a new account or log in with existing credentials
2. **Home Screen** - Browse real-time cryptocurrency prices and market data
3. **Favorites** - Add cryptocurrencies to your favorites list for quick access
4. **Profile** - Manage account settings, change password, and logout

### Key Interactions
- **Tap** cryptocurrency items to view detailed information
- **Swipe right** on favorites to pin/unpin them
- **Swipe left** on favorites to remove them
- **Long press** favorites to share their icons
- **Pull to refresh** on the home screen to update prices

---

## 📂 Project Structure

```
AL1N/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/grupo32/al1n/
│   │   │   │   ├── CryptoDetailActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── RegisterActivity.java
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── CryptoAdapter.java
│   │   │   │   │   ├── CryptoSearchAdapter.java
│   │   │   │   │   └── FavoriteAdapter.java
│   │   │   │   ├── database/
│   │   │   │   │   ├── FavoritesDao.java
│   │   │   │   │   └── FavoritesDbHelper.java
│   │   │   │   ├── fragments/
│   │   │   │   │   ├── FavoritesFragment.java
│   │   │   │   │   ├── HomeFragment.java
│   │   │   │   │   └── ProfileFragment.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── CoinMarketCapResponse.java
│   │   │   │   │   ├── CryptoDetailInfo.java
│   │   │   │   │   ├── CryptoItem.java
│   │   │   │   │   ├── FavoriteItem.java
│   │   │   │   │   └── PriceHistoryItem.java
│   │   │   │   ├── services/
│   │   │   │   │   ├── CoinMarketCapClient.java
│   │   │   │   │   └── CoinMarketCapService.java
│   │   │   │   └── utils/
│   │   │   │       ├── ChartUtils.java
│   │   │   │       ├── CryptoDataHolder.java
│   │   │   │       ├── CryptoDataMapper.java
│   │   │   │       └── NotificationHelper.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── menu/
│   │   │   │   ├── drawable/
│   │   │   │   ├── values/
│   │   │   │   └── AndroidManifest.xml
│   └── build.gradle
└── README.md
```

---

## 🔧 Technical Implementation

### Database Schema
- **Users Table**: Stores user credentials and preferences
- **Favorites Table**: Manages user's favorite cryptocurrencies with pinning capability

### API Integration
- **Retrofit HTTP Client**: Handles asynchronous network requests to CoinMarketCap API
- **JSON Parsing**: Automatic deserialization of CoinMarketCap responses using Gson converter
- **Data Mapping**: Custom utilities to convert CoinMarketCap data to app-specific models
- **Error Handling**: Graceful handling of network failures and API errors

### UI/UX Features
- **Material Design**: Consistent use of Material Design components
- **Responsive Layout**: Optimized for various screen sizes
- **Smooth Animations**: RecyclerView animations for item interactions
- **Bottom Navigation**: Intuitive navigation between main sections