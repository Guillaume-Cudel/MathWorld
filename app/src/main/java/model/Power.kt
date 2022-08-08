package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_power")
data class Power(@PrimaryKey val jobName: String, val powerName: String, val description: String) {
}