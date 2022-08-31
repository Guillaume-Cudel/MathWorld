package model

import androidx.room.*

@Entity
    (tableName = "students_table")

data class Student(
    val class_id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val job: String,
    @ColumnInfo(name = "point_of_life") val pointOfLife: Int,
    var level: Int,
    var experience: Int,
    var group: Int,
    var belt: Int,
    var beltXp: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


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


