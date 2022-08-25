package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.RpgClass
import model.Student
import model.StudentsInClass
import repository.MathWorldRepository

class ClassManagementViewModel(private val repository: MathWorldRepository): ViewModel() {

    fun insertStudent(student: Student) = viewModelScope.launch {
        repository.insertStudent(student)
    }

    fun getAllStudentsInClass(class_id: Int): LiveData<List<StudentsInClass>>?{
        var students: LiveData<List<StudentsInClass>>? = null
        viewModelScope.launch {
            students = repository.getAllStudentsInClass(class_id).asLiveData()
        }
        return students
    }

    fun getClassInformation(class_id: Int): LiveData<RpgClass>?{
        var rpgClass: LiveData<RpgClass>? = null
        viewModelScope.launch {
            rpgClass = repository.getClassInformation(class_id).asLiveData()
        }
        return rpgClass
    }
}