package com.example.greenthumb_test

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email_id") email_id:String,
        @Field("password") password: String,
        @Field("user_type") user_type: Int,
        @Field("provider_type") provider_type: Int
    ): Call<JsonObject>

}