package ru.otus.basicarchitecture


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    private val addressSuggestUseCase: AddressSuggestUseCase
): ViewModel() {
    private var _listUserAddress = MutableLiveData<List<UserAddress>>()
    val listUserAddress: LiveData<List<UserAddress>>
        get() = _listUserAddress
    private var _canContinue = MutableLiveData<Boolean>(false)
    val canContinue: LiveData<Boolean>
        get() = _canContinue
    private var _errorNetwork = MutableLiveData<Boolean>()
    val errorNetwork: LiveData<Boolean>
        get() = _errorNetwork
    private var _errorEmptyAddress = MutableLiveData<Boolean>()
    val errorEmptyAddress: LiveData<Boolean>
        get() = _errorEmptyAddress

    private var current_job: Job? = null

    fun validateData() {
        val successful = checkEmptyFields()

        if (successful == false){
            _canContinue.value = false
            return
        }
        _canContinue.value = true
    }

    fun setAddress(fullAddress: String) {
        wizardCache.userAddress.fullAddress = fullAddress
    }

    fun searchAddress(query: String) {
        current_job?.cancel()

        current_job = viewModelScope.launch {
            try {
                val result = addressSuggestUseCase.invoke(query)
                _listUserAddress.postValue(result)
            } catch (e: Exception) {
                _errorNetwork.postValue(true)
            }
        }
    }

    private fun checkEmptyFields(): Boolean{
        var successful = true
        if (wizardCache.userAddress.fullAddress.isBlank()){
            _errorEmptyAddress.value = true
            successful = false
        } else{
            _errorEmptyAddress.value = false
        }
        return successful
    }
}
