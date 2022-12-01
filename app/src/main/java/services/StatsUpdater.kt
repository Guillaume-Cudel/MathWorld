package services

import model.Student

interface StatsUpdater {

    fun updateExperience(student: Student, experience: Int, xpMax: Int)

    fun updateLevel(student: Student)

    fun updateLife(student: Student, life: Int)

    fun updateGroup(student: Student, group: Int)

    fun openDetail(student: Student)

}