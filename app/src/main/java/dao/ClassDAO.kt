package dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import model.*


@Dao
interface ClassDAO {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClass(studentsClass: StudentsClass)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJob(job: Job)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPowersInformations(powers: List<Power>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSealedPowers(sealedPowers: SealedPower)

    // QUERY
    @Query("SELECT * FROM class_table ORDER BY level DESC")
    fun getAllClass(): Flow<List<StudentsClass>>

    @Transaction
    @Query("SELECT * FROM class_table WHERE id = :class_id ORDER BY name ASC")
    fun getAllStudentsInClass(class_id: Int): Flow<List<StudentsInClass>>


    @Transaction
    @Query("SELECT * FROM job_table WHERE name = :job")
    fun getAllPowersInformationsByJob(job: String): Flow<List<JobWithPower>>

    @Transaction
    @Query("SELECT * FROM class_table WHERE id = :id")
    fun getClassInformation(id: Int): Flow<StudentsClass>

    @Transaction
    @Query("SELECT * FROM student_table WHERE id = :studentId")
    fun getSealedPowersByStudent(studentId: Int): Flow<StudentWithSealedPowers>

    @Update
    suspend fun updateStudent(student: Student)

    @Update
    suspend fun updateSealedPower(sealedPower: SealedPower)

    @Update
    suspend fun updateClass(studentClass: StudentsClass)

    @Delete
    fun deleteRpgClass(studentsClass: StudentsClass)
}