package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sealed_power_table")
data class SealedPower(
    @PrimaryKey val student_id: Int,
    val power1: Boolean,
    val power2: Boolean,
    val power3: Boolean,
    val power4: Boolean,
    val power5: Boolean,
    val power6: Boolean,
)
