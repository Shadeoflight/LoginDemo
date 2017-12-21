package com.example.joshuawu.mynedemo.RetrofitToolkit

import com.example.joshuawu.mynedemo.DataModels.MyneLoginPost
import com.example.joshuawu.mynedemo.DataModels.MyneLoginResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*

/**
 * Created by joshuawu on 12/21/17.
 */

interface MyneLoginService{

    @POST("api/authenticate")
    fun login(@Body loginPostData: MyneLoginPost) : Observable<MyneLoginResponse>

    // Factory class for generation of the API service interface
    companion object Factory{
        fun create(): MyneLoginService{
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://basicnodejsapi-joshuawu777336318.codeanyapp.com/") // NOTE: ADD SERVER URL
                    .build()

            return retrofit.create(MyneLoginService::class.java)
        }
    }
}