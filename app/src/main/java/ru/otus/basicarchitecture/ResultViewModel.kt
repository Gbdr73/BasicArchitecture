package ru.otus.basicarchitecture


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val wizardCache: WizardCache): ViewModel() {
    private var _userName = MutableLiveData<UserNameDate>()
    val userName: LiveData<UserNameDate>
        get() = _userName
    private var _userAddress = MutableLiveData<UserAddress>()
    val userAddress: LiveData<UserAddress>
        get() = _userAddress
    private var _interests = MutableLiveData<List<String>>()
    val interests: LiveData<List<String>>
        get() = _interests

    fun loadUserInfo(){
        loadUserName()
        loadUserAddress()
        loadInterests()
    }

    private fun loadUserName(){
        _userName.value = wizardCache.userNameDate
    }

    private fun loadUserAddress(){
        _userAddress.value = wizardCache.userAddress
    }

    private fun loadInterests(){
        _interests.value = wizardCache.interests
    }
}
