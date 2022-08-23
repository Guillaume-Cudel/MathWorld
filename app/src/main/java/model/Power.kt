package model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "table_power")
data class Power(
    @PrimaryKey val id: Int,
    val job_name: String,
    val powerName: String,
    val description: String,
    )


data class JobWithPower(
    @Embedded val job: Job,
    @Relation(
        parentColumn = "name",
        entityColumn = "job_name"
    )
    val powers: List<Power>
)