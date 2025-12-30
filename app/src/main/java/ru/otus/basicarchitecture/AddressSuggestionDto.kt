package ru.otus.basicarchitecture

data class AddressSuggestionDto(
    val value: String,
    val unrestricted_value: String,
    val data: AddressDataDto
)
