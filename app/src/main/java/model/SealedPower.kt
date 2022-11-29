package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sealed_power_table")
data class SealedPower(
    @PrimaryKey val student_id: Int,
    var power1: Int,
    var power2: Int,
    var power3: Int,
    var power4: Int,
    var power5: Int,
    var power6: Int,
)
