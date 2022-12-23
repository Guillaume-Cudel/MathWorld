package services

import model.Student

interface StatsUpdater {

    //todo centralise les fonctions avec un impl

    fun updateExperience(student: Student, experience: Int, xpMax: Int)

    fun updateLevel(student: Student)

    fun updateLife(student: Student, life: Int)

    fun updateGroup(student: Student, group: Int)

    fun openDetail(student: Student)

}