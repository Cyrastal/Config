package com.flj.latte.util.storage

import androidx.preference.PreferenceManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.flj.latte.global.Latte

/**
 * @author 傅令杰
 */
object LattePreference {

    /**
     * 提示:
     * Activity.getPreferences(int mode)生成 Activity名.xml 用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    private val appPreference =
        PreferenceManager.getDefaultSharedPreferences(Latte.instance.applicationContext)
    private const val APP_PREFERENCES_KEY = "profile"

    var appProfile: String?
        get() = appPreference.getString(APP_PREFERENCES_KEY, null)
        set(value) = appPreference
            .edit()
            .putString(APP_PREFERENCES_KEY, value)
            .apply()

    val appProfileJson: JSONObject
        get() {
            val profile = appProfile
            return JSON.parseObject(profile)
        }

    fun removeAppProfile() {
        appPreference
            .edit()
            .remove(APP_PREFERENCES_KEY)
            .apply()
    }

    fun clearAppPreferences() {
        appPreference
            .edit()
            .clear()
            .apply()
    }

    fun setAppFlag(key: String, flag: Boolean) {
        appPreference
            .edit()
            .putBoolean(key, flag)
            .apply()
    }

    fun setAppFlag(key: Enum<*>, flag: Boolean) {
        setAppFlag(key.name, flag)
    }

    fun getAppFlag(key: String): Boolean {
        return appPreference
            .getBoolean(key, false)
    }

    fun getAppFlag(key: Enum<*>): Boolean {
        return getAppFlag(key.name)
    }

    fun addCustomPreference(key: String, value: String) {
        appPreference
            .edit()
            .putString(key, value)
            .apply()
    }

    fun getCustomPreference(key: String): String? {
        return appPreference.getString(key, "")
    }

}