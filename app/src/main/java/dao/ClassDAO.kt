package dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import model.*


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

    @Transaction
    @Query("SELECT * FROM class_table WHERE id = :class_id ORDER BY name ASC")
    fun getAllStudentsInClass(class_id: Int): Flow<List<StudentsInClass>>

    @Transaction
    @Query("SELECT * FROM students_table WHERE id = :id")
    fun getPowerByStudent(id: Int): Flow<StudentWithJob>

    @Transaction
    @Query("SELECT * FROM job_table WHERE name = :job")
    fun getAllPowersByJob(job: String): Flow<List<JobWithPower>>


    @Delete
    fun deleteRpgClass(rpgClass: RpgClass)
}