# 🎉 HoneyPot - Your Personal Finance Buddy

![Android](https://img.shields.io/badge/Android-%233DDC84.svg?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-%2300C853.svg?style=for-the-badge&logo=jetpack-compose&logoColor=white)

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)

## **🚀 Overview**

Welcome to **HoneyPot**, your personal finance tracking app made with **Kotlin** and **Jetpack Compose**! This project is all about making financial management simple, fun, and stress-free. Whether you’re budgeting like a pro or just starting, **HoneyPot** helps you stay on top of your money! 💰✨

### **✨ Why I Built This**

I wanted a **clean, intuitive, and fun way** to track income and expenses. No more boring spreadsheets - just a smooth experience with a **beautiful UI, insightful analytics, and helpful budgeting tools**. This app is my solution to making finance tracking **enjoyable and effortless**! 🚀

## **🌟 Features**

- 📌 **Track Income & Expenses**: Effortlessly record and keep track of your income and expenses in real time.
- 🏷 **Categories for Income & Expenses**: Includes all standard categories and allows custom categories.
- 📩 **Automatic Tracking via SMS**: Detects transactions from SMS alerts and tracks them in pending transactions for review.
- 💰 **Budget Management**: Create budgets to limit spending and stay on track.
- 🔔 **Budget Alerts**: Get notified when you're approaching or exceeding your budget.
- 🎨 **Modern UI**: Built with **Jetpack Compose** for a sleek and intuitive user experience.
- 📊 **Dashboard & Analytics**: Break down expenses and income by category with visual insights.
- 🏦 **Multiple Account Tracking**: Manage and track finances separately for different bank accounts.
- ☁ **Cloud Storage:** Your transaction records are securely stored on the server, keeping them safe and accessible.
- 📄 **PDF & CSV Export**: Easily export transaction data for reporting and record-keeping.

## **🛠 Tech Stack**

- **Kotlin** – Used as the primary programming language for the app.
- **Jetpack Compose** – Used for building modern, declarative UI components.
- **MVVM Architecture** - Keeping code clean and maintainable.
- **Dagger Hilt** – Dependency injection framework to manage app dependencies efficiently.
- **Retrofit** – Used for making network requests and parsing API responses.
- **Google Credential Manager** – Manages authentication and credentials securely, including integration with Google accounts for sign-in.
- **Room Database** – Local database management for offline data storage.
- **Coil** – For image loading and displaying in Compose-based UI.
- **Paging3** – For efficient paginated data retrieval and display in the UI.

## **🔧 Installation & Setup**

This is a complete Android app that interacts with a backend server. While you can't fully run the app locally without the server (since it relies on server APIs), you can still use it for testing and experimentation.

### Setup

1. Install JDK 17 and point Android Studio/`JAVA_HOME` to it (Gradle 8.9 and AGP 8.7 require it).
2. Clone the repository: `git clone https://github.com/honeypot/honeypot.git`
3. Open the project in Android Studio and run a Gradle sync.
4. Build/debug with `./gradlew :app:assembleLocalDebug` (only Azure AI calls leave the device).

## Related Repositories

### 🔧 Backend

You can find the backend code here:  
➡️ [Backend Repository | honeypot-backend](https://github.com/honeypot/honeypot-backend)

### 🌐 Website

You can find the landing web page code here:  
➡️ [Website Repository | honeypot-web](https://github.com/honeypot/honeypot-web)

## **🤝 Contributing**

Love this project? Want to make it even better? Feel free to **open issues, submit pull requests, or just drop feedback**! Let’s build something amazing together. 💡💻
