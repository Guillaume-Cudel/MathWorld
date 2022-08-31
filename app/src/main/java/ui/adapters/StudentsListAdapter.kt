package ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider.BACKGROUND
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.guillaume.mathworld.R
import model.Student
import ui.StatsUpdater


class StudentsListAdapter(private val upStats: StatsUpdater): ListAdapter<Student, StudentViewHolder>(StudentViewHolder.StudentsComparator()) {

    private var xpMax: Int = 45

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = getItem(position)
        val xpBar = holder.xpBar

        displayExperience(current.level, current.experience, holder.xpInfo)
        holder.level.text = current.level.toString()
        xpBar.progress = current.experience
        xpBar.max = xpMax

        xpBar.setOnClickListener {
            xpBar.progress += 1

            if(xpBar.progress == xpBar.max){
                upStats.updateExperience(current, 0)
                upStats.updateLevel(current)
                displayExperience(current.level, current.experience, holder.xpInfo)
            } else {
                displayExperience(current.level, xpBar.progress, holder.xpInfo)
                upStats.updateExperience(current, xpBar.progress)
            }
        }

        holder.firstname.text = current.firstName
        holder.lastname.text = current.lastName
        holder.lifePoint.text = current.pointOfLife.toString()
        holder.ilotNumber.text = current.group.toString()
        //todo configure la ceinture
        //holder.belt


    }

    private fun displayExperience(level: Int, currentXp : Int, textView: TextView){
        when(level){
            1 -> { levelUp(currentXp, xpMax, textView) }
            2 -> {xpMax = 50
                levelUp(currentXp, xpMax, textView)}
            3 -> {xpMax = 55
                levelUp(currentXp, xpMax, textView)}
            4 -> {xpMax = 60
                levelUp(currentXp, xpMax, textView)}
            5 -> {xpMax = 65
                levelUp(currentXp, xpMax, textView)}
            6 -> {xpMax = 70
                levelUp(currentXp, xpMax, textView)}
            7 -> {xpMax = 75
                levelUp(currentXp, xpMax, textView)}
            8 -> {xpMax = 80
                levelUp(currentXp, xpMax, textView)}
            9 -> {xpMax = 85
                levelUp(currentXp, xpMax, textView)}
            10 -> {xpMax = 90
                levelUp(currentXp, xpMax, textView)}
            11 -> {xpMax = 95
                levelUp(currentXp, xpMax, textView)}
            12 -> {xpMax = 100
                levelUp(currentXp, xpMax, textView)}
            13 -> {xpMax = 105
                levelUp(currentXp, xpMax, textView)}
            14 -> {xpMax = 110
                levelUp(currentXp, xpMax, textView)}
            15 -> {xpMax = 115
                levelUp(currentXp, xpMax, textView)}
        }
    }

    private fun levelUp(xp: Int, xpMax : Int, textView: TextView): Boolean{
        var nextLvl = false
        val newMaxXp = xpMax + 5

         if(xp == xpMax){
             nextLvl = true
             val xpInfo = "$xp/$newMaxXp"
             textView.text = xpInfo
         } else if(xp > xpMax){
             val newXp = xpMax - xp
             val newXpInfo = "$newXp/$newMaxXp"
             textView.text = newXpInfo
             nextLvl = true
         } else {
             val xpText = "$xp/$xpMax"
             textView.text = xpText
         }
        return nextLvl
    }





}

class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val jobImage: ImageView = itemView.findViewById(R.id.student_job_image)
    val firstname: TextView = itemView.findViewById(R.id.student_firstname)
    val lastname: TextView = itemView.findViewById(R.id.student_lastname)
    val lifeImage: ImageView = itemView.findViewById(R.id.student_life_image)
    val lifePoint: TextView = itemView.findViewById(R.id.student_life_point)
    val level: TextView = itemView.findViewById(R.id.student_level_response)
    val xpBar: ProgressBar = itemView.findViewById(R.id.student_xp_bar)
    val xpInfo: TextView = itemView.findViewById(R.id.student_level_current_xp)
    val ilotImage: ImageView = itemView.findViewById(R.id.student_ilot_image)
    val ilotNumber: TextView = itemView.findViewById(R.id.student_ilot_number)
    val belt: ImageView = itemView.findViewById(R.id.student_belt)


    companion object{
        fun create(parent: ViewGroup): StudentViewHolder{
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
            return StudentViewHolder(view)
        }
    }


    class StudentsComparator : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.firstName == newItem.firstName
        }
    }
}


