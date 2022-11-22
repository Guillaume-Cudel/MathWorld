package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.JobWithPower
import model.StudentsClass
import model.Student
import model.StudentsInClass
import repository.MathWorldRepository

class DatabaseCallsViewModel(private val repository: MathWorldRepository): ViewModel() {

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

    fun getClassInformation(class_id: Int): LiveData<StudentsClass>?{
        var studentsClass: LiveData<StudentsClass>? = null
        viewModelScope.launch {
            studentsClass = repository.getClassInformation(class_id).asLiveData()
        }
        return studentsClass
    }

    fun getPowersByJob(job: String): LiveData<List<JobWithPower>>?{
        var powers: LiveData<List<JobWithPower>>? = null
        viewModelScope.launch {
            powers = repository.getPowersByJob(job).asLiveData()
        }
        return powers
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.updateStudent(student)
    }
}