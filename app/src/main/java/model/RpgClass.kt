package model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "class_table")

data class RpgClass(@PrimaryKey val name: String,
                    val students: List<Student>,
                    val level: String) : Serializable