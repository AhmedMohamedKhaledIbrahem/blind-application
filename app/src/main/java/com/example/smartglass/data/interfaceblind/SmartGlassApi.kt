package com.example.smartglass.data.interfaceblind

import com.example.smartglass.data.UploadResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SmartGlassApi {
    @Multipart
    @POST("detect")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("select_mode") selectMode: RequestBody,
        @Part("object_to_be_found") objectFinder:RequestBody
    ): Observable<ArrayList<UploadResponse>>


    companion object {
        operator fun invoke(): SmartGlassApi {
            return Retrofit.Builder()
                .baseUrl("http://10.0.0.105:5001/")

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SmartGlassApi::class.java)
        }
    }
}