package viewModel

import androidx.lifecycle.*
import model.Student

class MainViewModel: ViewModel() {

    private val _classNumber = MutableLiveData<Int>()
    val classNumber: LiveData<Int> = _classNumber

    fun setClass(number: Int){
        _classNumber.value = number
    }



}