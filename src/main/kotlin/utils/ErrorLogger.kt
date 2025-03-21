package utils

import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

// Custom error handler to log exceptions to a file
object ErrorLogger {
    private val logDir = File("logs").apply { mkdirs() }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

    fun logError(throwable: Throwable) {
        val timestamp = dateFormat.format(Date())
        val logFile = File(logDir, "error_$timestamp.log")

        try {
            val stackTrace = StringWriter()
            throwable.printStackTrace(PrintWriter(stackTrace))

            logFile.writeText("""
                Finance Tracker Error Log
                Timestamp: $timestamp
                
                Error: ${throwable.message}
                
                Stack Trace:
                $stackTrace
            """.trimIndent())

            println("Error logged to: ${logFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to write error log: ${e.message}")
        }
    }
}