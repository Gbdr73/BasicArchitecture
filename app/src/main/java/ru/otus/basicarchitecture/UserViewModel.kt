package ru.otus.basicarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val wizardCache: WizardCache
): ViewModel() {
    private var _canContinue = MutableLiveData<Boolean>(false)
    val canContinue: LiveData<Boolean>
        get() = _canContinue
    private var _errorAge = MutableLiveData<Boolean>()
    val errorAge: LiveData<Boolean>
        get() = _errorAge
    private var _errorEmptyName = MutableLiveData<Boolean>()
    val errorEmptyName: LiveData<Boolean>
        get() = _errorEmptyName
    private var _errorEmptySurname = MutableLiveData<Boolean>()
    val errorEmptySurname: LiveData<Boolean>
        get() = _errorEmptySurname
    private var _errorEmptyBirthday = MutableLiveData<Boolean>()
    val errorEmptyBirthday: LiveData<Boolean>
        get() = _errorEmptyBirthday

    private var _errorBirthday = MutableLiveData<Boolean>()
    val errorBirthday: LiveData<Boolean>
        get() = _errorBirthday

    fun validateData() {
        var successful = checkEmptyFields()

        if (successful == false){
            _canContinue.value = false
            return
        }

        successful = checkAge()

        if (successful == false){
            _canContinue.value = false
            return
        }
        _canContinue.value = true
    }

    fun setName(name: String) {
        wizardCache.userNameDate.name = name
    }

    fun setSurname(surname: String) {
        wizardCache.userNameDate.surname = surname
    }

    fun setBirthday(birthday: String) {
        wizardCache.userNameDate.birthday = birthday
    }

    private fun checkAge(): Boolean {
        var successful = true
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        try {
            val birthDate = sdf.parse(wizardCache.userNameDate.birthday)
            _errorBirthday.value = false

            val today = Calendar.getInstance().time
            val diff = today.time - birthDate.time
            val years = (diff / (1000L * 60 * 60 *24 * 365)).toInt()

            if (years < 18) {
                _errorAge.value = true
                successful = false
            } else {
                _errorAge.value = false
            }
        } catch (e: Exception) {
            _errorBirthday.value = true
            successful = false
        }

        return successful
    }

    private fun checkEmptyFields(): Boolean{
        var successful = true

        if (wizardCache.userNameDate.name == ""){
            _errorEmptyName.value = true
            successful = false
        } else{
            _errorEmptyName.value = false
        }

        if (wizardCache.userNameDate.surname == ""){
            _errorEmptySurname.value = true
            successful = false
        } else {
            _errorEmptySurname.value = false
        }

        if (wizardCache.userNameDate.birthday == ""){
            _errorEmptyBirthday.value = true
            successful = false
        } else{
            _errorEmptyBirthday.value = false
        }
        return successful
    }
}