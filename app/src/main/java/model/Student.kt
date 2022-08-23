package model

import androidx.room.*

@Entity
    (tableName = "students_table")

data class Student(
    @PrimaryKey val id: Int,
    val class_id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val job: String,
    @ColumnInfo(name = "point_of_life") val pointOfLife: Int,
    val level: Int,
    val experience: Int,
    val group: Int,
    val belt: String)


data class StudentsInClass(
    @Embedded val rpgClass: RpgClass,
    @Relation(
        parentColumn = "id",
        entityColumn = "class_id"
    )
    val students: List<Student>
)

data class StudentWithJob(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "id",
        entityColumn = "name"
    )
    val job: Job
)


