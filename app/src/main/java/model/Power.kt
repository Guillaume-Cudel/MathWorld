package model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlin.random.Random

@Entity(tableName = "power_table")
data class Power(
    val job_name: String,
    val powerName: String,
    val description: String
    ) {
    @PrimaryKey(autoGenerate = true)
    var powerId: Int = 0
}


data class JobWithPower(
    @Embedded val job: Job,
    @Relation(
        parentColumn = "name",
        entityColumn = "job_name"
    )
    val powers: List<Power>
)