package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.RpgClass
import repository.MathWorldRepository

class MainViewModel(private val repository: MathWorldRepository): ViewModel() {

    val allClasses: LiveData<List<RpgClass>> = repository.allRpgClass.asLiveData()

    fun insertClass(rpgClass: RpgClass) = viewModelScope.launch {
        repository.insertClass(rpgClass)
    }



}