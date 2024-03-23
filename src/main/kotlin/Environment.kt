object Environment {
    lateinit var version: String

    /**
     * Get the version of the application from the version.txt file.
     * Updates [version] accordingly.
     */
    fun readVersionFile() {
        version = this::class.java.getResource("/version.txt")!!.readText()
    }
}
