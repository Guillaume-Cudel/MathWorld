package dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import model.Job
import model.Power
import model.RpgClass
import model.Student


@Dao
interface ClassDAO {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClass(rpgClass: RpgClass)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJob(job: Job)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPowers(powers: List<Power>)

    // QUERY
    @Query("SELECT * FROM class_table ORDER BY level DESC")
    fun getAllClass(): Flow<List<RpgClass>>


    @Delete
    fun deleteRpgClass(rpgClass: RpgClass)
}