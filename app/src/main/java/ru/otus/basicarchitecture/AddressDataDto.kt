package ru.otus.basicarchitecture

data class AddressDataDto(
    val country: String?,
    val city: String?,
    val street: String?,
    val house: String?,
    val block: String?,
    val fullAddress: String? = null
)
