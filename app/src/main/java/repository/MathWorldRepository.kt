package repository

import androidx.annotation.WorkerThread
import dao.ClassDAO
import kotlinx.coroutines.flow.Flow
import model.RpgClass

class MathWorldRepository(private val dao: ClassDAO) {

    val allRpgClass: Flow<List<RpgClass>> = dao.getAllClass()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertClass(rpgClass: RpgClass){
        dao.insertClass(rpgClass)
    }


}