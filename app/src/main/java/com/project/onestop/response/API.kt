package com.project.onestop.response

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("Entries.php")
    fun userRegister(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("location") location: String,
        @Field("email") email: String,
        @Field("role") role: String,
        @Field("type") type: String,
        @Field("image") image: String
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("Login.php")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("Login.php")
    fun getuser(): Call<CommonResponse>

    @FormUrlEncoded
    @POST("GetEntries.php")
    fun getRole(
        @Field("role") role: String,
    ): Call<CommonResponse>


    @GET("getEventsCabs.php")
    fun getAll(): Call<CommonResponse>


    @FormUrlEncoded
    @POST("NgoRequest1.php")
    fun sendNgoRequests(
        @Field("ngoMobile") ngoMobile: String,
        @Field("userMobile") userMobile: String,
        @Field("userName") userName: String,
        @Field("userid") userid: String,
        @Field("ngoid") ngoid: String,
        @Field("image") image: String,
        @Field("ngoName") ngoName: String,
        @Field("requestsDetails") requestsDetails: String,
        @Field("time") time: String,
        @Field("status") status: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("message") message: String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("updateposted.php")
    fun updateNgoRequest(
        @Field("status") status: String,
        @Field("id") id: Int,
        @Field("ngoid") ngoid: String,
        @Field("userid") userid: String,
        @Field("type") type: String,
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("updatesentRequest.php")
    fun sendRequestNgo(
        @Field("status") status: String,
        @Field("ngoid") ngoid: String,
        @Field("userid") userid: String,
        @Field("userName") userName: String,
        @Field("userMobile") userMobile: String,
        @Field("time") time: String,
        @Field("type") type: String,
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("getPosted.php")
    fun getPosted(
        @Field("status") status: String,
        @Field("type") type: String,
    ): Call<ProductResponse>

    @FormUrlEncoded
    @POST("getPostedByid.php")
    fun getPostedbyUserid(
        @Field("userid") userid: String,
        @Field("type") type: String,
    ): Call<ProductResponse>


    @FormUrlEncoded
    @POST("getpostedbyngoid.php")
    fun getPostedbyngoUid(
        @Field("ngoid") ngoid: String,
        @Field("type") type: String,
        @Field("status") status: String,
    ): Call<ProductResponse>


}