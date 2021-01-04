package com.example.greenthumb_test

data class CountryResponse(
    val data: List<CountryData>,
    val message: String,
    val status: String
)