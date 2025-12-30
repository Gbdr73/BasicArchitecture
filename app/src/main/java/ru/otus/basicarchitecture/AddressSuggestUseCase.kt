package ru.otus.basicarchitecture


class AddressSuggestUseCase(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(query: String): List<UserAddress> {
        return addressRepository.suggestAddress(query)
    }
}