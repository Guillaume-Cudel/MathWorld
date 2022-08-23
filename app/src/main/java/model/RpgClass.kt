package model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "class_table")

data class RpgClass(@PrimaryKey val id: Int,
                    val name: String,
                    val level: String) : Serializable