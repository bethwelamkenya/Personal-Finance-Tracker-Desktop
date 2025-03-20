package utils

import models.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

object DataSimulator {
    private val banks = listOf("Global Bank", "Metro Savings", "Tech Credit Union")
    private val firstNames = listOf("Alex", "Sam", "Jordan", "Taylor", "Casey")
    private val lastNames = listOf("Smith", "Johnson", "Brown", "Lee", "Wilson")
    private val goalNames = listOf(
        "Emergency Fund",
        "Vacation to Japan",
        "New Car Fund",
        "Home Renovation"
    )

    fun generateUser(): User {
        return User(
            name = "${firstNames.random()} ${lastNames.random()}",
            email = "${firstNames.random()}${lastNames.random()}@gmail.com",
            passwordHash = "${firstNames.random()} ${lastNames.random()}",
            salt = "${firstNames.random()} ${lastNames.random()}",
            createdAt = LocalDate.now().minusDays(Random.nextLong(365))
        )
    }

    fun generateBankAccounts(currencyType: CurrencyType, count: Int): List<BankAccount> {
        return List(count) {
            BankAccount(
                id = "ACC${1000 + it}",
                bankName = banks.random(),
                accountNumber = "XX${Random.nextInt(10000000, 99999999)}",
                holderName = "${firstNames.random()} ${lastNames.random()}",
                balance = Random.nextDouble(1000.0, 50000.0).round(2),
                currency = currencyType.code,
                createdAt = LocalDate.now().minusDays(Random.nextLong(365))
            )
        }
    }

    fun generateSavingsGoals(accounts: List<BankAccount>, count: Int): List<SavingsGoal> {
        return List(count) {
            val target = Random.nextDouble(5000.0, 30000.0).round(2)
            val saved = Random.nextDouble(0.0, target * 1.2).round(2)
            val account = accounts.random()

            SavingsGoal(
                id = "SG${2000 + it}",
                goalName = goalNames.random(),
                targetAmount = target,
                savedAmount = saved,
                accountNumber = account.accountNumber,
                currency = account.currency,
                createdAt = LocalDate.now().minusDays(Random.nextLong(180))
            )
        }
    }

    fun generateTransactions(accounts: List<BankAccount>, goals: List<SavingsGoal>, count: Int): List<Transaction> {
        return List(count) {
            val type = TransactionType.entries.toTypedArray().random()
            val account = accounts.random()

            Transaction(
                id = "TX${3000 + it}",
                type = type.name,
                accountNumber = account.accountNumber,
                amount = Random.nextDouble(50.0, 2500.0).round(2),
                currency = account.currency,
                timestamp = LocalDateTime.now().minusHours(Random.nextLong(720)),
                targetAccountNumber = if (type.name.contains("Transfer", true)) "XX${
                    Random.nextInt(
                        10000000,
                        99999999
                    )
                }" else null,
                targetGoalName = if (type.name.contains("Transfer", true)) goals.random().goalName else null,
                targetUserEmail = if (type.name.contains(
                        "Transfer",
                        true
                    )
                ) "${firstNames.random()}${lastNames.random()}@gmail.com" else null,
                goalName = if (type.name.contains("goal", true)) goals.random().goalName else null,
            )
        }
    }

    private fun Double.round(decimals: Int): Double {
        return "%.${decimals}f".format(this).toDouble()
    }
}
