package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.*
import repository.MathWorldRepository

class DatabaseCallsViewModel(private val repository: MathWorldRepository): ViewModel() {

    fun insertStudent(student: Student) = viewModelScope.launch {
        repository.insertStudent(student)
    }

    fun insertSealedPowers(sealedPower: SealedPower) = viewModelScope.launch {
        repository.insertStudentSealedPowers(sealedPower)
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

    fun getPowersInformationsByJob(job: String): LiveData<List<JobWithPower>>?{
        var powers: LiveData<List<JobWithPower>>? = null
        viewModelScope.launch {
            powers = repository.getAllPowersInformationsByJob(job).asLiveData()
        }
        return powers
    }

    fun getSealedPowersByStudent(studentId: Int): LiveData<StudentWithSealedPowers>?{
        var sealedPowers: LiveData<StudentWithSealedPowers>? = null
        viewModelScope.launch {
            sealedPowers = repository.getSealedPowersByStudent(studentId).asLiveData()
        }
        return sealedPowers
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.updateStudent(student)
    }

    fun updateSealedPower(sealedPower: SealedPower) = viewModelScope.launch {
        repository.updateSealedPower(sealedPower)
    }

}