package com.example.alcoolougasolina.data

import android.content.Context

class SwitchRepository(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun salvarSwitch(modo: Boolean) {
        prefs.edit().putBoolean(KEY_MODO, modo).apply()
    }

    fun obterSwitch(): Boolean = prefs.getBoolean(KEY_MODO, true)

    fun removerSwitch() {
        prefs.edit().remove(KEY_MODO).apply()
    }

    companion object {
        private const val KEY_MODO = "modo"
    }
}