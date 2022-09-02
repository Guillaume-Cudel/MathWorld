package ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.guillaume.mathworld.R
import model.Student
import ui.StatsUpdater


class StudentsListAdapter(private val upStats: StatsUpdater): ListAdapter<Student, StudentViewHolder>(StudentViewHolder.StudentsComparator()) {

    private var giveExperience: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = getItem(position)

        // XP & LVL
        val xpBar = holder.xpBar
        displayExperience(current.experience, current.xpMax, holder.xpInfo)
        holder.level.text = current.level.toString()
        xpBar.progress = current.experience
        xpBar.max = current.xpMax
        xpBar.setOnClickListener {
            xpBar.progress += giveExperience
            if(xpBar.progress >= xpBar.max){
                val currentXp = (current.experience + giveExperience) - xpBar.max
                val newXpMax = xpBar.max + 5
                upStats.updateExperience(current, currentXp, newXpMax)
                upStats.updateLevel(current)
            } else {
                upStats.updateExperience(current, xpBar.progress, xpBar.max)
            }
        }

        // LIFE
        displayLife(current.pointOfLife, holder.lifeImage, holder.lifePoint)
        holder.lifeImage.setOnClickListener {
            var currentLife = current.pointOfLife
            if(current.pointOfLife == 0){
                currentLife = 3
            }else currentLife--

            upStats.updateLife(current, currentLife)
        }

        // NAME
        holder.firstname.text = current.firstName
        holder.lastname.text = current.lastName

        // GROUP
        holder.ilotNumber.text = current.group.toString()
        var groupNumber = current.group
        changeGroupImageColor(groupNumber, holder.ilotImage)

        holder.ilotImage.setOnClickListener {
            if(groupNumber == 8) groupNumber = 1
            else groupNumber++
            upStats.updateGroup(current, groupNumber)
        }

        // BELT
        setBelt(current.belt, holder.belt)

        // JOB


    }

    private fun displayExperience(currentXp : Int, xpMax: Int, textView: TextView){
        val xpText = "$currentXp/$xpMax"
        textView.text = xpText
    }


    private fun displayLife(life: Int, image: ImageView, text: TextView){
        when(life){
            0 -> image.setImageResource(R.drawable.broken_heart_3)
            1 -> image.setImageResource(R.drawable.broken_heart_2)
            2 -> image.setImageResource(R.drawable.broken_heart_1)
            3 -> image.setImageResource(R.drawable.heart)
        }
        text.text = life.toString()
    }

    private fun changeGroupImageColor(number: Int, image: ImageView){
        val context = image.context
        when(number){
            1 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dark_orange)))
            2 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.turquoise)))
            3 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)))
            4 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_700)))
            5 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_200)))
            6 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow_pur)))
            7 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_light)))
            8 -> ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_light)))
        }
    }

    private fun setBelt(belt: Int, image: ImageView){
        val context = image.context
        val beltImage: ImageView = image
        beltImage.setImageResource(R.drawable.black_belt)
        when(belt){
            1 -> { image.setImageResource(R.drawable.white_belt)
                ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))) }
            2 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow_pur)))
            3 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)))
            4 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_light)))
            5 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.turquoise)))
            6 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_200)))
            7 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red_light)))
            8 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.brown)))
            9 -> ImageViewCompat.setImageTintList(beltImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)))
        }
    }

    fun updateExperienceGiven(xpChoosed: Int){
        this.giveExperience = xpChoosed
        this.notifyDataSetChanged()
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


