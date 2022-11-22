package repository

import androidx.annotation.WorkerThread
import dao.ClassDAO
import kotlinx.coroutines.flow.Flow
import model.*

class MathWorldRepository(private val dao: ClassDAO) {

    val allStudentsClass: Flow<List<StudentsClass>> = dao.getAllClass()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertClass(studentsClass: StudentsClass){
        dao.insertClass(studentsClass)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertStudent(student: Student){
        dao.insertStudent(student)
    }

    @WorkerThread
    fun getAllStudentsInClass(class_id: Int): Flow<List<StudentsInClass>>{
       return dao.getAllStudentsInClass(class_id)
    }

    @WorkerThread
    fun getClassInformation(class_id: Int): Flow<StudentsClass>{
        return dao.getClassInformation(class_id)
    }

    @WorkerThread
    fun getPowersByJob(job: String): Flow<List<JobWithPower>>{
        return dao.getAllPowersByJob(job)
    }

    @WorkerThread
    suspend fun updateStudent(student: Student){
        dao.updateStudent(student)
    }
}