package com.example.joshuawu.mynedemo.DataModels

/**
 * Created by joshuawu on 12/21/17.
 */

/**
 * Less verbose than using only Java
 * Kotlin data classes are super cool
 */
data class MyneLoginResponse(
        val success: Boolean? = null,
        val message: String? = null,
        val token: String? = null)