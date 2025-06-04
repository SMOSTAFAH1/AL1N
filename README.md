# AL1N – CryptoTracker App

An Android cryptocurrency tracking application developed in Java for mobile device programming coursework.

---

## 📖 Project Overview

**AL1N** (CryptoTracker) is an Android application developed in **Java** that provides users with the following capabilities:

1. **User Authentication** - Register and login with session persistence option
2. **Real-time Cryptocurrency Data** - View live prices for cryptocurrencies (Bitcoin, Ethereum, etc.) from external APIs (CoinMarketCap/CoinGecko)
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
- **Activities**: Login, Register, Main, CryptoDetail
- **Fragments**: Home, Favorites, Profile
- **Adapters**: CryptoAdapter, FavoriteAdapter
- **Database**: SQLite with FavoritesDbHelper and FavoritesDao
- **Network**: Retrofit client for API communication
- **Models**: Data classes for cryptocurrency and favorite items

### Data Flow
1. **Authentication** - User credentials stored in SharedPreferences
2. **API Integration** - Real-time data fetched from CoinGecko/CoinMarketCap APIs
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
   The project includes the following dependencies:
   ```gradle
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
   implementation 'androidx.recyclerview:recyclerview:1.2.1'
   implementation 'com.google.android.material:material:1.4.0'
   ```

4. **Run the application**
   - Create or select an emulator with API Level ≥ 21
   - Or connect a physical device with USB debugging enabled
   - Click **Run ▶** in Android Studio

### API Configuration
- The project uses the public CoinGecko API by default (no API key required)
- To use CoinMarketCap, update the base URL in `CoinMarketCapClient.java` and add your API key

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
│   │   │   │   ├── activities/
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── RegisterActivity.java
│   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   └── CryptoDetailActivity.java
│   │   │   │   ├── fragments/
│   │   │   │   │   ├── HomeFragment.java
│   │   │   │   │   ├── FavoritesFragment.java
│   │   │   │   │   └── ProfileFragment.java
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── CryptoAdapter.java
│   │   │   │   │   └── FavoriteAdapter.java
│   │   │   │   ├── database/
│   │   │   │   │   ├── FavoritesDbHelper.java
│   │   │   │   │   └── FavoritesDao.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── CryptoItem.java
│   │   │   │   │   └── FavoriteItem.java
│   │   │   │   ├── network/
│   │   │   │   │   ├── CoinMarketCapClient.java
│   │   │   │   │   └── CoinMarketCapService.java
│   │   │   │   └── utils/
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
- **Retrofit HTTP Client**: Handles asynchronous network requests
- **JSON Parsing**: Automatic deserialization using Gson converter
- **Error Handling**: Graceful handling of network failures and API errors

### UI/UX Features
- **Material Design**: Consistent use of Material Design components
- **Responsive Layout**: Optimized for various screen sizes
- **Smooth Animations**: RecyclerView animations for item interactions
- **Bottom Navigation**: Intuitive navigation between main sections

---

## 🎯 Future Enhancements

### Potential Improvements
1. **Automated Updates** - Implement JobService for periodic price updates and notifications
2. **Advanced Sensors** - Integrate QR scanner for wallet addresses or GPS for location-based features
3. **Enhanced UI** - Add animations, themes, and improved responsive design
4. **Internationalization** - Multi-language support with proper localization
5. **Testing Coverage** - Unit tests for database operations and UI tests with Espresso
6. **Security** - Enhanced encryption for sensitive user data

### Technical Debt
- Migrate layouts to ConstraintLayout for better performance
- Externalize all strings to `strings.xml`
- Implement proper error handling and loading states
- Add comprehensive logging and crash reporting

---

## 📄 License

This project is developed for educational purposes as part of a mobile device programming course.

---

## 🤝 Contributing

This is an academic project. For suggestions or improvements, please open an issue or submit a pull request.
