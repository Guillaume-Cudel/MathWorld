package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_table")
open class Job(@PrimaryKey val name: String, val powers: List<Power>) {


}