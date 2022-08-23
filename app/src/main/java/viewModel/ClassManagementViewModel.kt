package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.StudentsInClass
import repository.MathWorldRepository

class ClassManagementViewModel(private val repository: MathWorldRepository): ViewModel() {


    fun getAllStudentsInClass(class_id: Int): LiveData<List<StudentsInClass>>?{
        var students: LiveData<List<StudentsInClass>>? = null
        viewModelScope.launch {
            students = repository.getAllStudentsInClass(class_id).asLiveData()
        }
        return students
    }
}