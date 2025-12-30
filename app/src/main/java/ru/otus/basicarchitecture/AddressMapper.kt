package ru.otus.basicarchitecture

class AddressMapper {
    fun mapDtoToEntity(dto: AddressDataDto) = UserAddress(
        fullAddress = dto.fullAddress ?: "",
        country = dto.country ?: "",
        city = dto.city ?: "",
        street = dto.street ?: "",
        house = dto.house ?: "",
        block = dto.block ?: ""
    )
}