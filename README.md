## 🐱 A Kotlin Multiplatform Cat Breeds app built with Clean Architecture, MVVM, and Jetpack Compose Multiplatform.

## ✨ Features

This project demonstrates a real-world **Kotlin Multiplatform (KMP)** app with **Clean Architecture** and **MVVM**.

### 🔐 Login
- Username and password fields with validation (length, forbidden characters).
- Simulated authentication with session persistence (token stored locally).
- On success, navigate to the main screen.

### 🐾 Cat Breed List
- Paginated and scrollable list of breeds.
- Each item shows:
  - Circular image with placeholder & error handling.
  - Name and short description.
- Pull-to-refresh support.
- Offline mode (cached data shown when offline).
- Navigation to breed details on tap.

### 📖 Cat Details
- Large cat image with a loading indicator.
- Full breed description and metadata:
  - Origin
  - Temperament
  - Life span
- "Like" button to add/remove from favorites.
- Favorite state reflected in UI (e.g., filled heart icon).

### ❤️ Favorites
- Favorites stored locally using **SQLDelight**.
- Persisted across sessions.
- Dedicated Favorites screen.

### 🛠️ Tech Stack
- **Kotlin Multiplatform** (Android, iOS, Desktop)
- **Jetpack Compose Multiplatform** for UI
- **Ktor** for networking
- **Koin** for dependency injection
- **SQLDelight** for local persistence
- **Coil 3** for image loading
- **MVVM + Clean Architecture**

## 🏗️ Architecture

The app follows **Clean Architecture** with **MVVM** on a **Kotlin Multiplatform (KMP)** setup:

