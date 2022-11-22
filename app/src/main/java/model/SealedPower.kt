package model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sealed_power")
data class SealedPower(
    val student_id: Int,
    val power1: Boolean,
    val power2: Boolean,
    val power3: Boolean,
    val power4: Boolean,
    val power5: Boolean,
    val power6: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
