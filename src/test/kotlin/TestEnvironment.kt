import kotlin.test.Test

class TestEnvironment {
    @Test
    fun loadVersion() {
        // Read the version from the version.txt file, make sure it doesn't crash
        Environment.readVersionFile()
        // Calling does not throw any errors (it has been initialized)
        Environment.version
    }
}
