package ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.guillaume.mathworld.R
import model.Student
import services.StatsUpdater
import services.UiConfigure


class StudentsListAdapter(private val upStats: StatsUpdater, private val uiConfigure: UiConfigure) :
    ListAdapter<Student, StudentViewHolder>(StudentViewHolder.StudentsComparator()) {


    var giveExperience: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = getItem(position)

        // XP & LVL
        val xpBar = holder.xpBar
        uiConfigure.displayExperience(current.experience, current.xpMax, holder.xpInfo)
        holder.level.text = current.level.toString()
        xpBar.progress = current.experience
        xpBar.max = current.xpMax
        xpBar.setOnClickListener {
            xpBar.progress += giveExperience
            if (xpBar.progress >= xpBar.max) {
                val currentXp = (current.experience + giveExperience) - xpBar.max
                val newXpMax = xpBar.max + 5
                upStats.updateExperience(current, currentXp, newXpMax)
                upStats.updateLevel(current)
            } else {
                upStats.updateExperience(current, xpBar.progress, xpBar.max)
            }
        }

        // LIFE
        uiConfigure.displayHeartIconLife(current.pointOfLife, holder.lifeImage)
        uiConfigure.displayLifeNumber(current.pointOfLife, holder.lifePoint)
        holder.lifeImage.setOnClickListener {
            var currentLife = current.pointOfLife
            if (current.pointOfLife == 0) {
                currentLife = 3
            } else currentLife--

            upStats.updateLife(current, currentLife)
        }

        // NAME
        holder.firstname.text = current.firstName
        holder.lastname.text = current.lastName

        // GROUP
        holder.ilotNumber.text = current.group.toString()
        var groupNumber = current.group
        uiConfigure.changeGroupImageColor(groupNumber, holder.ilotImage)

        holder.ilotImage.setOnClickListener {
            if (groupNumber == 8) groupNumber = 1
            else groupNumber++
            upStats.updateGroup(current, groupNumber)
        }

        // BELT
        uiConfigure.setBelt(current.bestBelt, holder.belt)


        // JOB
        uiConfigure.displayJobImage(current.job, holder.jobImage)

        // Detail
        holder.itemView.setOnClickListener {
            upStats.openDetail(current)
        }

    }

    fun updateExperienceGiven(xpChoosed: Int) {
        this.giveExperience = xpChoosed
        this.notifyDataSetChanged()
    }

}

class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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


    companion object {
        fun create(parent: ViewGroup): StudentViewHolder {
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


