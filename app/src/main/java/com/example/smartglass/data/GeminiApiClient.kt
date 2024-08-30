package com.example.smartglass.data

import com.google.ai.client.generativeai.GenerativeModel

class GeminiApiClient {
    private val key ="AIzaSyAaBScGRil-qRVtvkdjmU_XFa4vVjD0egw"
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = key
    )
     suspend fun aiAssistant(prompt:String, callback: (String?) -> Unit){
        val response = generativeModel.generateContent(prompt)
        val result =response.text?.replace("*"," ")
        callback(result)
    }
}