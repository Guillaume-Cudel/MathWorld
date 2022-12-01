package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sealed_power_table")
data class SealedPower(
    @PrimaryKey val student_id: Int,
    var power1: Int,
    var power1Actived: Boolean,
    var power2: Int,
    var power2Actived: Boolean,
    var power3: Int,
    var power3Actived: Boolean,
    var power4: Int,
    var power4Actived: Boolean,
    var power5: Int,
    var power5Actived: Boolean,
    var power6: Int,
    var power6Actived: Boolean,
    var powerToAssign: Int
)
