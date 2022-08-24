package viewModel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import model.RpgClass
import repository.MathWorldRepository

class MainViewModel: ViewModel() {

    private val _classNumber = MutableLiveData<Int>()
    val classNumber: LiveData<Int> = _classNumber

    fun setClass(number: Int){
        _classNumber.value = number
    }


}