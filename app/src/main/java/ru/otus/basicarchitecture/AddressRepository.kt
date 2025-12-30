package ru.otus.basicarchitecture

interface AddressRepository {
    suspend fun suggestAddress(query: String): List<UserAddress>
}
