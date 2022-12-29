package model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "class_table")

data class StudentsClass(@PrimaryKey val id: Int,
                         var name: String,
                         var level: String) : Serializable