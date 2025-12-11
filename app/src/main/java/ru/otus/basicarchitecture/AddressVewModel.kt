package ru.otus.basicarchitecture


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val wizardCache: WizardCache
): ViewModel() {
    private var _canContinue = MutableLiveData<Boolean>(false)
    val canContinue: LiveData<Boolean>
        get() = _canContinue
    private var _errorEmptyCity = MutableLiveData<Boolean>()
    val errorEmptyCity: LiveData<Boolean>
        get() = _errorEmptyCity
    private var _errorEmptyCountry = MutableLiveData<Boolean>()
    val errorEmptyCountry: LiveData<Boolean>
        get() = _errorEmptyCountry
    private var _errorEmptyAddress = MutableLiveData<Boolean>()
    val errorEmptyAddress: LiveData<Boolean>
        get() = _errorEmptyAddress

    fun validateData() {
        var successful = checkEmptyFields()

        if (successful == false){
            _canContinue.value = false
            return
        }
        _canContinue.value = true
    }

    fun setCountry(country: String) {
        wizardCache.userAddress.country = country
    }

    fun setCity(city: String) {
        wizardCache.userAddress.city = city
    }

    fun setAddress(address: String) {
        wizardCache.userAddress.address = address
    }

    private fun checkEmptyFields(): Boolean{
        var successful = true

        if (wizardCache.userAddress.country == ""){
            _errorEmptyCountry.value = true
            successful = false
        } else{
            _errorEmptyCountry.value = false
        }

        if (wizardCache.userAddress.city == ""){
            _errorEmptyCity.value = true
            successful = false
        } else {
            _errorEmptyCity.value = false
        }

        if (wizardCache.userAddress.address == ""){
            _errorEmptyAddress.value = true
            successful = false
        } else{
            _errorEmptyAddress.value = false
        }
        return successful
    }
}
