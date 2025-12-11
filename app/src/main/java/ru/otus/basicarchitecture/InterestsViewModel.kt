package ru.otus.basicarchitecture

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@HiltViewModel
class InterestsViewModel @Inject constructor(
    private val wizardCache: WizardCache): ViewModel() {
    private var _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue
    private var _listOfInterests = MutableLiveData<List<String>>()
    val listOfInterests: LiveData<List<String>>
        get() = _listOfInterests

    private val interests = listOf(
        "Animals", "Art", "Books", "Business", "Cars",
        "Cooking", "Dance", "Design", "Education",
        "Fashion", "Finance", "Fitness", "Food", "Gaming",
        "Health", "History", "Movies", "Music", "Politics",
        "Photography", "Programming", "Reading", "Science",
        "Space", "Sports", "Technology", "Travel"
    )

    fun checkInterests(){
        if (wizardCache.interests.size < 1){
            _canContinue.value = false
        } else {
            _canContinue.value = true
        }
    }

    fun setInterests(interests: List<String>){
        wizardCache.interests = interests
    }

    fun loadListOfInterests(){
        _listOfInterests.value = interests
    }
}