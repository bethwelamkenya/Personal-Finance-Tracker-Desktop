package viewModels

/**
 * Manages the lifecycle of ViewModels in the application
 */
object ViewModelStore {
    val viewModels = mutableMapOf<String, MyBaseViewModel>()

    /**
     * Gets or creates a ViewModel of the specified type
     */
    inline fun <reified T : MyBaseViewModel> getViewModel(key: String = T::class.java.name): T {
        return viewModels.getOrPut(key) {
            T::class.java.getDeclaredConstructor().newInstance()
        } as T
    }

    /**
     * Clears all ViewModels
     */
    fun clear() {
        viewModels.values.forEach { it.clear() }
        viewModels.clear()
    }
}
