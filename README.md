# Personal Finance Tracker

A desktop application built with Kotlin and Compose for Desktop that helps users manage their personal finances, track transactions, set savings goals, and monitor their financial health.

## Features
- User Authentication: Secure signup and login functionality
- Account Management: Create and manage multiple bank accounts
- Transaction Tracking: Record deposits, withdrawals, and transfers
- Savings Goals: Set and track progress towards financial goals
- Multi-user Support: Transfer funds between users
- Real-time transaction history updates
- Data Visualization: Visualize financial data through charts and graphs
- User-friendly interface with Material Design 3
```agsl
git clone https://github.com/bethwelamkenya/personal-finance-tracker-desktop.git
```

## Technologies Used
- Kotlin: Primary programming language
- Compose for Desktop: Modern UI toolkit for desktop applications
- Material Design 3: Modern and responsive UI components
- MVVM Architecture: Clean separation of UI and business logic
## Getting Started
### Prerequisites
- JDK 11 or higher
- Gradle 7.0 or higher

### Installation
1. Clone the repository:
```
git clone https://github.com/bethwelamkenya/personal-finance-tracker-desktop.git
```
2. Navigate to the project directory:
```
cd ./personal_finance_tracker
```
3. Build the application:
```
./gradlew build
```
4. Run the application:
```
./gradlew run
```
## Project Structure
```
src/main/kotlin/
├── models/     # Data classes and enums
├── ui/         # UI components and screens
│   ├── components/  # Reusable UI components
│   ├── screens/     # Application screens
│   └── theme/       # Theme definitions and styling
├── utils/     # Utility classes and functions
├── viewModels/ # Business logic and state management
└── Main.kt     # Application entry point
```
## Usage

### Creating an Account
1. Launch the application
2. Sign up with your email and password
3. Create a new bank account with initial balance
### Recording Transactions
1. Navigate to the "Add Transaction" screen
2. Select the transaction type (deposit, withdrawal, transfer)
3. Enter the amount and select the account
4. For transfers, select the target account or user
5. Confirm the transaction
### Setting Savings Goals
1. Navigate to the "Goals" section
2. Create a new goal with a target amount and deadline
3. Track your progress and make contributions to your goals
## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.
## Acknowledgments
- Material Design for the UI components
- JetBrains for Kotlin and Compose for DesktopJetBrains for Kotlin and Compose for Desktop