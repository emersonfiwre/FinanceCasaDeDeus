package br.com.casadedeus.service.repository

import android.content.Context
import android.content.SharedPreferences
import br.com.casadedeus.service.constants.UserConstants

/**
 * Acesso a dados rápidos do projeto - SharedPreferences
 */
class SecurityPreferences(context: Context) {

    private val mPreferences: SharedPreferences =
        context.getSharedPreferences(
            UserConstants.SHARED.USER_SHARED,
            Context.MODE_PRIVATE
        )//set constants

    fun store(key: String, value: String) {
        mPreferences.edit().putString(key, value).apply()
    }

    fun remove(key: String) {
        mPreferences.edit().remove(key).apply()
    }

    fun get(key: String): String {
        return mPreferences.getString(key, "") ?: ""
    }
}