package com.roa.libremessagesapp

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException

class AccessToken(private val context: Context) {

    companion object {
        const val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"
    }

    fun getAccessToken(): String? {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.service_account)
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(listOf(firebaseMessagingScope))

            googleCredentials.refresh()  // refresh the token
            googleCredentials.accessToken.tokenValue
        } catch (e: IOException) {
            Log.e("AccessToken", "Error al obtener el token de acceso: ${e.message}")
            null
        }
    }

}
