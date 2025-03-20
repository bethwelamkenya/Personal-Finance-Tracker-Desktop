package viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import models.CurrencyType
import ui.theme.AppColorScheme
import ui.theme.AppColorTheme
import java.util.prefs.Preferences

class SettingsViewModel : MyBaseViewModel() {
    private val preferences = Preferences.userRoot().node("personal_finance_tracker")

    private val _appColorTheme: MutableState<AppColorTheme> = mutableStateOf(
        AppColorTheme.fromString(
            preferences.get("app_color_theme", AppColorTheme.SYSTEM.name)
        )
    )
    val appColorTheme: State<AppColorTheme> = _appColorTheme

    private val _appColorScheme: MutableState<AppColorScheme> = mutableStateOf(
        AppColorScheme.fromString(
            preferences.get("app_color_scheme", AppColorScheme.DEFAULT_BLUE.name)
        )
    )
    val appColorScheme: State<AppColorScheme> = _appColorScheme

    private val _compactLayout: MutableState<Boolean> = mutableStateOf(
        preferences.getBoolean("compact_layout", false)
    )
    val compactLayout: State<Boolean> = _compactLayout

    private val _defaultCurrency: MutableState<CurrencyType> = mutableStateOf(
        CurrencyType.find(
            preferences.get("default_currency", CurrencyType.USD.code)
        )
    )
    val defaultCurrency: State<CurrencyType> = _defaultCurrency

    fun setAppColorTheme(theme: AppColorTheme) {
        _appColorTheme.value = theme
        preferences.put("app_color_theme", theme.name)
        preferences.flush()
    }

    fun setAppColorScheme(scheme: AppColorScheme) {
        _appColorScheme.value = scheme
        preferences.put("app_color_scheme", scheme.name)
        preferences.flush()
    }

    fun setCompactLayout(compact: Boolean) {
        _compactLayout.value = compact
        preferences.putBoolean("compact_layout", compact)
        preferences.flush()
    }

    fun setDefaultCurrency(currencyType: CurrencyType) {
        _defaultCurrency.value = currencyType
        preferences.put("default_currency", currencyType.code)
        preferences.flush()
    }
}
