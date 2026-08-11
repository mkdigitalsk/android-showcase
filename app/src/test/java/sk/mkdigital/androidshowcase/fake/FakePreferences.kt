package sk.mkdigital.androidshowcase.fake

import sk.mkdigital.androidshowcase.data.local.preferences.Preferences

class FakePreferences(override val storageName: String = "fake") : Preferences {

    private val values = mutableMapOf<String, Any>()

    override suspend fun putString(key: String, value: String?) = put(key, value)
    override suspend fun getString(key: String): String? = values[key] as? String

    override suspend fun putBoolean(key: String, value: Boolean?) = put(key, value)
    override suspend fun getBoolean(key: String): Boolean? = values[key] as? Boolean

    override suspend fun putInt(key: String, value: Int?) = put(key, value)
    override suspend fun getInt(key: String): Int? = values[key] as? Int

    override suspend fun putFloat(key: String, value: Float?) = put(key, value)
    override suspend fun getFloat(key: String): Float? = values[key] as? Float

    override suspend fun putLong(key: String, value: Long?) = put(key, value)
    override suspend fun getLong(key: String): Long? = values[key] as? Long

    override suspend fun putDouble(key: String, value: Double?) = put(key, value)
    override suspend fun getDouble(key: String): Double? = values[key] as? Double

    override suspend fun remove(key: String) {
        values.remove(key)
    }

    override suspend fun clear() {
        values.clear()
    }

    private fun put(key: String, value: Any?) {
        if (value == null) values.remove(key) else values[key] = value
    }
}
