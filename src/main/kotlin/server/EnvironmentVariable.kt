package server

open class EnvironmentVariable(private val key: String, private val default: String? = null) {
    fun get(): String {
        return System.getenv(key) ?: default ?: error("Environment variable $key not found")
    }
}
