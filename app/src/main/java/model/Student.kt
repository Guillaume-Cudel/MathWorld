package model

import androidx.room.*
import java.io.Serializable

@Entity
    (tableName = "students_table")

data class Student(
    val class_id: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val job: String,
    @ColumnInfo(name = "point_of_life") var pointOfLife: Int,
    var level: Int,
    var experience: Int,
    var xpMax: Int,
    var group: Int,
    var belt: Int,
    var beltXp: Int): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


data class StudentsInClass(
    @Embedded val studentsClass: StudentsClass,
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

data class StudentWithPowers(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "id",
        entityColumn = "student_id"
    )
    val sealedPowers: List<SealedPower>
)


