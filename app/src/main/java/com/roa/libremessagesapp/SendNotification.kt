package com.roa.libremessagesapp

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SendNotification(
    private val userFcmToken: String,
    private val title: String,
    private val body: String,
    private val context: Context
) {

    private val postUrl = "https://fcm.googleapis.com/v1/projects/libre-messages-app/messages:send"

    fun sendNotifications() {
        val requestQueue = Volley.newRequestQueue(context)
        val mainObj = JSONObject()
        try {
            val messageObject = JSONObject()
            val notificationObject = JSONObject()
            notificationObject.put("title", title)
            notificationObject.put("body", body)
            messageObject.put("token", userFcmToken)
            messageObject.put("notification", notificationObject)
            mainObj.put("message", messageObject)

            val request = object : JsonObjectRequest(
                Request.Method.POST, postUrl, mainObj,
                { response ->
                    // codigo que se ejecuta al obtener una respuesta
                },
                { volleyError ->
                    // codigo que se ejecuta al ocurrir un error
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val accessToken = AccessToken(context)
                    val accessKey = accessToken.getAccessToken()
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "Bearer $accessKey"
                    return headers
                }

            }

            requestQueue.add(request)
        } catch (e: JSONException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
