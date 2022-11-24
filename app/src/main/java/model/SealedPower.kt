package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sealed_power_table")
data class SealedPower(
    @PrimaryKey val student_id: Int,
    val power1: Int,
    val power2: Int,
    val power3: Int,
    val power4: Int,
    val power5: Int,
    val power6: Int,
)
