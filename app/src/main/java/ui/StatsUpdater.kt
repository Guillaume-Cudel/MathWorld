package ui

import model.RpgClass
import model.Student

interface StatsUpdater {

    fun updateExperience(student: Student, experience: Int)

    fun updateLevel(student: Student)

    fun updateLife(student: Student, life: Int)

    fun updateGroup(student: Student, group: Int)
}