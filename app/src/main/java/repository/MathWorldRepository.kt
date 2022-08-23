package repository

import androidx.annotation.WorkerThread
import dao.ClassDAO
import kotlinx.coroutines.flow.Flow
import model.RpgClass
import model.Student
import model.StudentsInClass

class MathWorldRepository(private val dao: ClassDAO) {

    val allRpgClass: Flow<List<RpgClass>> = dao.getAllClass()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertClass(rpgClass: RpgClass){
        dao.insertClass(rpgClass)
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
}