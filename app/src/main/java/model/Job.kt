package model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "job_table")
open class Job(
    @PrimaryKey val name: String
)


