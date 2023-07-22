package com.timekeeping.smart.share_preference

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.timekeeping.smart.entity.response.LoginResponse
import com.timekeeping.smart.extension.clearAll
import com.timekeeping.smart.extension.setString
import javax.inject.Inject

class HSBASharePrefImpl @Inject constructor(var context: Context) : HSBASharePref {

    companion object {
        const val MSAR_PREF = "TIME_KEPPING_PREF"
        const val TIME_KEPPING_PREF_USER = "TIME_KEPPING_PREF_USER"
    }

    private var mPrefs: SharedPreferences? = null

    init {
        mPrefs = context.getSharedPreferences(MSAR_PREF, Context.MODE_PRIVATE)
    }

    private fun initSharePref(sharePrefName: String): SharedPreferences {
        val keySpec = KeyGenParameterSpec.Builder(
            "_androidx_security_master_key_",
            PURPOSE_ENCRYPT or PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            sharePrefName,
            MasterKey.Builder(context).setKeyGenParameterSpec(keySpec).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    override var savedUser: LoginResponse?
        get() = try {
            Gson().fromJson(mPrefs?.getString(TIME_KEPPING_PREF_USER, null),
                LoginResponse::class.java)
        } catch (e: Exception) {
            null
        }
        set(value) {
            mPrefs?.setString(TIME_KEPPING_PREF_USER, Gson().toJson(value))
        }

    override fun logout() {
        mPrefs?.clearAll()
    }

}