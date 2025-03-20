package common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URI
import java.nio.file.Paths

class PathUtilTest {

    @Test
    fun `isFileURL returns true for file URLs`() {
        assertTrue(isFileURL("file:///path/to/file"))
        assertTrue(isFileURL("file://localhost/path/to/file"))
    }

    @Test
    fun `isFileURL returns false for non-file URLs`() {
        assertFalse(isFileURL("http://example.com"))
        assertFalse(isFileURL("https://example.com"))
        assertFalse(isFileURL("ftp://example.com"))
    }

    @Test
    fun `isFileURL returns false for non-URL strings`() {
        assertFalse(isFileURL("/path/to/file"))
        assertFalse(isFileURL("C:\\path\\to\\file"))
        assertFalse(isFileURL(""))
    }

    @Test
    fun `fileURLToPath converts file URLs to paths`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("C:\\path\\to\\file", fileURLToPath("file:///C:/path/to/file"))
            assertEquals("C:\\path\\to\\file", fileURLToPath("file://localhost/C:/path/to/file"))
        } else {
            assertEquals("/path/to/file", fileURLToPath("file:///path/to/file"))
            assertEquals("/path/to/file", fileURLToPath("file://localhost/path/to/file"))
        }
    }

    @Test
    fun `fileURLToPath handles URLs with special characters`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("C:\\path\\with space", fileURLToPath("file:///C:/path/with%20space"))
        } else {
            assertEquals("/path/with space", fileURLToPath("file:///path/with%20space"))
        }
    }

    @Test
    fun `fileURLToPath throws for non-file URLs`() {
        assertThrows(IllegalArgumentException::class.java) {
            fileURLToPath("http://example.com")
        }
    }

    @Test
    fun `pathToFileURL converts paths to file URLs`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("file:///C:/path/to/file", pathToFileURL("C:\\path\\to\\file"))
        } else {
            assertEquals("file:///path/to/file", pathToFileURL("/path/to/file"))
        }
    }

    @Test
    fun `pathToFileURL handles paths with special characters`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("file:///C:/path/with%20space", pathToFileURL("C:\\path\\with space"))
        } else {
            assertEquals("file:///path/with%20space", pathToFileURL("/path/with space"))
        }
    }

    @Test
    fun `isAbsolutePath returns true for absolute paths`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertTrue(isAbsolutePath("C:\\path\\to\\file"))
            assertTrue(isAbsolutePath("D:\\path\\to\\file"))
        } else {
            assertTrue(isAbsolutePath("/path/to/file"))
            assertTrue(isAbsolutePath("/usr/local/bin"))
        }
    }

    @Test
    fun `isAbsolutePath returns false for relative paths`() {
        assertFalse(isAbsolutePath("path/to/file"))
        assertFalse(isAbsolutePath("./path/to/file"))
        assertFalse(isAbsolutePath("../path/to/file"))
    }

    @Test
    fun `isAbsolutePath returns false for empty strings`() {
        assertFalse(isAbsolutePath(""))
    }

    private fun isAbsolutePath(string: String): Boolean {
        val path = Paths.get(string)
        return path.isAbsolute
    }

    private fun pathToFileURL(string: String): String {
        val path = Paths.get(string)
        val uri = path.toUri()
        return uri.toString()
    }

    private fun fileURLToPath(string: String): String {
        val uri = URI.create(string)
        val path = Paths.get(uri)
        return path.toString()
    }

    private fun isFileURL(string: String): Boolean {
        val uri = URI.create(string)
        return uri.scheme == "file"
    }

    @Test
    fun `resolveUriToAbsolutePath resolves file URLs to absolute paths`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("C:\\path\\to\\file", resolveUriToAbsolutePath("file:///C:/path/to/file"))
        } else {
            assertEquals("/path/to/file", resolveUriToAbsolutePath("file:///path/to/file"))
        }
    }

    @Test
    fun `resolveUriToAbsolutePath returns absolute paths unchanged`() {
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertEquals("C:\\path\\to\\file", resolveUriToAbsolutePath("C:\\path\\to\\file"))
        } else {
            assertEquals("/path/to/file", resolveUriToAbsolutePath("/path/to/file"))
        }
    }

    @Test
    fun `resolveUriToAbsolutePath resolves relative paths against current working directory`() {
        val relativePath = "relative/path"
        val expected = File(System.getProperty("user.dir"), relativePath).absolutePath
        assertEquals(expected, resolveUriToAbsolutePath(relativePath))
    }

    @Test
    fun `resolveUriToAbsolutePath handles empty strings by returning current working directory`() {
        assertEquals(System.getProperty("user.dir"), resolveUriToAbsolutePath(""))
    }

    private fun resolveUriToAbsolutePath(string: String): String {
        val uri = URI.create(string)
        val path = Paths.get(uri)
        return path.toAbsolutePath().toString()
    }
}
