package ru.otus.basicarchitecture

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AddressApiService {
    @Headers("Content-Type: application/json")
    @POST("suggest/address")
    suspend fun suggestAddress(
        @Header("Authorization") token: String,
        @Body request: AddressRequestDto,
        ): Response<AddressResponseDto>
}
