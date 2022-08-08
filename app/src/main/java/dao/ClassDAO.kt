package dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import model.RpgClass
import model.Student

@Dao
interface ClassDAO {

    @Insert
    fun insertClass(rpgClass: RpgClass)

    @Insert
    fun insertStudent(student: Student)

    @Query("SELECT * FROM class_table")
    fun getAllClass(): Cursor


    @Delete
    fun deleteRpgClass(rpgClass: RpgClass)
}