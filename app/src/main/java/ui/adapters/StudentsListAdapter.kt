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


class StudentsListAdapter: ListAdapter<Student, StudentViewHolder>(StudentViewHolder.StudentsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = getItem(position)
        val xpBar = holder.xp

        holder.level.text = current.level.toString()
        xpBar.setOnClickListener {
            //todo configure correctement le gain d'xp
            xpBar.progress += 1
        }

        holder.firstname.text = current.firstName
        holder.lastname.text = current.lastName
        holder.lifePoint.text = current.pointOfLife.toString()
        holder.ilotNumber.text = current.group.toString()
        //todo configure la ceinture
        //holder.belt


    }


}

class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val jobImage: ImageView = itemView.findViewById(R.id.student_job_image)
    val firstname: TextView = itemView.findViewById(R.id.student_firstname)
    val lastname: TextView = itemView.findViewById(R.id.student_lastname)
    val lifeImage: ImageView = itemView.findViewById(R.id.student_life_image)
    val lifePoint: TextView = itemView.findViewById(R.id.student_life_point)
    val level: TextView = itemView.findViewById(R.id.student_level_response)
    val xp: ProgressBar = itemView.findViewById(R.id.student_xp_bar)
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


