package ui

import model.RpgClass
import model.Student

interface StatsUpdater {

    fun updateExperience(student: Student, experience: Int)

    fun updateLevel(student: Student)

    fun updateLife(life: Int)

    fun updateGroup(group: Int)
}