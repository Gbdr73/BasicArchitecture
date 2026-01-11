package ru.otus.basicarchitecture

import android.util.Log
import java.io.IOException
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressApiService: AddressApiService
) : AddressRepository {
    override suspend fun suggestAddress(query: String): List<UserAddress> {
        val token = "Token ${BuildConfig.dadata_api_key}"

        val response = addressApiService.suggestAddress(token, AddressRequestDto(query))

        if (!response.isSuccessful){
            val errorResponse = response.errorBody()?.string()
            Log.e("API Error", "Error response: $errorResponse")
            throw IOException("Error response: $errorResponse")
        }

        val listAddressDataDto = response.body()?.suggestions?.map { suggestion ->
            suggestion.data.copy(fullAddress = suggestion.unrestricted_value)
        }

        val mapper = AddressMapper()
        val listUserAddress = listAddressDataDto?.map {
            mapper.mapDtoToEntity(it)
        }

        return listUserAddress ?: listOf()
    }
}