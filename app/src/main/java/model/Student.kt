package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
    (tableName = "students_table")

data class Student(
    @PrimaryKey val studentId: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val job: String,
    @ColumnInfo(name = "point_of_life") val pointOfLife: Int,
    val level: Int,
    val experience: Int,
    val group: Int,
    val belt: String) {
}