package com.jafleck.testutil.preferences

import com.badlogic.gdx.Preferences
import java.util.*

class InMemoryPreferences : Preferences {
    private val properties = Properties()

    override fun putBoolean(key: String, value: Boolean): Preferences {
        properties[key] = java.lang.Boolean.toString(value)
        return this
    }

    override fun putInteger(key: String, value: Int): Preferences {
        properties[key] = value.toString()
        return this
    }

    override fun putLong(key: String, value: Long): Preferences {
        properties[key] = value.toString()
        return this
    }

    override fun putFloat(key: String, value: Float): Preferences {
        properties[key] = value.toString()
        return this
    }

    override fun putString(key: String, value: String): Preferences {
        properties[key] = value
        return this
    }

    override fun put(vals: Map<String, *>): Preferences {
        for ((key, value) in vals) {
            if (value is Boolean) putBoolean(key, value)
            if (value is Int) putInteger(key, value)
            if (value is Long) putLong(key, value)
            if (value is String) putString(key, value)
            if (value is Float) putFloat(key, value)
        }
        return this
    }

    override fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    override fun getInteger(key: String): Int {
        return getInteger(key, 0)
    }

    override fun getLong(key: String): Long {
        return getLong(key, 0)
    }

    override fun getFloat(key: String): Float {
        return getFloat(key, 0f)
    }

    override fun getString(key: String): String {
        return getString(key, "")
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return java.lang.Boolean.parseBoolean(properties.getProperty(key, java.lang.Boolean.toString(defValue)))
    }

    override fun getInteger(key: String, defValue: Int): Int {
        return Integer.parseInt(properties.getProperty(key, defValue.toString()))
    }

    override fun getLong(key: String, defValue: Long): Long {
        return java.lang.Long.parseLong(properties.getProperty(key, defValue.toString()))
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return java.lang.Float.parseFloat(properties.getProperty(key, defValue.toString()))
    }

    override fun getString(key: String, defValue: String): String {
        return properties.getProperty(key, defValue)
    }

    override fun get(): Map<String, *> {
        val map = HashMap<String, Any>()
        for ((key, value) in properties) {
            if (value is Boolean)
                map[key as String] = java.lang.Boolean.parseBoolean(value as String)
            if (value is Int) map[key as String] = Integer.parseInt(value as String)
            if (value is Long) map[key as String] = java.lang.Long.parseLong(value as String)
            if (value is String) map[key as String] = value
            if (value is Float) map[key as String] = java.lang.Float.parseFloat(value as String)
        }

        return map
    }

    override fun contains(key: String): Boolean {
        return properties.containsKey(key)
    }

    override fun clear() {
        properties.clear()
    }

    override fun flush() {
    }

    override fun remove(key: String) {
        properties.remove(key)
    }
}
