package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "belt_table")
open class Belt(@PrimaryKey val id: Int, val color: String)