package com.vyou.kmm.client.credentials

import com.russhwolf.settings.Settings

class SettingsMock: Settings {
    override val keys: Set<String>
        get() = setOf()
    override val size: Int
        get() = 0

    var stringValue = ""
    var longValue = 0L
    private val booleanValue = false
    private val doubleValue = 0.0
    private val floatValue = 0f
    private val intValue = 0

    override fun clear() {}
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = booleanValue
    override fun getBooleanOrNull(key: String): Boolean? = null
    override fun getDouble(key: String, defaultValue: Double) = doubleValue
    override fun getDoubleOrNull(key: String): Double? = null
    override fun getFloat(key: String, defaultValue: Float) = floatValue
    override fun getFloatOrNull(key: String): Float? = null
    override fun getInt(key: String, defaultValue: Int) = intValue
    override fun getIntOrNull(key: String): Int? = null
    override fun getLong(key: String, defaultValue: Long) = longValue
    override fun getLongOrNull(key: String): Long? = null
    override fun getString(key: String, defaultValue: String) = stringValue
    override fun getStringOrNull(key: String): String? = null
    override fun hasKey(key: String): Boolean = false
    override fun putBoolean(key: String, value: Boolean) {}
    override fun putDouble(key: String, value: Double) {}
    override fun putFloat(key: String, value: Float) {}
    override fun putInt(key: String, value: Int) {}
    override fun putLong(key: String, value: Long) {}
    override fun putString(key: String, value: String) {}
    override fun remove(key: String) {}
}