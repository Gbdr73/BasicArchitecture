package ru.otus.basicarchitecture
import javax.inject.Inject

class WizardCache @Inject constructor(){
    var userNameDate: UserNameDate = UserNameDate("", "", "")
    var userAddress: UserAddress = UserAddress("", "", "")
    var interests: List<String> = listOf()
}

data class UserNameDate(
    var name: String,
    var surname: String,
    var birthday: String
)

data class UserAddress(
    var country: String,
    var city: String,
    var address: String
)