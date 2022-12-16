package ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guillaume.mathworld.R
import model.Student
import services.UiConfigure

class SummaryListAdapter(private val uiConfigure: UiConfigure):
    ListAdapter<Student, SummaryListViewHolder>(StudentViewHolder.StudentsComparator()){

    //var xpBeltList = listOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryListViewHolder {
        return SummaryListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SummaryListViewHolder, position: Int) {
        val current = getItem(position)

        //Name and lastname
        holder.firstnameText.text = current.firstName
        holder.lastnameText.text = current.lastName

        // Score
        holder.scoreText.text = current.beltXp.toString()
        // Xp
        holder.xpText.text = current.numNinjaXp.toString()
        // Belt
        uiConfigure.setBelt(current.currentBelt, holder.beltImage)
    }
}




class SummaryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val firstnameText: TextView = itemView.findViewById(R.id.new_belts_summary_student_firstname)
    val lastnameText: TextView = itemView.findViewById(R.id.new_belts_summary_student_lastname)
    val scoreText: TextView = itemView.findViewById(R.id.new_belts_summary_score_result)
    val beltImage: ImageView = itemView.findViewById(R.id.new_belts_summary_belt)
    val xpText: TextView = itemView.findViewById(R.id.new_belts_summary_xp_result)


    companion object {
        fun create(parent: ViewGroup): SummaryListViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_new_belts_summary, parent, false)
            return SummaryListViewHolder(view)
        }
    }
}